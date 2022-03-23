package com.zsy.flashsale.biz.service;

import com.zsy.flashsale.dao.po.ProductDo;

/**
 * @Author Allenzsy
 * @Date 2022/1/28 1:43
 * @Description:
 */
public interface ProductService {

    ProductDo saleProductUnsafe(Integer id);
    ProductDo saleProductXLock(Integer id);
    ProductDo saleProductCasLock(Integer id);

    ProductDo getProduct(Integer id);
    ProductDo getProductByXLock(Integer id);
    int saleProduct(ProductDo productDo);
    int saleProductByCasLock(ProductDo productDo);

    /**
     * 先从缓存获取商品，未命中则从数据库读
     * @param id
     * @return
     */
    ProductDo getProductWithCache(Integer id);
    /**
     * 删除商品缓存
     * @param id
     */
    void delProductCache(Integer id);
    /**
     * 将商品插入缓存
     * @param key
     * @param product
     */
    void setProductCache(String key, ProductDo product);

}
