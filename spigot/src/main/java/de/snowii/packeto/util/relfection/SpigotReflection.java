package de.snowii.packeto.util.relfection;

import de.snowii.packeto.util.reflection.ReflectionObject;
import de.snowii.packeto.util.reflection.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.Channel;

public class SpigotReflection {
    private static String CRAFTBUKKIT_PACKAGE;
    private static String MINECRAFT_PREFIX_PACKAGE = "net.minecraft.server.";
    private static String MINECRAFT_FULL_PACKAGE = null;

    private static boolean hasInit;

    public static void init() {
        if (hasInit) throw new IllegalStateException("Tried to Init SpigotReflection twice");
        final Server craftServer = Bukkit.getServer();
        CRAFTBUKKIT_PACKAGE = craftServer.getClass().getPackage().getName();
        MINECRAFT_FULL_PACKAGE = MINECRAFT_PREFIX_PACKAGE = "net.minecraft.";

        hasInit = true;
    }

    public static Class<?> getMinecraftClass(final @NotNull String className) {
        try {
            return Class.forName(MINECRAFT_FULL_PACKAGE + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find Minecraft class: " + className);
        }
    }

    public static Class<?> getPlayerConnection() {
        return getMinecraftClass("server.network.PlayerConnection", "server.network.ServerGamePacketListenerImpl", "PlayerConnection");
    }

    public static Class<?> getMinecraftClass(final @NotNull String className, String... aliases) {
        try {
            return Class.forName(MINECRAFT_FULL_PACKAGE + className);
        } catch (ClassNotFoundException e) {
            Class<?> clazz;
            for (String alias : aliases) {
                try {
                    clazz = Class.forName(MINECRAFT_FULL_PACKAGE + alias);
                    if (clazz != null) return clazz;
                } catch (ClassNotFoundException ex) {
                }
            }
        }
        throw new RuntimeException("Failed to find Minecraft class: " + className + " (Package: " + MINECRAFT_FULL_PACKAGE + " )");
    }

    public static Class<?> getCraftBukkit(final @NotNull String className) {
        try {
            return Class.forName(CRAFTBUKKIT_PACKAGE + "." + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find CraftBukkit class: " + className + " (Package: " + CRAFTBUKKIT_PACKAGE + " )");
        }
    }

    public static Class<?> getCraftServer() {
        return getCraftBukkit("CraftServer");
    }


    public static Object getMinecraftServerInstance(final @NotNull Server server) {
        try {
            return ReflectionUtil.getField(getCraftServer(), getMinecraftServer(), 0)
                    .get(server);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getMinecraftServerConnectionInstance() {
        try {
            return ReflectionUtil.getField(getMinecraftServer(), getServerConnection(), 0).get(getMinecraftServerInstance(Bukkit.getServer()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getCraftPlayer(final @NotNull Player player) {
        return getCraftBukkit("entity.CraftPlayer").cast(player);
    }

    public static Method getCraftPlayerHandle() {
        try {
            return getCraftBukkit("entity.CraftPlayer").getMethod("getHandle");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getEntityPlayer() {
        try {
            return getMinecraftClass("server.level.EntityPlayer", "server.level.ServerPlayer", "EntityPlayer");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getEntityPlayer(final @NotNull Player player) {
        Object craftPlayer = getCraftPlayer(player);
        try {
            return getCraftPlayerHandle().invoke(craftPlayer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    public static Object getPlayerConnection(final @NotNull Player player) {
        Object entityPlayer = getEntityPlayer(player);
        if (entityPlayer == null) {
            return null;
        }
        ReflectionObject wrappedEntityPlayer = new ReflectionObject(entityPlayer, getEntityPlayer());
        return wrappedEntityPlayer.readObject(0, getPlayerConnection());
    }

    public static Class<?> getNetworkManager() {
        return getMinecraftClass("network.NetworkManager", "network.Connection", "NetworkManager");
    }

    public static Object getNetworkManager(final @NotNull Player player) {
        Object playerConnection = getPlayerConnection(player);
        if (playerConnection == null) {
            return null;
        }
        ReflectionObject wrapper = new ReflectionObject(playerConnection, getPlayerConnection());
        try {
            return wrapper.readObject(0, getNetworkManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getChannel(final @NotNull Player player) {
        Object networkManager = getNetworkManager(player);
        if (networkManager == null) {
            return null;
        }
        ReflectionObject wrapper = new ReflectionObject(networkManager, getNetworkManager());
        return wrapper.readObject(0, Channel.class);
    }

    public static Class<?> getServerConnection() {
        return getMinecraftClass("server.network.ServerConnection", "server.network.ServerConnectionListener", "ServerConnection");
    }

    public static Class<?> getMinecraftServer() {
        try {
            return getMinecraftClass("server.MinecraftServer", "MinecraftServer");
        } catch (RuntimeException e) {
            return getMinecraftClass("MinecraftServer");
        }
    }
}
