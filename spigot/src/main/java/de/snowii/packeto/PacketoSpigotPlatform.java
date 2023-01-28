package de.snowii.packeto;

import co.aikar.timings.lib.TimingManager;
import de.snowii.packeto.api.listener.SpigotListenerManager;
import de.snowii.packeto.api.listener.SpigotPacketEvent;
import de.snowii.packeto.api.listener.SpigotPacketListener;
import de.snowii.packeto.bukkit.PacketoBukkitListener;
import de.snowii.packeto.inject.ChannelInjector;
import de.snowii.packeto.inject.PaperInjector;
import de.snowii.packeto.inject.SpigotInjector;
import de.snowii.packeto.packet.ConnectionState;
import de.snowii.packeto.packet.PacketType;
import de.snowii.packeto.packet.listener.ListenerManager;
import de.snowii.packeto.platform.Platform;
import de.snowii.packeto.util.PipelineUtil;
import de.snowii.packeto.util.relfection.SpigotReflection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketoSpigotPlatform implements Platform {
    private static TimingManager timingManager;
    private SpigotInjector injector;
    private SpigotListenerManager listenerManager;
    private final JavaPlugin plugin;

    private static PacketoSpigotPlatform instance;

    public PacketoSpigotPlatform(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        timingManager = TimingManager.of(plugin);
    }

    @Override
    public void postLoad() {
        Bukkit.getPluginManager().registerEvents(new PacketoBukkitListener(), this.plugin);
    }

    @Override
    public void preLoad() {
        if (instance == null) {
            instance = this;
            // important Reflection must be init first
            SpigotReflection.init();
            if (!PaperInjector.PAPER_INJECTION_METHOD) {
                PipelineUtil.init();
            }
            this.injector = new SpigotInjector();
            this.injector.inject();
            this.listenerManager = new SpigotListenerManager();
            this.listenerManager.registerListener(new SpigotPacketListener() {
                @Override
                public void onPacketReceive(final @NotNull SpigotPacketEvent event) {
                    if (event.getPacketType().equals(PacketType.Client.Handshake.HANDSHAKE)) {
                        event.getPacketUser().setState(ConnectionState.LOGIN);
                    }
                }

                @Override
                public void onPacketSend(final @NotNull SpigotPacketEvent event) {
                    if (event.getPacketType().equals(PacketType.Server.Login.LOGIN_SUCCESS)) {
                        event.getPacketUser().setState(ConnectionState.PLAY);
                    }
                }
            }, false);
        } // Else Already loaded
    }

    @Override
    public void shutdown() {
        if (this.injector.isInjected()) this.injector.uninject();
    }

    @Override
    public @NotNull ChannelInjector getInjector() {
        return this.injector;
    }

    @Override
    public @NotNull ListenerManager getListenerManager() {
        return this.listenerManager;
    }

    public static @NotNull TimingManager getTimingManager() {
        return timingManager;
    }

    public static @Nullable PacketoSpigotPlatform getInstance() {
        return instance;
    }
}
