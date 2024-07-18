package org.caffinitas.ohc.serializer;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;

public class BooleanSerializer implements CacheSerializer<Boolean> {

    public static final BooleanSerializer INSTANCE = new BooleanSerializer();

    @Override
    public void serialize(Boolean value, ByteBuffer buf) {

        byte st = (byte) (value ? 1 : 0);
//        buf.putInt(1);
        buf.put(st);
    }

    @Override
    public Boolean deserialize(ByteBuffer buf) {
//        byte[] b = new byte[buf.getInt()];

        return buf.get() == 1;
    }

    @Override
    public int serializedSize(Boolean aBoolean) {
        return 1;
    }

}