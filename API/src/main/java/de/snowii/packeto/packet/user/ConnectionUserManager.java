package de.snowii.packeto.packet.user;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionUserManager {
    private static final Map<UUID, SimplePacketUser> clients = new ConcurrentHashMap<>();
    private static final Set<SimplePacketUser> connections = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void newUser(final @NotNull SimplePacketUser user) {
        Channel channel = user.getChannel();

        clients.put(user.getUUID(), user);

        if (channel != null) {
            // disconnected
            if (!channel.isOpen()) return;
            else if (connections.add(user)) {
                channel.closeFuture().addListener((ChannelFutureListener) future -> onDisconnect(user));
            }
        }
    }

    public static void onDisconnect(final @NotNull SimplePacketUser connection) {
        connections.remove(connection);
        clients.remove(connection.getUUID());
    }

    public static Map<UUID, SimplePacketUser> getClients() {
        return clients;
    }

    public static Set<SimplePacketUser> getConnections() {
        return connections;
    }
}
