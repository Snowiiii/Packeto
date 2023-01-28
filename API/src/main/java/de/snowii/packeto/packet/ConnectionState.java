package de.snowii.packeto.packet;

public enum ConnectionState {
    HANDSHAKING,
    STATUS,
    LOGIN,
    PLAY;

    public ConnectionState next() {
        switch (this) {
            case HANDSHAKING -> {
                return STATUS;
            }
            case STATUS -> {
                return LOGIN;
            }
            case LOGIN -> {
                return PLAY;
            }
            case PLAY -> throw new IllegalStateException("Next State, but State is Already Play ?");
            default -> throw new IllegalStateException("Next State Not found");
        }
    }
}
