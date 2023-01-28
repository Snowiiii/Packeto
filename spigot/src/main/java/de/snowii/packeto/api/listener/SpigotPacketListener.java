package de.snowii.packeto.api.listener;

import de.snowii.packeto.packet.listener.PacketListener;
import org.jetbrains.annotations.NotNull;

public abstract class SpigotPacketListener implements PacketListener {
    public void onPacketReceive(final @NotNull SpigotPacketEvent event) {

    }

    public void onPacketSend(final @NotNull SpigotPacketEvent event) {
    }
}
