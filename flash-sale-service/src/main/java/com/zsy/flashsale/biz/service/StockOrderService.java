package com.zsy.flashsale.biz.service;

import com.zsy.flashsale.dao.po.StockDo;

public interface StockOrderService {

    int createOrderUnsafe(Integer id);
    int createOrderCasLock(Integer id);
    int createOrderXLock(Integer id);

    int createOrderUnsafe(StockDo stock);
    int createOrderCasLock(StockDo stock);
    int createOrderByXLock(Integer id);


}
