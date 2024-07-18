package org.caffinitas.ohc.serializer;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;

public class BytesSerializer implements CacheSerializer<byte[]> {

    public static final BytesSerializer INSTANCE = new BytesSerializer();

    @Override
    public void serialize(byte[] value, ByteBuffer buf) {
        buf.putInt(value.length);
        buf.put(value);
    }

    @Override
    public byte[] deserialize(ByteBuffer buf) {
        byte[] b = new byte[buf.getInt()];
        buf.get(b);
        return b;
    }

    @Override
    public int serializedSize(byte[] value) {
        return 4 + value.length;
    }
}