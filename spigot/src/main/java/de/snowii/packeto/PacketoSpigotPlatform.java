package de.snowii.packeto;

import co.aikar.timings.lib.TimingManager;
import de.snowii.packeto.api.listener.SpigotListenerManager;
import de.snowii.packeto.inject.ChannelInjector;
import de.snowii.packeto.inject.PaperInjector;
import de.snowii.packeto.inject.SpigotInjector;
import de.snowii.packeto.packet.listener.ListenerManager;
import de.snowii.packeto.platform.Platform;
import de.snowii.packeto.util.PipelineUtil;
import de.snowii.packeto.util.relfection.SpigotReflection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketoSpigotPlatform implements Platform {
    private static TimingManager timingManager;
    private SpigotInjector injector;
    private SpigotListenerManager listenerManager;

    private static PacketoSpigotPlatform instance;

    public PacketoSpigotPlatform(final @NotNull JavaPlugin plugin) {
        timingManager = TimingManager.of(plugin);
    }

    @Override
    public void load() {
        if (instance != null) { // Already loaded
            instance = this;
            // important Reflection must be init first
            SpigotReflection.init();
            if (!PaperInjector.PAPER_INJECTION_METHOD) {
                PipelineUtil.init();
            }
            this.injector = new SpigotInjector();
            this.injector.inject();
            this.listenerManager = new SpigotListenerManager();
        }
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
