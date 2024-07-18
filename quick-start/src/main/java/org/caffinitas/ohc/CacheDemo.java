package org.caffinitas.ohc;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.caffinitas.ohc.cache.MyOHCache;
import org.caffinitas.ohc.serializer.StringSerializer;


import java.time.Duration;

public class CacheDemo {
    public static void main(String[] args) {
        Cache<String, String> ohCache =  new MyOHCache<String>(StringSerializer.INSTANCE, new StringSerializer(), 5L, 5L);


        Cache<Object, Object> caffeineCache = Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(Duration.ofMinutes(5L))
                .recordStats().build();


        ohCache.put("a1","111111");
        ohCache.put("a2","111111");
        ohCache.put("a3","111111");


        caffeineCache.put("a1","111111");
        caffeineCache.put("a2","111111");
        caffeineCache.put("a3","111111");


        System.out.println(ohCache.getAllPresent(Lists.newArrayList("a1","a2","a3")));
        System.out.println(caffeineCache.getAllPresent(Lists.newArrayList("a1","a2","a3")));
    }
}