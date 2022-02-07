package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.service.StockCasService;
import com.zsy.flashsale.dao.mapper.StockCasMapper;
import com.zsy.flashsale.dao.po.StockCasDo;
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
    public StockCasDo saleStock(Integer id) throws Exception {
        StockCasDo stockCasDo = checkStock(id);
        int res = stockCasMapper.updateById(id);
        return stockCasDo;
    }

    private StockCasDo checkStock(Integer id) {
        StockCasDo stockCasDo = stockCasMapper.selectById(id);
        if (stockCasDo.getCount() <= 0) {
            throw new RuntimeException("sale out");
        }
        return stockCasDo;
    }

}
