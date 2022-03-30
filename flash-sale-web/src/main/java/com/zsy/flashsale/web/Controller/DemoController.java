package com.zsy.flashsale.web.Controller;

import com.zsy.flashsale.biz.service.ProductService;
import com.zsy.flashsale.biz.service.OrderService;
import com.zsy.flashsale.biz.service.impl.FileService;
import com.zsy.flashsale.dao.po.ProductDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author Allenzsy
 * @Date 2022/1/27 1:12
 * @Description:
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    FileService fileService;


    @RequestMapping("/test")
    public String test() {
        System.out.println("test...");
        return "Hello...";
    }

    @RequestMapping("/sale/{id}")
    public String sale(@PathVariable Integer id) {
        try {
            productService.saleProductUnsafe(id);
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
                res = orderService.createOrderUnsafe(id);
            } else if("xlock".equals(method)) {
                res = orderService.createOrderXLock(id);
            } else if("caslock".equals(method)) {
                res = orderService.createOrderCasLock(id);
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
                ProductDo product = productService.getProduct(id);
                if (product == null || product.getCount() <= 0)
                    return "已售罄，商品"+product.getName()+"库存为0";
                res = orderService.createOrderUnsafe(product);

            } else if("caslock".equals(method)) {
                ProductDo product = productService.getProduct(id);
                if (product == null || product.getCount() <= 0)
                    return "已售罄，商品"+product.getName()+"库存为0";
                res = orderService.createOrderCasLock(product);

            } else if("xlock".equals(method)) {
                res = orderService.createOrderByXLock(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(res == 0)
            return "使用" + method + "下单失败";
        return "使用" + method + "下单成功";

    }
    @RequestMapping("/redis/{key}/{val}")
    public String testRedis(@PathVariable String key,
                            @PathVariable String val) {
        stringRedisTemplate.opsForValue().set(key, val, 5, TimeUnit.SECONDS);
        productService.getProductWithCache(1);

        return "设置成功";
    }

    @RequestMapping("/export")
    public String export() {
        fileService.export();
        return "导出成功";
    }


}
