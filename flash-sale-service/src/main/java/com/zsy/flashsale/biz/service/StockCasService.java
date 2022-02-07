package com.zsy.flashsale.biz.service;

import com.zsy.flashsale.dao.po.StockCasDo;

/**
 * @Author Allenzsy
 * @Date 2022/1/28 1:43
 * @Description:
 */
public interface StockCasService {

    StockCasDo saleStock(Integer id) throws Exception;

}
