package de.snowii.packeto.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PipelineUtil {
    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;
    private static Method MTM_DECODE;

    private static boolean hasInit;

    public static void init() {
        if (hasInit) throw new IllegalStateException("Tried to Init Pipeline Util twice");
        try {
            DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
            DECODE_METHOD.setAccessible(true);
            ENCODE_METHOD = MessageToByteEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
            ENCODE_METHOD.setAccessible(true);
            MTM_DECODE = MessageToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, Object.class, List.class);
            MTM_DECODE.setAccessible(true);
            hasInit = true;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Object> callDecode(final ByteToMessageDecoder decoder, final ChannelHandlerContext ctx, final Object input) throws InvocationTargetException {
        if (!hasInit) throw new IllegalStateException("Tried to Use Pipeline Util without Init it");
        List<Object> output = new ArrayList<>();
        try {
            PipelineUtil.DECODE_METHOD.invoke(decoder, ctx, input, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void callEncode(final MessageToByteEncoder<ByteBuf> encoder, final ChannelHandlerContext ctx, final Object msg, final ByteBuf output) throws InvocationTargetException {
        if (!hasInit) throw new IllegalStateException("Tried to Use Pipeline Util without Init it");
        try {
            PipelineUtil.ENCODE_METHOD.invoke(encoder, ctx, msg, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> callDecode(final MessageToMessageDecoder<ByteBuf> decoder, final ChannelHandlerContext ctx, final Object msg) throws InvocationTargetException {
        if (!hasInit) throw new IllegalStateException("Tried to Use Pipeline Util without Init it");
        final List<Object> output = new ArrayList<>();
        try {
            MTM_DECODE.invoke(decoder, ctx, msg, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static boolean containsCause(Throwable t, final Class<?> c) {
        while (t != null) {
            if (c.isAssignableFrom(t.getClass())) {
                return true;
            }

            t = t.getCause();
        }
        return false;
    }

    public static <T> @Nullable T getCause(Throwable t, Class<T> c) {
        while (t != null) {
            if (c.isAssignableFrom(t.getClass())) {
                //noinspection unchecked
                return (T) t;
            }

            t = t.getCause();
        }
        return null;
    }

    public static ChannelHandlerContext getContextBefore(String name, ChannelPipeline pipeline) {
        boolean mark = false;
        for (String s : pipeline.names()) {
            if (mark) {
                return pipeline.context(pipeline.get(s));
            }
            if (s.equalsIgnoreCase(name))
                mark = true;
        }
        return null;
    }

    public static ChannelHandlerContext getPreviousContext(String name, ChannelPipeline pipeline) {
        String previous = null;
        for (String entry : pipeline.toMap().keySet()) {
            if (entry.equals(name)) {
                return pipeline.context(previous);
            }
            previous = entry;
        }
        return null;
    }


}
