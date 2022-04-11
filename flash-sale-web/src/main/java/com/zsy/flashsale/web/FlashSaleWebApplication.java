package com.zsy.flashsale.web;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Allenzsy
 * @Date 2022/1/27 1:02
 * @Description:
 */
@MapperScan("com.zsy.flashsale.*.mapper")
@SpringBootApplication(scanBasePackages = "com.zsy.flashsale")
public class FlashSaleWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlashSaleWebApplication.class);
    }

}
