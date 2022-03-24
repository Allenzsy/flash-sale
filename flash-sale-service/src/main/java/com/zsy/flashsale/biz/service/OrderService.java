package com.zsy.flashsale.biz.service;

import com.zsy.flashsale.dao.po.ProductDo;

public interface OrderService {

    int createOrderUnsafe(Integer id);
    int createOrderCasLock(Integer id);
    int createOrderXLock(Integer id);

    int createOrderUnsafe(ProductDo product);
    int createOrderCasLock(ProductDo product);
    int createOrderByXLock(Integer pid);
    int createOrderByMQ(Integer pid);


}
