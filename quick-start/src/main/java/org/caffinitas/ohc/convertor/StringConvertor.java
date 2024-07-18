package org.caffinitas.ohc.convertor;

public class StringConvertor implements SerializerConvertor<String, char[]> {

    public static final StringConvertor INSTANCE = new StringConvertor();

    @Override
    public char[] serializer(String value) {
        return value.toCharArray();
    }

    @Override
    public String deserialize(char[] value) {
        return new String(value);
    }

}