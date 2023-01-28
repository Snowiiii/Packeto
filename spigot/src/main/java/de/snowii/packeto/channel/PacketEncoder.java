package de.snowii.packeto.channel;

import com.viaversion.viaversion.exception.CancelCodecException;
import de.snowii.packeto.PacketoSpigotPlatform;
import de.snowii.packeto.api.listener.SpigotPacketEvent;
import de.snowii.packeto.inject.PaperInjector;
import de.snowii.packeto.inject.PipelineNames;
import de.snowii.packeto.packet.PacketDirection;
import de.snowii.packeto.packet.user.SimplePacketUser;
import de.snowii.packeto.util.PipelineUtil;
import de.snowii.packeto.util.softdepend.viaversion.ViaVersionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ChannelHandler.Sharable
// Server -> Client
public final class PacketEncoder extends MessageToMessageEncoder<ByteBuf> {
    private boolean handledCompression = PaperInjector.COMPRESSION_ENABLED_EVENT != null;

    private SimplePacketUser simplePacketUser;

    public PacketEncoder(SimplePacketUser simplePacketUser) {
        this.simplePacketUser = simplePacketUser;
    }


    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        final boolean needsRecompression = !handledCompression && handleCompressionOrder(ctx, msg);

        if (msg.isReadable()) {
            try (final var ignored = PacketoSpigotPlatform.getTimingManager().ofStart("PacketEncoder")) {
                final int startReadIndex = msg.readerIndex();
                if (PacketoSpigotPlatform.getInstance().getListenerManager().callEventNormal(new SpigotPacketEvent(PacketDirection.SERVER, simplePacketUser, msg))) {
                    msg.clear();
                }
                msg.readerIndex(startReadIndex);
            }
        }

        if (needsRecompression) {
            recompress(ctx, msg);
        }
        out.add(msg.retain());
        PacketoSpigotPlatform.getInstance().getListenerManager().callEventReadonly(new SpigotPacketEvent(PacketDirection.SERVER, simplePacketUser, msg));
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (ViaVersionUtil.isAvailable() && PipelineUtil.containsCause(cause, CancelCodecException.class)) {
            return;
        }
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }


    @SuppressWarnings("unchecked")
    private void recompress(final ChannelHandlerContext ctx, final ByteBuf buf) throws InvocationTargetException {
        final ByteBuf compressed = ctx.alloc().buffer();
        try {
            PipelineUtil.callEncode((MessageToByteEncoder<ByteBuf>) ctx.pipeline().get(PipelineNames.MINECRAFT_COMPRESSOR), ctx, buf, compressed);
            buf.clear().writeBytes(compressed);
        } finally {
            compressed.release();
        }
    }

    private boolean handleCompressionOrder(final ChannelHandlerContext ctx, final ByteBuf buf) throws Exception {
        final ChannelPipeline pipeline = ctx.pipeline();
        final List<String> names = pipeline.names();
        final int compressorIndex = names.indexOf(PipelineNames.MINECRAFT_COMPRESSOR);
        if (compressorIndex == -1) {
            return false;
        }

        handledCompression = true;
        if (compressorIndex > names.indexOf(PipelineNames.ENCODER_NAME)) {
            final ByteBuf decompressed = (ByteBuf) PipelineUtil.callDecode((ByteToMessageDecoder) pipeline.get(PipelineNames.MINECRAFT_DECOMPRESSOR), ctx, buf).get(0);
            try {
                buf.clear().writeBytes(decompressed);
            } finally {
                decompressed.release();
            }

            pipeline.addAfter(PipelineNames.MINECRAFT_COMPRESSOR, PipelineNames.ENCODER_NAME, pipeline.remove(PipelineNames.ENCODER_NAME));
            pipeline.addAfter(PipelineNames.MINECRAFT_DECODER, PipelineNames.DECODER_NAME, pipeline.remove(PipelineNames.DECODER_NAME));
            return true;
        }
        return false;
    }

    public SimplePacketUser getSimplePacketUser() {
        return simplePacketUser;
    }
}
