package de.snowii.packeto.channel;

import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.util.PipelineUtil;
import de.snowii.packeto.PacketoSpigotPlatform;
import de.snowii.packeto.api.listener.SpigotPacketEvent;
import de.snowii.packeto.inject.PaperInjector;
import de.snowii.packeto.inject.PipelineNames;
import de.snowii.packeto.packet.PacketDirection;
import de.snowii.packeto.packet.user.SimplePacketUser;
import de.snowii.packeto.util.softdepend.viaversion.ViaVersionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@ChannelHandler.Sharable
// Client -> Server
public final class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    private SimplePacketUser simplePacketUser;

    public PacketDecoder(SimplePacketUser simplePacketUser) {
        this.simplePacketUser = simplePacketUser;
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        if (msg.isReadable()) {
            try (final var ignored = PacketoSpigotPlatform.getInstance().getTimingManager().ofStart("PacketDecoder")) {
                if (PacketoSpigotPlatform.getInstance().getListenerManager().callEventNormal(new SpigotPacketEvent(PacketDirection.CLIENT, simplePacketUser, msg))) {
                    msg.clear();
                }
                msg.resetReaderIndex();
                out.add(msg.retain());
                PacketoSpigotPlatform.getInstance().getListenerManager().callEventReadonly(new SpigotPacketEvent(PacketDirection.CLIENT, simplePacketUser, msg));
            }
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (ViaVersionUtil.isAvailable() && PipelineUtil.containsCause(cause, CancelCodecException.class)) {
            return;
        }
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object event) throws Exception {
        if (PaperInjector.COMPRESSION_ENABLED_EVENT == null || event != PaperInjector.COMPRESSION_ENABLED_EVENT) {
            super.userEventTriggered(ctx, event);
            return;
        }

        final ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addAfter(PipelineNames.MINECRAFT_COMPRESSOR, PipelineNames.ENCODER_NAME, pipeline.remove(PipelineNames.ENCODER_NAME));
        pipeline.addAfter(PipelineNames.MINECRAFT_DECOMPRESSOR, PipelineNames.DECODER_NAME, pipeline.remove(PipelineNames.DECODER_NAME));
        super.userEventTriggered(ctx, event);
    }
}
