package com.zsy.flashsale.dao.utils;

/**
 * @Author Allenzsy
 * @Date 2022/3/23 20:53
 * @Description:
 */
public enum CacheKey {


    PRODUCT("sale:product:%s"),
    ORDER("sal:order:%s");

    private String sFormat;

    private CacheKey(String sFormat) {
        this.sFormat = sFormat;
    }

    public String getKey(int id) {
        return String.format(sFormat, id);
    }
}
