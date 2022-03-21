package com.zsy.flashsale.biz.service;

import com.zsy.flashsale.dao.po.StockDo;

/**
 * @Author Allenzsy
 * @Date 2022/1/28 1:43
 * @Description:
 */
public interface StockService {

    StockDo saleStockUnsafe(Integer id);
    StockDo saleStockXLock(Integer id);
    StockDo saleStockCasLock(Integer id);

    StockDo getStock(Integer id);
    StockDo getStockByXLock(Integer id);
    int saleStock(StockDo stockDo);
    int saleStockByCasLock(StockDo stockDo);
}
