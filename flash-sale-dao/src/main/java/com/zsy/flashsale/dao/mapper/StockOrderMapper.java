package com.zsy.flashsale.dao.mapper;

import com.zsy.flashsale.dao.po.StockOrderDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface StockOrderMapper {

    @Insert({"insert into stock_order(sid, name, create_time) " +
             "values(#{sid}, #{name}, #{createTime, jdbcType=TIMESTAMP})" })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    int insertOrder(StockOrderDo stockOrderDo);
}
