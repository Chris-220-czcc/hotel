package com.cwj.hotel.utils;

import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    @Getter
    public static ConcurrentHashMap<String,Object> cache = new ConcurrentHashMap<>();
    public static void put(String key, Object value) {
        cache.put(key, value);
    }
    public static Object get(String key) {
        return cache.get(key);
    }
    public static void remove(String key) {
        cache.remove(key);
    }
    public static void clear() {
        cache.clear();
    }

}
