package com.cwj.hotel.listener;


import com.cwj.hotel.entity.HotelReserve;
import com.cwj.hotel.service.ReserveService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
@Component
@RocketMQMessageListener(topic = "reserve-add",consumerGroup = "reserve-add")
public class ReserveListener implements RocketMQListener<Map<String,String>> {
    @Resource
    private ReserveService reserveService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

//    @Override
//    public void onMessage(String s) {
//        reserveService.removeByOrderNumber(s);
//        System.out.println("订单"+s+"取消");
//    }

    @Override
    public void onMessage(Map map) {
        long delayTime=Long.parseLong(map.get("delayTime").toString());
        String orderNumber= (String) map.get("orderNumber");
        if (delayTime<System.currentTimeMillis()){
            System.out.println("订单"+orderNumber+"超时，取消订单");
            reserveService.removeByOrderNumber(String.valueOf(orderNumber));
        }else {
            try {
                Thread.sleep(1000);
                System.out.println("订单"+orderNumber+"未超时");
                rocketMQTemplate.convertAndSend("reserve-add", map);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
