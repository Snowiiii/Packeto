package de.snowii.packeto.channel.inbound;

import de.snowii.packeto.PacketoSpigotPlatform;
import de.snowii.packeto.channel.PacketDecoder;
import de.snowii.packeto.channel.PacketEncoder;
import de.snowii.packeto.inject.PipelineNames;
import de.snowii.packeto.inject.WrappedChannelInitializer;
import de.snowii.packeto.packet.user.SimplePacketUser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import java.lang.reflect.Method;

public class SpigotChannelInitializer extends ChannelInitializer<Channel> implements WrappedChannelInitializer {

    private static final Method INIT_CHANNEL_METHOD;
    private final ChannelInitializer<Channel> original;

    public SpigotChannelInitializer(ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
    }

    static {
        try {
            INIT_CHANNEL_METHOD = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
            INIT_CHANNEL_METHOD.setAccessible(true);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void initChannel(final Channel channel) throws Exception {
        INIT_CHANNEL_METHOD.invoke(this.original, channel);
        afterInitChannel(channel);
    }

    public static void afterInitChannel(final Channel channel) {
        try (var ignored = PacketoSpigotPlatform.getInstance().getTimingManager().ofStart("InitChannel")) {
            SimplePacketUser user = new SimplePacketUser(channel);

            // ConnectionUserManager.newUser(user);

            final ChannelPipeline pipeline = channel.pipeline();
            pipeline.addAfter(PipelineNames.MINECRAFT_ENCODER, PipelineNames.ENCODER_NAME, new PacketEncoder(user));
            pipeline.addAfter(PipelineNames.MINECRAFT_DECODER, PipelineNames.DECODER_NAME, new PacketDecoder(user));
        }
    }

    @Override
    public ChannelInitializer<Channel> original() {
        return original;
    }
}
