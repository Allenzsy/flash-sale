package com.zsy.flashsale.web.Controller;

import com.alibaba.fastjson.JSONObject;
import com.zsy.flashsale.biz.service.OrderService;
import com.zsy.flashsale.biz.service.ProductService;
import com.zsy.flashsale.dao.po.ProductDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
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

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RequestMapping("sync/{id}")
    public String createOrderWithCache(@PathVariable Integer id) {
        int res = 0;
        // 这里仅使用缓存，在数据库中还有库存的情况下性能应该没有不使用缓存好（多了缓存判断，失效还是会读数据库，并且因为高并发场景下，大多数读取完缓存再用CAS去减库存时都无法成功），
        // 只有当数据库中也真正无库存时，性能会一下上来，这是由于根本不会往下走，缓存中库存就是0，直接返回已售罄了
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

    @RequestMapping("async/{id}")
    public String createOrderByMQ(@PathVariable Integer id) {
        ProductDo product = productService.getProductWithCache(id);
        if (product == null || product.getCount() <= 0) {
            log.info("已售罄，商品{}库存为0", product.getName());
            return "已售罄，商品"+product.getName()+"库存为0";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        String message = jsonObject.toJSONString();

        log.info("这就去通知消息队列开始下单：[{}]", message);
        this.rabbitTemplate.convertAndSend("orderQueue", message);
        return "正在下单请稍后";
    }

}
