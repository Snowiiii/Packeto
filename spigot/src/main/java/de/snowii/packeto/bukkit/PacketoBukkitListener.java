package de.snowii.packeto.bukkit;

import de.snowii.packeto.channel.PacketEncoder;
import de.snowii.packeto.util.relfection.SpigotReflection;
import io.netty.channel.Channel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PacketoBukkitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        final Channel channel = (Channel) SpigotReflection.getChannel(event.getPlayer());
        if (!Objects.requireNonNull(channel).isOpen()) return;
        Objects.requireNonNull(this.getEncoder(channel)).setPlayer(event.getPlayer());
    }

    private @Nullable PacketEncoder getEncoder(Channel channel) {
        return channel.pipeline().get(PacketEncoder.class);
    }
}
