package com.zsy.flashsale.biz.service;

import com.zsy.flashsale.dao.po.StockCasDo;
import org.springframework.stereotype.Service;

/**
 * @Author Allenzsy
 * @Date 2022/1/28 1:43
 * @Description:
 */
public interface StockCasService {

    int saleStock(StockCasDo stock);

}
