package de.snowii.packeto.bukkit;

import de.snowii.packeto.channel.PacketEncoder;
import de.snowii.packeto.packet.user.ConnectionUserManager;
import de.snowii.packeto.packet.user.SimplePacketUser;
import de.snowii.packeto.util.relfection.SpigotReflection;
import io.netty.channel.Channel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;

public class PacketoBukkitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        final Channel channel = (Channel) SpigotReflection.getChannel(event.getPlayer());
        if (!channel.isOpen()) return;
        final SimplePacketUser user = this.getSimpleUser(channel);
        assert user != null;
        ConnectionUserManager.newUser(user);
    }

    private @Nullable SimplePacketUser getSimpleUser(Channel channel) {
        PacketEncoder encoder = channel.pipeline().get(PacketEncoder.class);
        return encoder != null ? encoder.getSimplePacketUser() : null;
    }
}
