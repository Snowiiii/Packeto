package de.snowii.packeto.platform;

import de.snowii.packeto.inject.ChannelInjector;
import de.snowii.packeto.packet.listener.ListenerManager;
import org.jetbrains.annotations.NotNull;

public interface Platform {


    /**
     * Must be Called on Enabling
     */
    void load();

    void shutdown();

    @NotNull ChannelInjector getInjector();

    @NotNull ListenerManager getListenerManager();
}
