package de.snowii.packeto.inject;

import de.snowii.packeto.channel.inbound.SpigotChannelInitializer;
import io.netty.channel.Channel;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PaperInjector {
    public static final Object COMPRESSION_ENABLED_EVENT = paperCompressionEnabledEvent();
    public static final boolean PAPER_INJECTION_METHOD = hasPaperInjectionMethod();
    public static final boolean PAPER_PROTOCOL_METHOD = hasServerProtocolMethod();


    public static void addPaperChannelInitializeListener() throws ReflectiveOperationException {
        // Call io.papermc.paper.network.ChannelInitializeListenerHolder.addListener(net.kyori.adventure.key.Key, io.papermc.paper.network.ChannelInitializeListener)
        // Create an interface proxy of ChannelInitializeListener
        final Class<?> listenerClass = Class.forName("io.papermc.paper.network.ChannelInitializeListener");
        final Object channelInitializeListener = Proxy.newProxyInstance(SpigotChannelInitializer.class.getClassLoader(), new Class[]{listenerClass}, (proxy, method, args) -> {
            if (method.getName().equals("afterInitChannel")) {
                SpigotChannelInitializer.afterInitChannel((Channel) args[0]);
                return null;
            }
            return method.invoke(proxy, args);
        });

        final Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        final Method addListenerMethod = holderClass.getDeclaredMethod("addListener", Key.class, listenerClass);
        addListenerMethod.invoke(null, Key.key("packeto", "injector"), channelInitializeListener);
    }

    public static void removePaperChannelInitializeListener() throws ReflectiveOperationException {
        final Class<?> holderClass = Class.forName("io.papermc.paper.network.ChannelInitializeListenerHolder");
        final Method addListenerMethod = holderClass.getDeclaredMethod("removeListener", Key.class);
        addListenerMethod.invoke(null, Key.key("packeto", "injector"));
    }

    private static boolean hasServerProtocolMethod() {
        try {
            Bukkit.getUnsafe().getClass().getDeclaredMethod("getProtocolVersion");
            return true;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private static @Nullable Object paperCompressionEnabledEvent() {
        try {
            final Class<?> eventClass = Class.forName("io.papermc.paper.network.ConnectionEvent");
            return eventClass.getDeclaredField("COMPRESSION_THRESHOLD_SET").get(null);
        } catch (final ReflectiveOperationException e) {
            return null;
        }
    }

    private static boolean hasPaperInjectionMethod() {
        return hasClass("io.papermc.paper.network.ChannelInitializeListener");
    }

    private static boolean hasClass(final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }
}
