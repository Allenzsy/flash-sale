package com.zsy.flashsale.web.Controller;

import com.zsy.flashsale.biz.service.StockService;
import com.zsy.flashsale.biz.service.StockOrderService;
import com.zsy.flashsale.dao.po.StockDo;
import com.zsy.flashsale.dao.po.StockOrderDo;
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
    StockService stockService;
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
            stockService.saleStockUnsafe(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "sale out";
        }
        return "sale success...";
    }

    @RequestMapping("/orderV1/{id}/{method}")
    public String orderV1(@PathVariable Integer id,
                        @PathVariable String method) {
        int res = 0;
        try {
            if("unsafe".equals(method)) {
                res = stockOrderService.createOrderUnsafe(id);
            } else if("xlock".equals(method)) {
                res = stockOrderService.createOrderXLock(id);
            } else if("caslock".equals(method)) {
                res = stockOrderService.createOrderCasLock(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return method + res;
    }


    @RequestMapping("/orderV2/{method}/{id}")
    public String orderV2(@PathVariable Integer id,
                          @PathVariable String method) {
        int res = 0;
        try {
            if("unsafe".equals(method)) {
                StockDo stock = stockService.getStock(id);
                if (stock == null || stock.getCount() <= 0)
                    return "已售罄，商品"+stock.getName()+"库存为0";
                res = stockOrderService.createOrderUnsafe(stock);

            } else if("caslock".equals(method)) {
                StockDo stock = stockService.getStock(id);
                if (stock == null || stock.getCount() <= 0)
                    return "已售罄，商品"+stock.getName()+"库存为0";
                res = stockOrderService.createOrderCasLock(stock);

            } else if("xlock".equals(method)) {
                res = stockOrderService.createOrderByXLock(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(res == 0)
            return "使用" + method + "下单失败";
        return "使用" + method + "下单成功";

    }


}
