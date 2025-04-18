package com.cwj.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwj.hotel.entity.HotelRoomType;
import com.cwj.hotel.mapper.RoomTypeMapper;
import com.cwj.hotel.service.RoomTypeService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RocketMQMessageListener(topic = "roomtype",consumerGroup = "roomtypegroup")
public class RoomTypeServiceImpl extends ServiceImpl<RoomTypeMapper, HotelRoomType> implements RoomTypeService, RocketMQListener<HotelRoomType> {
    @Transactional
    public void test(){
        System.out.println("hello");
    }

    @Override
    public void onMessage(HotelRoomType hotelRoomType) {
        save(hotelRoomType);
    }
}
