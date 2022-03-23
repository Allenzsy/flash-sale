package com.zsy.flashsale.web.Controller;

import com.zsy.flashsale.biz.service.OrderService;
import com.zsy.flashsale.biz.service.ProductService;
import com.zsy.flashsale.dao.po.ProductDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Allenzsy
 * @Date 2022/3/24 0:48
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @RequestMapping("sync/{id}")
    public String createOrderCache(@PathVariable Integer id) {
        int res = 0;

        ProductDo product = productService.getProductWithCache(id);
        if (product == null || product.getCount() <= 0) {
            log.info("已售罄，商品{}库存为0", product.getName());
            return "已售罄，商品"+product.getName()+"库存为0";
        }

        res = orderService.createOrderCasLock(product);
        if(res == 0) {
            log.info("下单失败");
            return "下单失败";
        }
        log.info("下单成功");
        productService.delProductCache(id);
        return "下单成功";
    }

}
