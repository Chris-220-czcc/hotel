package com.cwj.hotel.controller;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.function.Consumer;

public class Test {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer=new DefaultMQProducer("please_rename_unique_group_name");
        producer.setNamesrvAddr("192.168.237.129:9876");
        producer.start();
        Message message=new Message("please_rename_unique_group_name","TagA","Hello".getBytes());
        producer.send(message);
        producer.shutdown();


    }
}
