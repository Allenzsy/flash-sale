package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.service.ProductService;
import com.zsy.flashsale.biz.service.OrderService;
import com.zsy.flashsale.dao.mapper.OrderMapper;
import com.zsy.flashsale.dao.po.ProductDo;
import com.zsy.flashsale.dao.po.OrderDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderMapper OrderMapper;
    @Autowired
    ProductService productService;

    /**
     * 若仅使用 @Transactional，由于默认级别是注解无法解决超卖问题
     * @param pid
     * @return
     * @throws Exception
     */
    @Override
    @Transactional // @Transactional(isolation = SERIALIZABLE) 使用串行化可解决
    public int createOrderUnsafe(Integer pid) {
        ProductDo productDo = productService.saleProductUnsafe(pid);
        OrderDo orderDo = new OrderDo(productDo.getId(), productDo.getName(), new Date());
        int res = OrderMapper.insertOrder(orderDo);
        return res;
    }

    /**
     *
     * @param pid
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int createOrderXLock(Integer pid) {
        ProductDo productDo = productService.saleProductXLock(pid);
        OrderDo orderDo = new OrderDo(productDo.getId(), productDo.getName(), new Date());
        int res = OrderMapper.insertOrder(orderDo);
        return res;
    }

    /**
     *
     * @param pid
     * @return
     * @throws Exception
     */
    @Override
    public int createOrderCasLock(Integer pid) {
        ProductDo productDo = null;
        try {
            productDo = productService.saleProductCasLock(pid);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        OrderDo orderDo = new OrderDo(productDo.getId(), productDo.getName(), new Date());
        int res = OrderMapper.insertOrder(orderDo);
        return res;
    }

    /*************************************************
     *                                              *
     *              细粒度拆分功能                     *
     *                                              *
     ************************************************/

    /**
     * 若仅使用 @Transactional，由于默认级别是注解无法解决超卖问题
     * @param product 库存对象
     * @return
     * @throws Exception
     */
    @Override
    @Transactional // @Transactional(isolation = SERIALIZABLE) 使用串行化可解决
    public int createOrderUnsafe(ProductDo product) {
        int res = productService.saleProduct(product);
        if (res == 0) {
            throw new RuntimeException(product+"扣库存失败");
        }
        OrderDo orderDo = new OrderDo(product.getId(), product.getName(), new Date());
        res = OrderMapper.insertOrder(orderDo);
        return res;
    }

    /**
     *
     * @param product
     * @return
     * @throws Exception
     */
    @Override
    public int createOrderCasLock(ProductDo product) {
        // 通过乐观锁更新库存，没有符合条件的数据 mapper 会返回 0
        int res = productService.saleProductByCasLock(product);
        if (res == 0) {
            System.out.println(product+"扣库存失败");
            return 0;
        }
        OrderDo orderDo = new OrderDo(product.getId(), product.getName(), new Date());
        res = OrderMapper.insertOrder(orderDo);
        return res;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int createOrderByXLock(Integer id) {
        // 通过排他锁，提前锁住要扣库存的数据（变更行）
        ProductDo product = productService.getProductByXLock(id);
        if (product == null || product.getCount() <= 0) {
            System.out.println("已售罄，商品"+product.getName()+"库存为0");
            throw new RuntimeException("已售罄，商品"+product.getName()+"库存为0");
        }
        int res = productService.saleProductByCasLock(product);
        if (res == 0) {
            System.out.println(product+"扣库存失败");
            return 0;
        }
        OrderDo orderDo = new OrderDo(product.getId(), product.getName(), new Date());
        res = OrderMapper.insertOrder(orderDo);
        return res;
    }



}
