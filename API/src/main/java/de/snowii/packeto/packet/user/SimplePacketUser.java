package de.snowii.packeto.packet.user;

import de.snowii.packeto.packet.ConnectionState;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class SimplePacketUser {
    private final Channel channel;
    private ConnectionState state = ConnectionState.HANDSHAKING;
    private UUID uuid;

    public SimplePacketUser(final @NotNull Channel channel) {
        this.channel = channel;
    }

    public void setState(ConnectionState state) {
        this.state = state;
    }

    public ConnectionState getState() {
        return state;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
}
