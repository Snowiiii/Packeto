package de.snowii.packeto.util.softdepend.viaversion;

import org.bukkit.Bukkit;

public class ViaVersionUtil {
    private static Boolean IS_AVAILABLE;

    public static boolean isAvailable() {
        if (IS_AVAILABLE == null) {
            IS_AVAILABLE = Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
        }
        return IS_AVAILABLE;
    }
}
