package org.caffinitas.ohc.serializer;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;

public class StringSerializer implements CacheSerializer<String> {

    public static final StringSerializer INSTANCE = new StringSerializer();

    /**
     * 计算字符串序列化后占用的空间
     *
     * @param value 需要序列化存储的字符串
     * @return 序列化后的字节数
     */
    @Override
    public int serializedSize(String value) {
        return 4 + value.length() * 2;
    }

    /**
     * 将字符串对象序列化到 ByteBuffer 中，ByteBuffer是OHC管理的堆外内存区域的映射。
     *
     * @param value 需要序列化的对象
     * @param buf   序列化后的存储空间
     */
    @Override
    public void serialize(String value, ByteBuffer buf) {

        buf.putInt(value.length());

        for (int i = 0; i < value.length(); i++) {
            buf.putChar(value.charAt(i));
        }

    }

    /**
     * 对堆外缓存的字符串进行反序列化
     *
     * @param buf 字节数组所在的 ByteBuffer
     * @return 字符串对象.
     */
    @Override
    public String deserialize(ByteBuffer buf) {

        char[] b = new char[buf.getInt()];
        for (int i = 0; i < b.length; i++) {
            b[i] = buf.getChar(i);
        }
        return new String(b);
    }
}