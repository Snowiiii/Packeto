package de.snowii.packeto.api.listener;

import de.snowii.packeto.packet.listener.ListenerManager;
import de.snowii.packeto.packet.listener.PacketEvent;
import de.snowii.packeto.packet.listener.PacketListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpigotListenerManager implements ListenerManager {
    private final List<SpigotPacketListener> packetListeners = new ArrayList<>();

    private final List<SpigotPacketListener> readonlyPacketListeners = new ArrayList<>();

    @Override
    public boolean callEventNormal(final @NotNull PacketEvent event) {
        if (event instanceof SpigotPacketEvent spigotPacketEvent) {
            switch (event.getDirection()) {
                case SERVER -> {
                    for (final var packetListener : this.packetListeners) {
                        packetListener.onPacketSend(spigotPacketEvent);
                    }
                }
                case CLIENT -> {
                    for (final var packetListener : this.packetListeners) {
                        packetListener.onPacketReceive(spigotPacketEvent);
                    }
                }
            }
        } else throw new IllegalStateException("Tried to Call a Non Spigot Event on Spigot Platform");
        return event.isCancelled();
    }

    @Override
    public void callEventReadonly(final @NotNull PacketEvent event) {
        if (event instanceof SpigotPacketEvent spigotPacketEvent) {
            switch (event.getDirection()) {
                case SERVER -> {
                    for (final var packetListener : this.readonlyPacketListeners) {
                        packetListener.onPacketSend(spigotPacketEvent);
                    }
                }
                case CLIENT -> {
                    for (final var packetListener : this.readonlyPacketListeners) {
                        packetListener.onPacketReceive(spigotPacketEvent);
                    }
                }
            }
        } else throw new IllegalStateException("Tried to Call a Non Spigot Event on Spigot Platform");
    }

    @Override
    public void registerListener(@NotNull PacketListener listener, boolean readOnly) {
        if (listener instanceof SpigotPacketListener spigotPacketListener) {
            if (readOnly) {
                this.readonlyPacketListeners.add(spigotPacketListener);
            } else {
                this.packetListeners.add(spigotPacketListener);
            }
        } else throw new IllegalStateException("Tried to Register a Non Spigot Listener on Spigot Platform");
    }

    @Override
    public void removeListener(@NotNull PacketListener listener, boolean readOnly) {
        if (listener instanceof SpigotPacketListener spigotPacketListener) {
            if (readOnly) {
                this.readonlyPacketListeners.remove(listener);
            } else {
                this.packetListeners.remove(listener);
            }
        } else throw new IllegalStateException("Tried to Remove a Non Spigot Listener on Spigot Platform");
    }

    @Override
    public void removeAllListeners() {
        this.packetListeners.clear();
        this.readonlyPacketListeners.clear();
    }

    @Override
    public List<?> getPacketListeners() {
        return this.packetListeners;
    }

    @Override
    public List<?> getReadonlyPacketListeners() {
        return this.readonlyPacketListeners;
    }
}
