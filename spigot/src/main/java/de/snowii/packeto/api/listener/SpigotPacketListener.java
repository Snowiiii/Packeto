package de.snowii.packeto.api.listener;

import org.jetbrains.annotations.NotNull;

public abstract class SpigotPacketListener {
    public void onPacketReceive(final @NotNull SpigotPacketEvent event) {

    }

    public void onPacketSend(final @NotNull SpigotPacketEvent event) {
    }
}
