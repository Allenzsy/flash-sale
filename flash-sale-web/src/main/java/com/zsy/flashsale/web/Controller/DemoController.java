package com.zsy.flashsale.web.Controller;

import com.zsy.flashsale.biz.service.StockCasService;
import com.zsy.flashsale.biz.service.StockOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    StockCasService stockCasService;
    @Autowired
    StockOrderService stockOrderService;


    @RequestMapping("/test")
    public String test() {
        System.out.println("test...");
        return "Hello...";
    }

    @RequestMapping("/sale/{id}")
    public String sale(@PathVariable Integer id) {
        try {
            stockCasService.saleStock(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "sale out";
        }
        return "sale success...";
    }

    @RequestMapping("/order/{id}")
    public String order(@PathVariable Integer id) {

        int res = 0;
        try {
            res = stockOrderService.createOrderUnsafe(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "sale out";
        }
        return "order " + res + " success...";
    }

}
