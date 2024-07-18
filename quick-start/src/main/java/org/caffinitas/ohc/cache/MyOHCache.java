package org.caffinitas.ohc.cache;

import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.caffinitas.ohc.CacheSerializer;
import org.caffinitas.ohc.HashAlgorithm;
import org.caffinitas.ohc.OHCache;
import org.caffinitas.ohc.OHCacheBuilder;
import org.caffinitas.ohc.config.ThreadPool;
import org.caffinitas.ohc.convertor.StringConvertor;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

/**
 * @author yindongjie
 */
public class MyOHCache<V> implements EnrichCache<String, V> {

    @Getter
    private final OHCache<String, V> cache;

    private final LongAdder hitCount = new LongAdder();
    private final LongAdder missCount = new LongAdder();

    public MyOHCache(CacheSerializer<String> keySerializer, CacheSerializer<V> valueSerializer, long capacity, long ttlMin) {
        cache = OHCacheBuilder.<String, V>newBuilder()
                .keySerializer(keySerializer)
                .valueSerializer(valueSerializer)
                .capacity(1024L * 1024 * capacity)
                .defaultTTLmillis(1000 * 60 * ttlMin)
                .segmentCount(128)
                .hashTableSize(32 * 1024)
                .timeouts(true)
                .hashMode(HashAlgorithm.CRC32C)
                .build();
    }

    @Override
    public @Nullable V getIfPresent(@NonNull Object key) {
        V v = cache.get((String) key);
        if (v != null) {
            hitCount.add(1);
        } else {
            missCount.add(1);
        }
        return v;
    }

    @Override
    public @Nullable V get(@NonNull String key, @NonNull Function<? super String, ? extends V> mappingFunction) {
        return getIfPresent(key);
    }


    @Override
    public @NonNull Map<@NonNull String, @NonNull V> getAllPresent(@NonNull Iterable<@NonNull ?> keys) {
        Map<String, V> result = Maps.newHashMap();
        for (Object key : keys) {
            V v = getIfPresent(key);
            if (v != null) {
                result.put((String) key, v);
            }
        }
        return result;
    }


    @Override
    public void put(@NonNull String key, @NonNull V value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(@NonNull Map<? extends @NonNull String, ? extends @NonNull V> map) {
        ThreadPool.Cache.execute(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<? extends @NonNull String, ? extends @NonNull V> entry : map.entrySet()) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    @Override
    public void invalidate(@NonNull Object key) {
        cache.remove((String) key);
    }

    @Override
    public void invalidateAll(@NonNull Iterable<@NonNull ?> keys) {
        Iterator<@NonNull ?> iterator = keys.iterator();
        if (iterator.hasNext()) {
            invalidate(iterator.next());
        }
    }

    @Override
    public void invalidateAll() {
        cache.clear();
    }

    @Override
    public @NonNegative long estimatedSize() {
        return cache.memUsed();
    }

    @Override
    public @NonNull CacheStats stats() {
        return new CacheStats(
                negativeToMaxValue(hitCount.sum()),
                negativeToMaxValue(missCount.sum()),
                0,
                0,
                0,
                0,
                0);
    }

    @Override
    public @NonNull ConcurrentMap<@NonNull String, @NonNull V> asMap() {
        return null;
    }

    @Override
    public void cleanUp() {
        cache.clear();
    }

    @Override
    public @NonNull Policy<String, V> policy() {
        return null;
    }

    private char[] convertKey(String key) {
        return StringConvertor.INSTANCE.serializer(key);
    }

    /**
     * Returns {@code value}, if non-negative. Otherwise, returns {@link Long#MAX_VALUE}.
     */
    private static long negativeToMaxValue(long value) {
        return (value >= 0) ? value : Long.MAX_VALUE;
    }

    @Override
    public @NonNegative long size() {
        return cache.size();
    }
}