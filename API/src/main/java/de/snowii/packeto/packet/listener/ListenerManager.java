package de.snowii.packeto.packet.listener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @param <T> Event
 * @param <E> Listener
 */
public interface ListenerManager<T, E> {

    /**
     * for Packeto's Internal use
     *
     * @return True if Event was Cancelled, False otherwise
     */
    boolean callEventNormal(final @NotNull T event);

    /**
     * for Packeto's Internal use
     */
    void callEventReadonly(final @NotNull T event);

    /**
     * Register a new Platform Specific Packet Listener
     *
     * @param listener Platform Listener
     * @param readOnly If True Listener will not able to Cancel the Packet, So the Listener will be a Readonly Listener this can Speed up things and prevent Bugs
     */
    void registerListener(final @NotNull E listener, final boolean readOnly);

    /**
     * Removed a Platform Specific Packet Listener
     *
     * @param listener Platform Listener
     */
    void removeListener(final @NotNull E listener);

    /**
     * Removes all Platform Specific Packet Listeners
     */
    void removeAllListeners();

    List<E> getPacketListeners();

    List<E> getReadonlyPacketListeners();
}
