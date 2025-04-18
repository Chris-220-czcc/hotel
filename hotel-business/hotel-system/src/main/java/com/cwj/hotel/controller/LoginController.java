package com.cwj.hotel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cwj.hotel.entity.User;
import com.cwj.hotel.service.UserService;
import com.cwj.hotel.utils.HutoolJWTUtil;
import com.cwj.hotel.utils.Md5Util;
import com.cwj.hotel.utils.RedisUtil;
import com.cwj.hotel.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class LoginController {
    @Resource
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user) {
        
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        User user1 = userService.getOne(wrapper);
        if (Objects.isNull(user1)){
            return Result.fail().message("该用户不存在");
        }else{
            if (user1.getPassword().equals(Md5Util.Md5(user.getPassword()))){
                String accessToken= HutoolJWTUtil.createToken(user,15 * 60 * 1000);
                String refreshToken= HutoolJWTUtil.createToken(user,30 * 60 * 1000);
                redisUtil.set(user1.getId().toString(),refreshToken, 30 * 60 * 1000);
                Map<String,Object> map=new HashMap<>();
                map.put("userIcon",user1.getUserIcon());
                map.put("realName",user1.getRealName());
                map.put("sex",user1.getSex());
                map.put("accessToken",accessToken);
                map.put("refreshToken",refreshToken);
                return Result.ok().data(map).message("登录成功");
            }else{
                return Result.fail().message("账号或密码错误");
            }
        }
    }
}
