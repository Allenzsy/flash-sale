package com.zsy.flashsale.web.Controller;

import com.zsy.flashsale.biz.service.StockCasService;
import com.zsy.flashsale.dao.po.StockCasDo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/test")
    public String test() {
        System.out.println("test...");
        return "Hello...";
    }

    @RequestMapping("/sale")
    public String sale() {
        StockCasDo stockCasDo = new StockCasDo();
        stockCasDo.setId(1);
        stockCasService.saleStock(stockCasDo);
        return "success sale...";
    }

}
