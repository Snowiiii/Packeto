package de.snowii.packeto.inject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public interface WrappedChannelInitializer {

    ChannelInitializer<Channel> original();
}
