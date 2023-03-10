package de.snowii.packeto.packet;

import de.snowii.packeto.annotation.AddedMC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class PacketType implements Serializable, Cloneable {
    @SuppressWarnings("unused")
    public static final int UNKNOWN_PACKET = -1;

    private final ConnectionState state;
    private final PacketDirection direction;
    private final int currentId;

    public PacketType(@NotNull ConnectionState state, @NotNull PacketDirection direction, int currentId) {
        this.state = state;
        this.direction = direction;
        this.currentId = currentId;
    }

    /**
     * Packets that are Send by the Server to the Client
     */
    @SuppressWarnings("unused")
    public static class Server {

        public static class Play {


        }

        public enum Status implements BasePacketType {
            RESPONSE(0),
            PONG(1);

            private final int id;

            Status(int id) {
                this.id = id;
            }

            @Nullable
            public static BasePacketType getById(final int packetID) {
                return values()[packetID];
            }

            public int getId() {
                return this.id;
            }

        }

        public enum Login implements BasePacketType {
            DISCONNECT(0),
            ENCRYPTION_REQUEST(1),
            LOGIN_SUCCESS(2),
            SET_COMPRESSION(3),
            @AddedMC(version = "1.13")
            LOGIN_PLUGIN_REQUEST(4);

            private final int id;

            Login(int id) {
                this.id = id;
            }

            @Nullable
            public static BasePacketType getById(int packetID) {
                return values()[packetID];
            }

            @Override
            public int getId() {
                return this.id;
            }
        }
    }

    /**
     * Packets that are Send by the Client to the Server
     */
    @SuppressWarnings("unused")
    public static class Client {

        public enum Handshake implements BasePacketType {
            HANDSHAKE(0);

            private final int id;

            Handshake(int id) {
                this.id = id;
            }

            @Nullable
            public static BasePacketType getById(final int packetID) {
                return values()[packetID];
            }


            @Override
            public int getId() {
                return this.id;
            }
        }

        public enum Status implements BasePacketType {
            REQUEST(0),
            PING(1);

            private final int id;

            Status(int id) {
                this.id = id;
            }

            @Nullable
            public static BasePacketType getById(final int packetID) {
                return values()[packetID];
            }

            public int getId() {
                return this.id;
            }
        }

        public enum Login implements BasePacketType {
            LOGIN_START(0),
            ENCRYPTION_RESPONSE(1),
            @AddedMC(version = "1.13")
            LOGIN_PLUGIN_RESPONSE(2);

            private final int id;

            Login(int id) {
                this.id = id;
            }

            @Nullable
            public static BasePacketType getById(int packetID) {
                return values()[packetID];
            }

            @Override
            public int getId() {
                return this.id;
            }
        }

        public enum Play {
        }


    }

    public static BasePacketType getById(PacketDirection side, ConnectionState state, int packetID) {
        switch (state) {
            case HANDSHAKING -> {
                if (side == PacketDirection.CLIENT) {
                    return Client.Handshake.getById(packetID);
                }
            }
            case STATUS -> {
                if (side == PacketDirection.CLIENT) {
                    return Client.Status.getById(packetID);
                } else {
                    return Server.Status.getById(packetID);
                }
            }
            case LOGIN -> {
                if (side == PacketDirection.CLIENT) {
                    return Client.Login.getById(packetID);
                } else {
                    return Server.Login.getById(packetID);
                }
            }
            /**  case PLAY:
             if (side == PacketSide.CLIENT) {
             return Play.Client.getById(version, packetID);
             } else {
             return Play.Server.getById(version, packetID);
             }
             **/
        }
        return null;
    }


    @Override
    public PacketType clone() {
        try {
            return (PacketType) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
