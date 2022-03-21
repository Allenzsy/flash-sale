package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.service.StockService;
import com.zsy.flashsale.dao.mapper.StockMapper;
import com.zsy.flashsale.dao.po.StockDo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Allenzsy
 * @Date 2022/1/28 1:44
 * @Description:
 */
@Service
public class StockServiceImpl implements StockService {
    @Resource
    StockMapper stockMapper;

    @Override
    public StockDo saleStockUnsafe(Integer id) {
        StockDo stockDo = stockMapper.selectById(id);
        if (stockDo.getCount() <= 0) {
            throw new RuntimeException("sale out");
        }
        int res = stockMapper.updateById(id);
        return stockDo;
    }

    @Override
    public StockDo saleStockXLock(Integer id) {
        StockDo stockDo = stockMapper.selectByIdXLock(id);
        if (stockDo.getCount() <= 0) {
            throw new RuntimeException("sale out");
        }
        int res = stockMapper.updateById(id);
        return stockDo;
    }

    @Override
    public StockDo saleStockCasLock(Integer id) {
        StockDo stockDo = stockMapper.selectById(id);
        if (stockDo.getCount() <= 0) {
            throw new RuntimeException("sale out");
        }
        int res = stockMapper.updateByIdCasLock(id, stockDo.getCount());
        if(res == 0) {
            throw new RuntimeException("already bought by other");
        }
        return stockDo;
    }

    @Override
    public StockDo getStock(Integer id) {
        StockDo stockDo = stockMapper.selectById(id);
        return stockDo;
    }

    @Override
    public StockDo getStockByXLock(Integer id) {
        return stockMapper.selectByIdXLock(id);
    }

    @Override
    public int saleStock(StockDo stockDo) {
        return stockMapper.updateById(stockDo.getId());
    }

    @Override
    public int saleStockByCasLock(StockDo stockDo) {
        return stockMapper.updateByIdCasLock(stockDo.getId(), stockDo.getCount());
    }
}
