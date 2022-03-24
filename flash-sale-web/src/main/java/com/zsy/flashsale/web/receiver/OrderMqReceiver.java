package com.zsy.flashsale.web.receiver;

import com.alibaba.fastjson.JSONObject;
import com.zsy.flashsale.biz.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author Allenzsy
 * @Date 2022/3/24 23:47
 * @Description:
 */
@Slf4j
@Component
@RabbitListener(queues = "orderQueue") // 订阅消息队列
public class OrderMqReceiver {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void process(String message) {
        log.info("OrderMqReceiver收到消息开始用户下单流程: " + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        Integer pid = jsonObject.getInteger("id");
        orderService.createOrderByMQ(pid);
    }

}
