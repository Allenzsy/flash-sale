package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.service.StockCasService;
import com.zsy.flashsale.biz.service.StockOrderService;
import com.zsy.flashsale.dao.mapper.StockOrderMapper;
import com.zsy.flashsale.dao.po.StockCasDo;
import com.zsy.flashsale.dao.po.StockOrderDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Service
public class StockOrderServiceImpl implements StockOrderService {

    @Resource
    StockOrderMapper stockOrderMapper;
    @Autowired
    StockCasService stockCasService;

    @Override
    @Transactional(isolation = SERIALIZABLE)
    public int createOrderUnsafe(Integer sid) throws Exception {
        StockCasDo stockCasDo = stockCasService.saleStock(sid);
        StockOrderDo stockOrderDo = new StockOrderDo(stockCasDo.getId(), stockCasDo.getName(), new Date());
        stockOrderMapper.insertOrder(stockOrderDo);
        return stockOrderDo.getId();
    }
}
