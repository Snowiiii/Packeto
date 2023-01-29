package de.snowii.packeto.api.listener;

import de.snowii.packeto.packet.listener.ListenerManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpigotListenerManager implements ListenerManager<SpigotPacketEvent, SpigotPacketListener> {
    private final List<SpigotPacketListener> packetListeners = new ArrayList<>();

    private final List<SpigotPacketListener> readonlyPacketListeners = new ArrayList<>();

    @Override
    public boolean callEventNormal(final @NotNull SpigotPacketEvent event) {
        switch (event.getDirection()) {
            case SERVER -> {
                for (final var packetListener : this.packetListeners) {
                    packetListener.onPacketSend(event);
                }
            }
            case CLIENT -> {
                for (final var packetListener : this.packetListeners) {
                    packetListener.onPacketReceive(event);
                }
            }
        }
        return event.isCancelled();
    }

    @Override
    public void callEventReadonly(final @NotNull SpigotPacketEvent event) {
        switch (event.getDirection()) {
            case SERVER -> {
                for (final var packetListener : this.readonlyPacketListeners) {
                    packetListener.onPacketSend(event);
                }
            }
            case CLIENT -> {
                for (final var packetListener : this.readonlyPacketListeners) {
                    packetListener.onPacketReceive(event);
                }
            }
        }
    }

    @Override
    public void registerListener(final @NotNull SpigotPacketListener listener, final boolean readOnly) {
        if (readOnly) {
            this.readonlyPacketListeners.add(listener);
        } else {
            this.packetListeners.add(listener);
        }
    }

    @Override
    public void removeListener(final @NotNull SpigotPacketListener listener) {
        if (!this.packetListeners.remove(listener)) {
            this.readonlyPacketListeners.remove(listener);
        }
    }

    @Override
    public void removeAllListeners() {
        this.packetListeners.clear();
        this.readonlyPacketListeners.clear();
    }

    @Override
    public List<SpigotPacketListener> getPacketListeners() {
        return this.packetListeners;
    }

    @Override
    public List<SpigotPacketListener> getReadonlyPacketListeners() {
        return this.readonlyPacketListeners;
    }
}
