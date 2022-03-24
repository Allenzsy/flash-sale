package com.zsy.flashsale.web.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Allenzsy
 * @Date 2022/3/24 23:27
 * @Description: rabbitMq配置
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue delCacheQueue() {
        return new Queue("delCache");
    }

    @Bean
    public Queue orderQueue() {
        return new Queue("orderQueue");
    }

}
