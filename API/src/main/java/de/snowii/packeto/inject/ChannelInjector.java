package de.snowii.packeto.inject;

public interface ChannelInjector {

    /**
     * Inject into Platform Specific Channel
     */
    void inject();

    /**
     * @return True if was Successfully Injected, False otherwise
     */
    boolean isInjected();

    /**
     * Uninject Platform Specific Channel
     */
    void uninject();
}
