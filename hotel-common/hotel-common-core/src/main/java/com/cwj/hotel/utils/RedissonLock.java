package com.cwj.hotel.utils;

import ch.qos.logback.core.net.server.Client;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonLock {
//    private static final String LOCK_KEY="RedissonLock";
//    private final RLock R_LOCK;
//    private RedissonClient redissonClient;
    public static RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }
}
