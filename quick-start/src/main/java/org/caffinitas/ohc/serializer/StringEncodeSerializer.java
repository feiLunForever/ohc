package org.caffinitas.ohc.serializer;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringEncodeSerializer implements CacheSerializer<String> {

    public static final StringEncodeSerializer INSTANCE = new StringEncodeSerializer();

    /**
     * 计算字符串序列化后占用的空间
     *
     * @param value 需要序列化存储的字符串
     * @return 序列化后的字节数
     */
    @Override
    public int serializedSize(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        return 4 + bytes.length;
    }

    /**
     * 将字符串对象序列化到 ByteBuffer 中，ByteBuffer是OHC管理的堆外内存区域的映射。
     *
     * @param value 需要序列化的对象
     * @param buf   序列化后的存储空间
     */
    @Override
    public void serialize(String value, ByteBuffer buf) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        buf.putInt(bytes.length);
        buf.put(bytes);

    }

    /**
     * 对堆外缓存的字符串进行反序列化
     *
     * @param buf 字节数组所在的 ByteBuffer
     * @return 字符串对象.
     */
    @Override
    public String deserialize(ByteBuffer buf) {

        byte[] bytes = new byte[buf.getInt()];
        buf.get(bytes);
        return new String(bytes);
    }
}