package de.snowii.packeto.packet.listener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ListenerManager {

    boolean callEventNormal(final @NotNull PacketEvent event);

    void callEventReadonly(final @NotNull PacketEvent event);

    void registerListener(final @NotNull PacketListener listener, final boolean readOnly);

    void removeListener(final @NotNull PacketListener listener, final boolean readOnly);

    void removeAllListeners();

    List<?> getPacketListeners();

    List<?> getReadonlyPacketListeners();
}
