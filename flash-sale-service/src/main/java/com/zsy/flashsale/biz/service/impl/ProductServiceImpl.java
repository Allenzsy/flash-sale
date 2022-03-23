package com.zsy.flashsale.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.zsy.flashsale.biz.service.ProductService;
import com.zsy.flashsale.dao.mapper.ProductMapper;
import com.zsy.flashsale.dao.po.ProductDo;
import com.zsy.flashsale.dao.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @Author Allenzsy
 * @Date 2022/1/28 1:44
 * @Description:
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Resource
    ProductMapper productMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public ProductDo saleProductUnsafe(Integer id) {
        ProductDo productDo = productMapper.selectById(id);
        if (productDo.getCount() <= 0) {
            throw new RuntimeException("sale out");
        }
        int res = productMapper.updateById(id);
        return productDo;
    }

    @Override
    public ProductDo saleProductXLock(Integer id) {
        ProductDo productDo = productMapper.selectByIdXLock(id);
        if (productDo.getCount() <= 0) {
            throw new RuntimeException("sale out");
        }
        int res = productMapper.updateById(id);
        return productDo;
    }

    @Override
    public ProductDo saleProductCasLock(Integer id) {
        ProductDo productDo = productMapper.selectById(id);
        if (productDo.getCount() <= 0) {
            throw new RuntimeException("sale out");
        }
        int res = productMapper.updateByIdCasLock(id, productDo.getCount());
        if(res == 0) {
            throw new RuntimeException("already bought by other");
        }
        return productDo;
    }

    /* 新方式 */
    @Override
    public ProductDo getProduct(Integer id) {
        ProductDo productDo = productMapper.selectById(id);
        return productDo;
    }

    @Override
    public ProductDo getProductByXLock(Integer id) {
        return productMapper.selectByIdXLock(id);
    }

    @Override
    public int saleProduct(ProductDo productDo) {
        return productMapper.updateById(productDo.getId());
    }

    @Override
    public int saleProductByCasLock(ProductDo productDo) {
        return productMapper.updateByIdCasLock(productDo.getId(), productDo.getCount());
    }

    @Override
    public ProductDo getProductWithCache(Integer id) {
        String key = CacheKey.PRODUCT.getKey(id);
        log.debug("生成缓存key: [{}]", key);
        String jProduct = redisTemplate.opsForValue().get(key);
        ProductDo product = null;
        if(jProduct != null && jProduct.length() != 0) {
            try {
                log.info("命中缓存: [{}]", jProduct);
                product = JSON.parseObject(jProduct, ProductDo.class);
                return product;
            } catch (Exception e) {
                log.error("反序列化商品信息失败", e);
            }
        }
        // 未命中缓存，则查数据库并放入缓存
        product = this.getProduct(id);
        if (product == null) {
            return null;
        }
        setProductCache(key, product);
        return product;
    }

    @Override
    public void delProductCache(Integer id) {
        String key = CacheKey.PRODUCT.getKey(id);
        redisTemplate.delete(key);
        log.info("删除商品缓存 key: [{}] ", key);
    }

    @Override
    public void setProductCache(String key, ProductDo product) {
        String jProduct = JSON.toJSONString(product);
        redisTemplate.opsForValue().set(key, jProduct, 30, TimeUnit.SECONDS);
        log.info("写入商品缓存 key: [{}], value:[{}]", key, jProduct);;
    }
}
