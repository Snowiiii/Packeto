package de.snowii.packeto.inject;

import de.snowii.packeto.channel.inbound.SpigotChannelInitializer;
import de.snowii.packeto.util.Pair;
import de.snowii.packeto.util.SynchronizedListWrapper;
import de.snowii.packeto.util.reflection.ReflectionUtil;
import de.snowii.packeto.util.relfection.SpigotReflection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SpigotInjector implements ChannelInjector {
    protected final List<ChannelFuture> injectedFutures = new ArrayList<>();
    protected final List<Pair<Field, Object>> injectedLists = new ArrayList<>();

    private boolean injected = false;

    public boolean isServerBound() {
        if (PaperInjector.PAPER_INJECTION_METHOD) {
            return true;
        }
        try {
            final Object connection = SpigotReflection.getMinecraftServerConnectionInstance();
            if (connection == null) {
                return false;
            }

            for (Field field : connection.getClass().getDeclaredFields()) {
                if (!List.class.isAssignableFrom(field.getType())) {
                    continue;
                }

                field.setAccessible(true);
                List<?> value = (List<?>) field.get(connection);
                // Check if the list has at least one element
                synchronized (value) {
                    if (!value.isEmpty() && value.get(0) instanceof ChannelFuture) {
                        return true;
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void inject() {
        if (this.injected) throw new IllegalStateException("Tried to inject Packeto although it Already injected");

        if (PaperInjector.PAPER_INJECTION_METHOD) {
            try {
                PaperInjector.setPaperChannelInitializeListener();
                this.injected = true;
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Packet: Failed to Init Paper Channel Listener", e);
            }
            return;
        }


        try {
            final var serverConnection = SpigotReflection.getMinecraftServerConnectionInstance();
            if (serverConnection != null) {
                for (final Field field : serverConnection.getClass().getDeclaredFields()) {
                    // Check for list with the correct generic type
                    if (!List.class.isAssignableFrom(field.getType()) || !field.getGenericType().getTypeName().contains(ChannelFuture.class.getName())) {
                        continue;
                    }
                    field.setAccessible(true);
                    final List<ChannelFuture> list = (List<ChannelFuture>) field.get(serverConnection);
                    final List<ChannelFuture> wrappedList = new SynchronizedListWrapper<>(list, this::injectChannel);
                    synchronized (list) {
                        // Iterate through current list
                        for (ChannelFuture future : list) {
                            this.injectChannel(future);
                        }

                        field.set(serverConnection, wrappedList);
                    }

                    injectedLists.add(new Pair<>(field, serverConnection));
                }
            }
            this.injected = true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isInjected() {
        return injected;
    }

    private void injectChannel(final ChannelFuture future) {
        final List<String> names = future.channel().pipeline().names();
        ChannelHandler bootstrapAcceptor = null;
        // Find the right channelhandler
        for (final String name : names) {
            final var handler = future.channel().pipeline().get(name);
            try {
                ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class);
                bootstrapAcceptor = handler;
                break;
            } catch (ReflectiveOperationException ignored) {
                // Not this one
            }
        }

        if (bootstrapAcceptor == null) {
            // Default to first (also allows blame to work)
            bootstrapAcceptor = future.channel().pipeline().first();
        }

        try {
            final ChannelInitializer<Channel> oldInitializer = ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
            ReflectionUtil.set(bootstrapAcceptor, "childHandler", new SpigotChannelInitializer(oldInitializer));
            injectedFutures.add(future);
        } catch (NoSuchFieldException ignored) {
            throw new RuntimeException(ignored + bootstrapAcceptor.toString());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uninject() {
        if (!this.injected) throw new IllegalStateException("Tried to uninject Packeto although its not Injected");
        if (PaperInjector.PAPER_INJECTION_METHOD) {
            try {
                PaperInjector.removePaperChannelInitializeListener();
                this.injected = false;
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Packet: Failed to remove Paper Channel Listener", e);
            }
            return;
        }

        for (final ChannelFuture future : injectedFutures) {
            // Default to first
            final var pipeline = future.channel().pipeline();
            var bootstrapAcceptor = pipeline.first();
            if (bootstrapAcceptor == null) {
                continue;
            }

            // Pick best
            for (final String name : pipeline.names()) {
                final var handler = pipeline.get(name);
                if (handler == null) {
                    continue;
                }

                try {
                    if (ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class) instanceof WrappedChannelInitializer) {
                        bootstrapAcceptor = handler;
                        break;
                    }
                } catch (ReflectiveOperationException ignored) {
                }
            }

            try {
                final ChannelInitializer<Channel> initializer = ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                if (initializer instanceof WrappedChannelInitializer) {
                    ReflectionUtil.set(bootstrapAcceptor, "childHandler", ((WrappedChannelInitializer) initializer).original());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        injectedFutures.clear();

        for (Pair<Field, Object> pair : injectedLists) {
            try {
                final Field field = pair.key();
                final Object o = field.get(pair.value());
                if (o instanceof SynchronizedListWrapper) {
                    final List<ChannelFuture> originalList = ((SynchronizedListWrapper) o).originalList();
                    synchronized (originalList) {
                        field.set(pair.value(), originalList);
                    }
                }
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        injectedLists.clear();
        this.injected = false;
    }
}
