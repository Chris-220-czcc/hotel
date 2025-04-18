package com.cwj.hotel.controller;

import cn.hutool.jwt.JWTUtil;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwj.hotel.entity.HotelRoomType;
import com.cwj.hotel.entity.Student;
import com.cwj.hotel.service.RoomTypeService;
import com.cwj.hotel.utils.HutoolJWTUtil;
import com.cwj.hotel.utils.PageUtil;
import com.cwj.hotel.utils.RedisUtil;
import com.cwj.hotel.utils.Result;
import com.cwj.hotel.vo.RoomTypeVo;
import com.cwj.hotel.vo.RoomTypeWithTokenVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;
import io.swagger.v3.core.util.Json;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/roomtype")
public class RoomTypeController{
    @Resource
    private RoomTypeService roomTypeService;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private RedisUtil redisUtil;

//经过redis
    @GetMapping("/list")
    public Result<Object> list(@RequestParam(value = "pageSize",defaultValue = "10") Long pageSize,
                               @RequestParam(value = "pageIndex",defaultValue = "1") Long pageIndex,
                               @RequestParam(value = "searchValue",defaultValue = "") String searchValue) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        String hotelRoomTypeListStr = redisTemplate.opsForValue().get("hotelRoomTypeList");
        List<HotelRoomType> hotelRoomTypeArrayList = objectMapper.readValue(hotelRoomTypeListStr, new TypeReference<List<HotelRoomType>>() {
        });
        PageUtil<HotelRoomType> roomTypePageUtil=new PageUtil<>(pageIndex, 10L,pageSize,hotelRoomTypeArrayList);
        return Result.ok().data(roomTypePageUtil).message("查询房间类型列表成功");
//        Page<HotelRoomType> page=new Page<>(pageIndex,pageSize);
//        QueryWrapper<HotelRoomType> queryWrapper=new QueryWrapper<>();
//        queryWrapper.like(StringUtils.hasLength(searchValue),"type_name",searchValue);
//        queryWrapper.orderByDesc("id");
//        roomTypeService.page(page,queryWrapper);
//        PageUtil<HotelRoomType> pageUtil=new PageUtil<>(page.getCurrent(),page.getTotal(),page.getSize(),page.getRecords());
//        return Result.ok().data(pageUtil).message("查询房间类型列表成功");
    }

    @GetMapping("/list2")
    public Result<Object> list2(@RequestParam(value = "pageSize",defaultValue = "10") Long pageSize,
                               @RequestParam(value = "pageIndex",defaultValue = "1") Long pageIndex,
                               @RequestParam(value = "searchValue",defaultValue = "") String searchValue) throws JsonProcessingException {
//        ObjectMapper objectMapper=new ObjectMapper();
//        String hotelRoomTypeListStr = redisTemplate.opsForValue().get("hotelRoomTypeList");
//        List<HotelRoomType> hotelRoomTypeArrayList = objectMapper.readValue(hotelRoomTypeListStr, new TypeReference<List<HotelRoomType>>() {
//        });
//        PageUtil<HotelRoomType> roomTypePageUtil=new PageUtil<>(pageIndex, (long) hotelRoomTypeArrayList.size(),pageSize,hotelRoomTypeArrayList);
//        return Result.ok().data(roomTypePageUtil).message("查询房间类型列表成功");
        Page<HotelRoomType> page=new Page<>(pageIndex,pageSize);
        QueryWrapper<HotelRoomType> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(searchValue),"type_name",searchValue);
        queryWrapper.orderByDesc("id");
        roomTypeService.page(page,queryWrapper);
        PageUtil<HotelRoomType> pageUtil=new PageUtil<>(page.getCurrent(),page.getTotal(),page.getSize(),page.getRecords());
        return Result.ok().data(pageUtil).message("查询房间类型列表成功");
    }
    @PostMapping("/add")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public Result<Object> add(@RequestBody RoomTypeWithTokenVo roomTypeWithTokenVo) throws InterruptedException, IOException {
        Thread.sleep(500);

//        System.out.println(roomTypeWithTokenVo);
        HotelRoomType hotelRoomType=roomTypeWithTokenVo.getHotelRoomType();
        //浏览器的token
        String token=roomTypeWithTokenVo.getToken();
//        System.out.println(token);
        //从session中取token
        String toke= (String) redisUtil.get("tableToken");
//        System.out.println(toke);
        if (redisUtil.get("tableToken")!=null){
            toke= (String) redisUtil.get("tableToken");
        }else {
            return Result.fail().message("重复提交表单");
        }
        if(token==null||!token.equals(toke)){
            return Result.fail().message("重复提交表单");
        }else {
            redisUtil.del("tableToken");
        }

        String typeNameForm=hotelRoomType.getTypeName();
        QueryWrapper<HotelRoomType> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("type_name",typeNameForm);
        boolean exists = roomTypeService.exists(queryWrapper);
        if(exists){
            return Result.fail().message("该类型已存在");
        }else {
            roomTypeService.save(hotelRoomType);
//            ObjectMapper objectMapper=new ObjectMapper();
//            Gson gson=new Gson();
//            List<HotelRoomType> list = roomTypeService.list();
////            String json1 = gson.toJson(list);
//            String json1 = objectMapper.writeValueAsString(list);
//            //把list放入redis
//            redisUtil.set("hotelRoomTypeList", json1);
//            //获取listr
//            Object hotelRoomTypeListObj= redisUtil.get("hotelRoomTypeList");
//            //object转成json
//            String json = gson.toJson(hotelRoomTypeListObj);
//            System.out.println("------------------------Json-----------------------");
//            System.out.println(json);
//            //json转List
//            List<HotelRoomType> hotelRoomTypeList= objectMapper.readValue(json, new TypeReference<List<HotelRoomType>>() {});


            ObjectMapper objectMapper=new ObjectMapper();
            List<HotelRoomType> list = roomTypeService.list();
            String json = objectMapper.writeValueAsString(list);
            redisTemplate.opsForValue().set("hotelRoomTypeList",json);
            String hotelRoomTypeListStr = redisTemplate.opsForValue().get("hotelRoomTypeList");
            List<HotelRoomType> hotelRoomTypeArrayList = objectMapper.readValue(hotelRoomTypeListStr, new TypeReference<List<HotelRoomType>>() {
            });

            System.out.println("---------------redis----------------");
            System.out.println(hotelRoomTypeArrayList);
            return Result.ok().message("成功添加"+typeNameForm+"类型");
        }
    }

    @GetMapping("/get/{id}")
    public Result<Object> get(@PathVariable("id") Long id) {
        HotelRoomType roomType = roomTypeService.getById(id);
        if (roomType == null) {
            return Result.fail().message("未找到该房间类型信息");
        }else {
            return Result.ok().message("成功获取该房间类型信息").data(roomType);
        }

    }

    @PutMapping("/edit")
    public Result<Object> edit(@RequestBody HotelRoomType hotelRoomType) {
        boolean flag = roomTypeService.updateById(hotelRoomType);
        if(flag){
            return Result.ok().message("修改成功！");
        }else {
            return Result.fail().message("修改失败！");
        }
    }

    @GetMapping("/all")
    public List<RoomTypeVo> getRoomType() {
        List<HotelRoomType> list = roomTypeService.list();
        List<RoomTypeVo> roomTypeVos = new ArrayList<>();
        for (HotelRoomType hotelRoomType : list) {
            roomTypeVos.add(new RoomTypeVo(hotelRoomType.getId(),hotelRoomType.getTypeName()));
        }
        return roomTypeVos;
    }

//    @GetMapping("/token")
//    public Result<Object> token(HttpSession session) {
//        String token= HutoolJWTUtil.createToken();
//        redisUtil.set("tableToken",token);
//        return Result.ok().message("获取表单session成功").data(token);
//    }
    //test
    @PutMapping("/addtype")
    public void addType(@RequestBody HotelRoomType hotelRoomType){
        roomTypeService.save(hotelRoomType);
    }

}
