package com.cwj.hotel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwj.hotel.entity.Role;
import com.cwj.hotel.exception.BadRequestException;
import com.cwj.hotel.service.RoleService;
import com.cwj.hotel.utils.PageUtil;
import com.cwj.hotel.utils.RedissonLock;
import com.cwj.hotel.utils.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/role")
@Tag(name="系统角色控制器", description = "对角色进行增删改查")
public class RoleController {
    @Resource
    private RoleService roleService;
    @GetMapping("/list")
    public Result<Object> list(
            @RequestParam(value = "pageIndex",defaultValue = "1") Long pageIndex,
            @RequestParam(value = "pageSize",defaultValue = "10") Long pageSize,
            @RequestParam(value ="searchValue",defaultValue = "") String searchValue) {
        //1.创建page对象，给page：当前页和每页数据条数
        //2.创建wrapper对象，把分页条件放入wrapper对象中
        //3.用mybatisplus中的service方法，用wrapper筛选的条件，查找并赋值给page,其中包括给page：总记录数，总页数；
        //4.把page中的各个属性重新赋值给PageUtil-->？:为啥要重新赋值，为啥不直接return page?Dto?
        Page<Role> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(searchValue), Role::getName, searchValue);
        roleService.page(page, queryWrapper);
        PageUtil<Role> pageUtil = new PageUtil<>(page.getCurrent(), page.getTotal(), page.getSize(), page.getRecords());
        System.out.println(page.getTotal());
        return Result.ok().data(pageUtil);
    }
    @PostMapping("/add")
    public Result<Object> add(@RequestBody @Validated Role role) {
        boolean flag=roleService.save(role);
        if(flag) return Result.ok().message("添加role成功");
        return Result.fail().message("添加role失败");
    }
    @GetMapping("/get/{id}")
    public Result<Object> get(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) throw new BadRequestException("获取role信息失败");
        Role role = roleService.getById(id);
        return Result.ok().data(role).message("查找role信息成功");
    }
    @PutMapping("/edit")
    public Result<Object> edit(@RequestBody @Validated Role role) {
        boolean flag=roleService.updateById(role);
        if(flag) return Result.ok().message("修改role成功");
        return Result.fail().message("修改role失败");
    }
    @DeleteMapping("/del/{id}")
    public Result<Object> del(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) throw new BadRequestException("删除该role失败");
        roleService.removeById(id);
        return Result.ok().message("删除role成功");
    }
    @PutMapping("/test/{id}")
    public Long test(@PathVariable("id") Long id) throws InterruptedException {
        RedissonClient redissonClient=RedissonLock.getRedissonClient();
        RLock rlock=redissonClient.getLock("Add_Age");
        boolean isLocked=false;
        while (!isLocked){
            isLocked = rlock.tryLock(10, 30, java.util.concurrent.TimeUnit.SECONDS);
            if (isLocked) {
                try {
                    // 获得锁后执行的业务逻辑​
                    System.out.println("获得锁，执行任务...");
                    Role role = roleService.getById(id);
                    int age=role.getAge()+1;
                    role.setAge(age);
                    roleService.updateById(role);
                } finally {
                    // 释放锁​
                    rlock.unlock();
                    System.out.println("释放锁");
                }
            } else {
                System.out.println("未能获得锁");
            }
        }
        return id;
    }
}
