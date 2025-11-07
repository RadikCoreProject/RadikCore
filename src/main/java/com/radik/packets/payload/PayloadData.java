package com.radik.packets.payload;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public final class PayloadData {
    public static void writeBoundedString(ByteBuf buf, String value, int maxChars, int maxBytes) {
        if (value == null) value = "";
        if (value.length() > maxChars) {
            throw new IllegalArgumentException("LIMIT EXCEED: " + maxChars + " > " + value.length());
        }

        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > maxBytes) {
            throw new IllegalArgumentException("LIMIT EXCEED: " + maxBytes + " > " + bytes.length);
        }
        buf.writeByte(bytes.length);
        buf.writeBytes(bytes);
    }

    public static @NotNull String readBoundedString(@NotNull ByteBuf buf, int maxChars, int maxBytes) {
        int length = buf.readByte() & 0xFF;
        if (length > maxBytes) {
            throw new DecoderException("LIMIT EXCEED: " + maxBytes);
        }

        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        String result = new String(bytes, StandardCharsets.UTF_8);
        if (result.length() > maxChars) {
            throw new DecoderException("LIMIT EXCEED: " + maxChars);
        }

        return result;
    }
}
