package de.snowii.packeto.packet.listener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @param <T> Event
 * @param <E> Listener
 */
public interface ListenerManager<T, E> {

    boolean callEventNormal(final @NotNull T event);

    void callEventReadonly(final @NotNull T event);

    void registerListener(final @NotNull E listener, final boolean readOnly);

    void removeListener(final @NotNull E listener, final boolean readOnly);

    void removeAllListeners();

    List<E> getPacketListeners();

    List<E> getReadonlyPacketListeners();
}
