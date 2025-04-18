package com.cwj.hotel.utils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    //是否有key
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            return false;
        }
    }
    public  void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }
    //根据key获取value
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

//    public String getString(String key) {
//        return key == null ? null : (String) redisTemplate.opsForValue().get(key);
//    }
    //放入key-value
    public boolean set(String key, Object value, long time) {
        try {
            redisTemplate.opsForValue().set(key, value,time, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {

            return false;
        }
    }
}
