package de.snowii.packeto.packet.listener;

import de.snowii.packeto.packet.BasePacketType;
import de.snowii.packeto.packet.PacketDirection;
import de.snowii.packeto.packet.PacketType;
import de.snowii.packeto.packet.buffer.PacketBuffer;
import de.snowii.packeto.packet.user.SimplePacketUser;
import io.netty.buffer.ByteBuf;


public class PacketEvent {
    private boolean isCancelled;
    private final BasePacketType packetType;
    private final PacketDirection direction;
    private final PacketBuffer packetBuffer;
    private final SimplePacketUser packetUser;

    public PacketEvent(PacketDirection direction, SimplePacketUser packetUser, ByteBuf byteBuf) {
        this.direction = direction;
        this.packetUser = packetUser;
        this.packetBuffer = new PacketBuffer(byteBuf);
        this.packetType = PacketType.getById(direction, packetUser.getState(), this.packetBuffer.readVarInt());
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public PacketDirection getDirection() {
        return this.direction;
    }

    public BasePacketType getPacketType() {
        return packetType;
    }

    public SimplePacketUser getPacketUser() {
        return packetUser;
    }

    public PacketBuffer getPacketBuffer() {
        return packetBuffer;
    }
}
