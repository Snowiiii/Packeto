package de.snowii.packeto.api.listener;

import de.snowii.packeto.packet.ConnectionState;
import de.snowii.packeto.packet.PacketDirection;
import de.snowii.packeto.packet.listener.PacketEvent;
import io.netty.buffer.ByteBuf;

public class SpigotPacketEvent extends PacketEvent {

    public SpigotPacketEvent(final PacketDirection direction, final ConnectionState connectionState, final ByteBuf byteBuf) {
        super(direction, connectionState, byteBuf);
    }
}
