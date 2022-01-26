package com.zsy.flashsale.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Allenzsy
 * @Date 2022/1/27 1:12
 * @Description:
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/test")
    public String test() {
        return "Hello...";
    }

}
