package org.caffinitas.ohc.serializer;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;

public class MessageSerializer<T extends GeneratedMessageV3> implements CacheSerializer<T> {

    private Parser<T> parser;

    public MessageSerializer(Parser<T> parser) {
        this.parser = parser;
    }

    @Override
    public void serialize(T t, ByteBuffer buf) {
        buf.putInt(t.getSerializedSize());
        buf.put(t.toByteArray());
    }

    @Override
    public T deserialize(ByteBuffer buf) {
        byte[] bytes = new byte[buf.getInt()];
        // 读取字节数组
        buf.get(bytes);
        try {
            return parser.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int serializedSize(T t) {
        return t.getSerializedSize() + 4;
    }
}