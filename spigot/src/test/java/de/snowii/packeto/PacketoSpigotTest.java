package de.snowii.packeto;

import de.snowii.packeto.api.listener.SpigotPacketEvent;
import de.snowii.packeto.api.listener.SpigotPacketListener;
import de.snowii.packeto.packet.PacketType;
import de.snowii.packeto.platform.Platform;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PacketoSpigotTest extends JavaPlugin {
    private static PacketoSpigotTest instance;

    private static Platform platform;

    @Override
    public void onEnable() {
        platform.postLoad();
        platform.getListenerManager().registerListener(new SpigotPacketListener() {
            @Override
            public void onPacketReceive(@NotNull SpigotPacketEvent event) {
                if (event.getPacketType().equals(PacketType.Client.Login.LOGIN_START)) {
                    getLogger().warning("Login Start");
                }
            }

            @Override
            public void onPacketSend(@NotNull SpigotPacketEvent event) {
            }
        }, true);
    }

    @Override
    public void onLoad() {
        instance = this;
        getLogger().warning("Loading Packeto Spigot Test");
        platform = new PacketoSpigotPlatform(this);
        platform.preLoad();
    }

    @Override
    public void onDisable() {
        platform.shutdown();
    }

    public static PacketoSpigotTest getInstance() {
        return instance;
    }
}
