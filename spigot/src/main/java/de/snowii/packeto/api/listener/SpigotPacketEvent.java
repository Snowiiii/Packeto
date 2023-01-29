package de.snowii.packeto.api.listener;

import de.snowii.packeto.packet.PacketDirection;
import de.snowii.packeto.packet.listener.PacketEvent;
import de.snowii.packeto.packet.user.SimplePacketUser;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class SpigotPacketEvent extends PacketEvent {
    private final Player player;

    public SpigotPacketEvent(final @Nullable Player player, final PacketDirection direction, final SimplePacketUser simplePacketUser, final ByteBuf byteBuf) {
        super(direction, simplePacketUser, byteBuf);
        this.player = player;
    }

    /**
     * If ConnectionState != Play this will return null
     */
    public @Nullable Player getPlayer() {
        return player;
    }
}
