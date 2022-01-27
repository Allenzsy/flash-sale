package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.service.StockCasService;
import com.zsy.flashsale.dao.mapper.StockCasMapper;
import com.zsy.flashsale.dao.po.StockCasDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Allenzsy
 * @Date 2022/1/28 1:44
 * @Description:
 */
@Service
public class StockCasServiceImpl implements StockCasService {
    @Resource
    StockCasMapper stockCasMapper;

    @Override
    public int saleStock(StockCasDo stock) {
        int res = stockCasMapper.updateById(stock.getId());
        return res;
    }
}
