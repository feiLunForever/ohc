package org.caffinitas.ohc.serializer;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;

public class CharSerializer implements CacheSerializer<char[]> {

    public static final CharSerializer INSTANCE = new CharSerializer();

    @Override
    public void serialize(char[] value, ByteBuffer buf) {
        buf.putInt(value.length);
        for (char c : value) {
            buf.putChar(c);
        }
    }

    @Override
    public char[] deserialize(ByteBuffer buf) {
        char[] b = new char[buf.getInt()];
        for (int i = 0; i < b.length; i++) {
            b[i] = buf.getChar(i);
        }
        return b;
    }

    @Override
    public int serializedSize(char[] value) {
        return 4 + value.length * 2;
    }
}