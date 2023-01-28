package de.snowii.packeto.api.listener;

import de.snowii.packeto.packet.PacketDirection;
import de.snowii.packeto.packet.listener.PacketEvent;
import de.snowii.packeto.packet.user.SimplePacketUser;
import io.netty.buffer.ByteBuf;

public class SpigotPacketEvent extends PacketEvent {

    public SpigotPacketEvent(final PacketDirection direction, final SimplePacketUser simplePacketUser, final ByteBuf byteBuf) {
        super(direction, simplePacketUser, byteBuf);
    }
}
