package de.snowii.packeto.packet;

public interface BasePacketType {
    default String getName() {
        return ((Enum<?>) this).name();
    }

    int getId();

}
