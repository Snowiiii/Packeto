package de.snowii.packeto.platform;

import de.snowii.packeto.inject.ChannelInjector;
import de.snowii.packeto.packet.listener.ListenerManager;
import org.jetbrains.annotations.NotNull;

public interface Platform {

    /**
     * Called if Plugin has been Enabled
     */
    void postLoad();

    /**
     * Called if Plugin currently Loading
     */
    void preLoad();

    void shutdown();

    @NotNull ChannelInjector getInjector();

    @NotNull ListenerManager getListenerManager();
}
