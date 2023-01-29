package de.snowii.packeto.platform;

import de.snowii.packeto.inject.ChannelInjector;
import de.snowii.packeto.packet.listener.ListenerManager;
import org.jetbrains.annotations.NotNull;

public interface Platform {


    /**
     * Must be Called on Enabling
     */
    void load();

    /**
     * Please don't Forget to shut down if Platform no longer needed
     */
    void shutdown();

    /**
     * @return Platform Specific Channel Injector
     */
    @NotNull ChannelInjector getInjector();

    /**
     * @return Platform Specific Packet Listener
     */
    @NotNull ListenerManager getListenerManager();
}
