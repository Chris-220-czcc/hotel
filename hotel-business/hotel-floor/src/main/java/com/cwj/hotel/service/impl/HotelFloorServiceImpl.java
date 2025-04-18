package com.cwj.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cwj.hotel.entity.HotelFloor;
import com.cwj.hotel.mapper.HotelFloorMapper;
import com.cwj.hotel.service.HotelFloorService;
import com.cwj.hotel.vo.MQFloorVo;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
@RocketMQMessageListener(consumerGroup = "hotelfloorgroup", topic = "hotelfloor")
public class HotelFloorServiceImpl extends ServiceImpl<HotelFloorMapper, HotelFloor> implements HotelFloorService, RocketMQListener<MQFloorVo> {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Override
    public void onMessage(MQFloorVo message) {
//        MessageHeaders header=message.getHeaders();
//        Long time= (Long) header.get("time");
//        System.out.println(time);
//        if (time<System.currentTimeMillis()){
//            System.out.println("取消订单");
//        }else {
//            System.out.println("重新回消息队列");
//        }

        HotelFloor hotelFloor = message.getHotelFloor();
        long time=message.getTime();
        if (time<System.currentTimeMillis()){
            System.out.println("取消订单");
        }else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            MQFloorVo mqFloorVo=new MQFloorVo(hotelFloor,time);
            Message<MQFloorVo> messageFloor= MessageBuilder.withPayload(mqFloorVo).build();
            rocketMQTemplate.syncSend("hotelfloor", messageFloor);
            System.out.println("还未到期");
        }

    }
}
