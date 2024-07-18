package org.caffinitas.ohc.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.checkerframework.checker.index.qual.NonNegative;


public interface EnrichCache<K,V> extends Cache<K,V> {

    @NonNegative
    long size();

}