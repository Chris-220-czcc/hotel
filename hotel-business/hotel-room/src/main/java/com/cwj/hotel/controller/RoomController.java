package com.cwj.hotel.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cwj.hotel.entity.HotelFloor;
import com.cwj.hotel.entity.HotelRoom;
import com.cwj.hotel.entity.HotelRoomType;
import com.cwj.hotel.exception.BadRequestException;
import com.cwj.hotel.feign.FloorService;
import com.cwj.hotel.feign.RoomTypeService;
import com.cwj.hotel.service.RoomService;
import com.cwj.hotel.util.MinioUtil;
import com.cwj.hotel.utils.PageUtil;
import com.cwj.hotel.utils.Result;
import com.cwj.hotel.vo.FloorVo;
import com.cwj.hotel.vo.MQFloorVo;
import com.cwj.hotel.vo.RoomTypeVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import jakarta.annotation.Resource;
//import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;


import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Resource
    private RoomService roomService;
    @Resource
    private RoomTypeService roomTypeService;
    @Resource
    private FloorService floorService;
    @Resource
    private MinioClient minioClient;

    /*
        模糊+分页查询房间列表
     */
    @GetMapping("/list")
    public Result<Object> list(
            @RequestParam(value = "searchValue",defaultValue = "") String searchValue,
            @RequestParam(value = "pageSize",defaultValue = "") Long pageSize,
            @RequestParam(value = "pageIndex",defaultValue = "") Long pageIndex
    ) {
        QueryWrapper<HotelRoom> queryWrapper = new QueryWrapper<>();
        //like：如果condition成立，sql语句加上”column like %value%“
        queryWrapper.like(StringUtils.hasLength(searchValue),"room_name",searchValue);
        Page<HotelRoom> page=new Page<>(pageIndex,pageSize);
        roomService.page(page,queryWrapper);

        for(HotelRoom room:page.getRecords()){
            if (StringUtils.hasLength(room.getCoverImg())){
                room.setCoverImg(getRoomCoverImage(room.getCoverImg()));
            }
        }

        PageUtil<HotelRoom> rooms=new PageUtil<>(page.getCurrent(),page.getTotal(),page.getPages(),page.getRecords());
        return Result.ok().message("查询房间列表成功！").data(rooms);
    }

    /*
        添加房间
     */
    @PostMapping("/add")
    public Result<Object> add(@RequestBody HotelRoom hotelRoom) {
        if (Objects.isNull(hotelRoom)) return Result.fail().message("添加失败");
        boolean flag = roomService.save(hotelRoom);
        if (flag) return Result.ok().message("添加成功");
        else return Result.fail().message("添加失败");
    }
    //非即时上传
//    @PostMapping("/add1")
//    public Result<Object> add1(@RequestBody HotelRoom hotelRoom) {
//        if (Objects.isNull(hotelRoom)) return Result.fail().message("添加失败");
//        boolean flag = roomService.save(hotelRoom);
//        if (flag) return Result.ok().message("添加成功");
//        else return Result.fail().message("添加失败");
//    }

    /*
        更新房间图片
     */
    @PostMapping("/upload")
    public Result<Object> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) throw new BadRequestException("上传文件不能为空");
        String type = file.getContentType();
        if(!StringUtils.hasText(type)||!Arrays.asList("image/jpeg","image/png","image/gif").contains(type)){
            throw new BadRequestException("上传图片的格式必须为 jpg, png 和 gif");
        }
//        Boolean hotelIsExit = MinioUtil.bucketExists(minioClient, "hotel");
//        if (hotelIsExit) MinioUtil.makeBucket(minioClient, "hotel");
        String fileName = MinioUtil.upload(minioClient,"hotel",file);//上传minio
        return Result.ok().data(fileName);
    }

    /*
        查询房间类型
        利用feign远程调用接口
     */
    @GetMapping("/roomtype")
    public Result<Object> getRoomType() {
        List<RoomTypeVo> roomTypeVoList = roomTypeService.getRoomType();
        return Result.ok().data(roomTypeVoList);
    }

    /*
        查询楼层信息
        feign
     */
    @GetMapping("/floor")
    public Result<Object> getFloor() {
        List<FloorVo> floorVoList=floorService.all();
        return Result.ok().data(floorVoList);
    }

    /*
        查看图片
     */
    private String getRoomCoverImage(String coverImg) {
        System.out.println(MinioUtil.preview(minioClient, "hotel", coverImg));
        return MinioUtil.preview(minioClient, "hotel", coverImg);
    }

    //////////////////////////////
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @PostMapping("/rocketmq")
    public void RocketMQTest(){
        HotelFloor hotelFloor=new HotelFloor();
        hotelFloor.setFloorName("test-mq");
        hotelFloor.setFloorNo(1);
//        HotelRoomType roomType=new HotelRoomType();
//        roomType.setTypeName("test-mq");
//        roomType.setTypeSort(1);
        long time=System.currentTimeMillis()+5000;
        MQFloorVo mqFloorVo=new MQFloorVo(hotelFloor,time);
        Message<MQFloorVo> messageFloor= MessageBuilder.withPayload(mqFloorVo).build();
        rocketMQTemplate.syncSend("hotelfloor", messageFloor);
//        rocketMQTemplate.syncSend("roomtype", roomType);
    }
    @PostMapping("/norocketmq")
    public void NoRocketMQTest(){
        HotelFloor hotelFloor=new HotelFloor();
        hotelFloor.setFloorName("test-nomq");
        hotelFloor.setFloorNo(1);

        HotelRoomType roomType=new HotelRoomType();
        roomType.setTypeName("test-nomq");
        roomType.setTypeSort(1);
        System.out.println(hotelFloor);
        System.out.println(roomType);
        floorService.addFloor(hotelFloor);
        roomTypeService.addType(roomType);
    }
}
