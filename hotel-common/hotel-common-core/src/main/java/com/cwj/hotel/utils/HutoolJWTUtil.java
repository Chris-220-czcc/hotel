package com.cwj.hotel.utils;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.cwj.hotel.entity.User;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HutoolJWTUtil {
    private static final String SECRET_KEY = "mySecretKey"; // 替换为你自己的密钥
//    private static final JWTSigner SIGNER = JWTSignerUtil.hs256(SECRET_KEY.getBytes());
    /**
     * 生成系统用户token
     * @param user 用户对象
     * @return 返回生成的token字符串
     */
//    public static String createToken(User user){
//        DateTime now = DateTime.now();
//        DateTime newTime = now.offsetNew(DateField.MINUTE,120);
//        Map<String,Object> payload = new HashMap<String,Object>();
//        // 签发时间
//        payload.put(JWTPayload.ISSUED_AT, now);
//        // 过期时间
//        payload.put(JWTPayload.EXPIRES_AT, newTime);
//        // 生效时间
//        payload.put(JWTPayload.NOT_BEFORE, now);
//        // 载荷
//        payload.put("username", user.getUsername());
//        payload.put("userId", user.getId());
//        return JWTUtil.createToken(payload,SECRET_KEY.getBytes());
//    }
    public static String createToken(User user, int time){
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE,time);
        Map<String,Object> payload = new HashMap<String,Object>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 载荷
        payload.put("userId", user.getId());
        return JWTUtil.createToken(payload,SECRET_KEY.getBytes());
    }
//    public static String createToken(){
//        DateTime now = DateTime.now();
//        DateTime newTime = now.offsetNew(DateField.MINUTE,120);
//        Map<String,Object> payload = new HashMap<String,Object>();
//        // 签发时间
//        payload.put(JWTPayload.ISSUED_AT, now);
//        // 过期时间
//        payload.put(JWTPayload.EXPIRES_AT, newTime);
//        // 生效时间
//        payload.put(JWTPayload.NOT_BEFORE, now);
//        // 载荷
//        String key = "cwj.com"; // 盐
//        return JWTUtil.createToken(payload,key.getBytes());
//    }
    //生成refreshToken，并存入redis中
    public static void createReFreshToken(User user, int time){
        String token = createToken(user, time);
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.set(user.getId().toString(),token,time);
    }

    //验证token是否合法以及过期
    public static boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            // 验证签名
            boolean isSignatureValid = JWTUtil.verify(token, SECRET_KEY.getBytes());
            if (!isSignatureValid) {
                return false;
            }
            // 解析 JWT
            JWT jwt = JWTUtil.parseToken(token);
            // 检查过期时间
            DateTime expiration = (DateTime) jwt.getPayload(JWTPayload.EXPIRES_AT);
            return expiration == null || !expiration.before(new DateTime());
        } catch (Exception e) {
            return false;
        }
    }
    //验证refreshToken是否有效
    public static boolean validateRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        String userId = parseToken(token);
        RedisUtil redisUtil = new RedisUtil();
        String redisToken = (String) redisUtil.get(userId);
        return redisToken != null && redisToken.equals(token);
    }

    //从token中返回用户编号
    public static String parseToken(String token){
        final JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayload("userId").toString();
    }

}
