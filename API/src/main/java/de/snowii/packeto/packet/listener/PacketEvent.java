package de.snowii.packeto.packet.listener;

import de.snowii.packeto.packet.BasePacketType;
import de.snowii.packeto.packet.ConnectionState;
import de.snowii.packeto.packet.PacketDirection;
import de.snowii.packeto.packet.PacketType;
import de.snowii.packeto.packet.buffer.PacketBuffer;
import io.netty.buffer.ByteBuf;


public class PacketEvent {
    private boolean isCancelled;
    private final BasePacketType packetType;
    private final PacketDirection direction;
    private final PacketBuffer packetBuffer;
    private final ConnectionState connectionState;

    public PacketEvent(PacketDirection direction, ConnectionState connectionState, ByteBuf byteBuf) {
        this.direction = direction;
        this.connectionState = connectionState;
        this.packetBuffer = new PacketBuffer(byteBuf);
        this.packetType = PacketType.getById(direction, connectionState, new PacketBuffer(byteBuf).readVarInt());
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

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public PacketBuffer getPacketBuffer() {
        return packetBuffer;
    }
}
