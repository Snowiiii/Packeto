package de.snowii.packeto.util.version;

import org.jetbrains.annotations.NotNull;

public class MinecraftVersion implements Comparable<MinecraftVersion> {

    public static MinecraftVersion V1_19 = new MinecraftVersion(759);
    public static MinecraftVersion V1_18 = new MinecraftVersion(757);
    public static MinecraftVersion V1_17 = new MinecraftVersion(755);
    public static MinecraftVersion V1_16 = new MinecraftVersion(735);
    public static MinecraftVersion V1_15 = new MinecraftVersion(573);
    public static MinecraftVersion V1_14 = new MinecraftVersion(477);
    public static MinecraftVersion V1_13 = new MinecraftVersion(393);
    public static MinecraftVersion V1_12 = new MinecraftVersion(335);
    public static MinecraftVersion V1_11 = new MinecraftVersion(315);
    public static MinecraftVersion V1_10 = new MinecraftVersion(210);
    public static MinecraftVersion V1_9 = new MinecraftVersion(107);
    public static MinecraftVersion V1_8 = new MinecraftVersion(47);

    public static MinecraftVersion LATEST = V1_19;


    private final int version;

    public MinecraftVersion(final int version) {
        this.version = version;
    }

    public int getProtocolVersion() {
        return version;
    }

    @Override
    public int compareTo(@NotNull MinecraftVersion minecraftVersion) {
        return minecraftVersion.version == this.version ? this.version : 1;
    }
}
