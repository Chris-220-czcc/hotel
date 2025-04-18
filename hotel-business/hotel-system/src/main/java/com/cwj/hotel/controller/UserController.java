package com.cwj.hotel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwj.hotel.entity.User;
import com.cwj.hotel.service.UserService;
import com.cwj.hotel.utils.HutoolJWTUtil;
import com.cwj.hotel.utils.Md5Util;
import com.cwj.hotel.utils.PageUtil;
import com.cwj.hotel.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/list")
    public Result<Object> list(
            @RequestParam(value = "pageIndex",defaultValue = "1") Long pageIndex,
            @RequestParam(value = "pageSize",defaultValue = "10") Long pageSize,
            @RequestParam(value = "status",defaultValue = "-1") int status,
            @RequestParam(value = "searchValue",defaultValue = "") String searchValue
    ) {
//        QueryWrapper<Object> query = Wrappers.query();
//        query.eq("username", searchValue);

        Page<User> page = new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(status !=-1,User::getStatus,status)//???箭头函数？new LambdaQueryWrapper？
                .and(StringUtils.hasLength(searchValue),
                        q->q.like(User::getRealName,searchValue)
                                .or().like(User::getUsername,searchValue)
                                .or().like(User::getSex,searchValue));
        userService.page(page,wrapper);
        PageUtil<User> pageUtil=new PageUtil<>(page.getCurrent(),page.getTotal(),page.getSize(),page.getRecords());
        return Result.ok().data(pageUtil).message("获取用户数据成功！");
    }

    @PostMapping("/add")
    public Result<Object> add(@RequestBody User user) {
        String username = user.getUsername();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user1 = userService.getOne(wrapper);
        if(user1!=null){
            return Result.fail().message("用户名已存在");
        }else {
            user.setPassword(Md5Util.Md5(user.getPassword()));
            userService.save(user);
            return Result.ok().message("添加用户成功");
        }
    }

    @GetMapping("/get/{id}")
    public Result<Object> get(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (Objects.isNull(user)){
            return Result.fail().message("未找到该用户");
        }else{
            return Result.ok().data(user).message("成功获取该用户信息");
        }
    }

    @PutMapping("/edit")
    public Result<Object> edit(@RequestBody User user) {
        user.setPassword(Md5Util.Md5(user.getPassword()));
        boolean flag = userService.updateById(user);
        if (flag) return Result.ok().message("修改成功");
        else return Result.fail().message("修改失败");
    }
    @DeleteMapping("/del/{id}")
    public Result<Object> del(@PathVariable("id") Long id) {
        boolean flag = userService.removeById(id);
        if (flag) return Result.ok().message("删除成功");
        else return Result.fail().message("删除失败");
    }

}
