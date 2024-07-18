package org.caffinitas.ohc.convertor;

public interface SerializerConvertor<K, V> {
    V serializer(K key);

    K deserialize(V value);

}