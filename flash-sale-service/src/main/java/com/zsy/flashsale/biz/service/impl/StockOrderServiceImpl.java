package com.zsy.flashsale.biz.service.impl;

import com.zsy.flashsale.biz.service.StockService;
import com.zsy.flashsale.biz.service.StockOrderService;
import com.zsy.flashsale.dao.mapper.StockOrderMapper;
import com.zsy.flashsale.dao.po.StockDo;
import com.zsy.flashsale.dao.po.StockOrderDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class StockOrderServiceImpl implements StockOrderService {

    @Resource
    StockOrderMapper stockOrderMapper;
    @Autowired
    StockService stockService;

    /**
     * 若仅使用 @Transactional，由于默认级别是注解无法解决超卖问题
     * @param sid
     * @return
     * @throws Exception
     */
    @Override
    @Transactional // @Transactional(isolation = SERIALIZABLE) 使用串行化可解决
    public int createOrderUnsafe(Integer sid) {



        StockDo stockDo = stockService.saleStockUnsafe(sid);
        StockOrderDo stockOrderDo = new StockOrderDo(stockDo.getId(), stockDo.getName(), new Date());
        int res = stockOrderMapper.insertOrder(stockOrderDo);
        return res;
    }

    /**
     *
     * @param sid
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int createOrderXLock(Integer sid) {
        StockDo stockDo = stockService.saleStockXLock(sid);
        StockOrderDo stockOrderDo = new StockOrderDo(stockDo.getId(), stockDo.getName(), new Date());
        int res = stockOrderMapper.insertOrder(stockOrderDo);
        return res;
    }

    /**
     *
     * @param sid
     * @return
     * @throws Exception
     */
    @Override
    public int createOrderCasLock(Integer sid) {
        StockDo stockDo = null;
        try {
            stockDo = stockService.saleStockCasLock(sid);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        StockOrderDo stockOrderDo = new StockOrderDo(stockDo.getId(), stockDo.getName(), new Date());
        int res = stockOrderMapper.insertOrder(stockOrderDo);
        return res;
    }

    /**
     * 若仅使用 @Transactional，由于默认级别是注解无法解决超卖问题
     * @param stock 库存对象
     * @return
     * @throws Exception
     */
    @Override
    @Transactional // @Transactional(isolation = SERIALIZABLE) 使用串行化可解决
    public int createOrderUnsafe(StockDo stock) {
        int res = stockService.saleStock(stock);
        if (res == 0) {
            throw new RuntimeException(stock+"扣库存失败");
        }
        StockOrderDo stockOrderDo = new StockOrderDo(stock.getId(), stock.getName(), new Date());
        res = stockOrderMapper.insertOrder(stockOrderDo);
        return res;
    }

    /**
     *
     * @param stock
     * @return
     * @throws Exception
     */
    @Override
    public int createOrderCasLock(StockDo stock) {
        // 通过乐观锁更新库存，没有符合条件的数据 mapper 会返回 0
        int res = stockService.saleStockByCasLock(stock);
        if (res == 0) {
            System.out.println(stock+"扣库存失败");
            return 0;
        }
        StockOrderDo stockOrderDo = new StockOrderDo(stock.getId(), stock.getName(), new Date());
        res = stockOrderMapper.insertOrder(stockOrderDo);
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
        StockDo stock = stockService.getStockByXLock(id);
        if (stock == null || stock.getCount() <= 0) {
            System.out.println("已售罄，商品"+stock.getName()+"库存为0");
            throw new RuntimeException("已售罄，商品"+stock.getName()+"库存为0");
        }
        int res = stockService.saleStockByCasLock(stock);
        if (res == 0) {
            System.out.println(stock+"扣库存失败");
            return 0;
        }
        StockOrderDo stockOrderDo = new StockOrderDo(stock.getId(), stock.getName(), new Date());
        res = stockOrderMapper.insertOrder(stockOrderDo);
        return res;
    }



}
