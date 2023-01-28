package de.snowii.packeto.inject;

public interface ChannelInjector {

    void inject();

    boolean isInjected();

    void uninject();
}
