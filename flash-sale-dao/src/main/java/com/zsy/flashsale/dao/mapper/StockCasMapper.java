package com.zsy.flashsale.dao.mapper;

import com.zsy.flashsale.dao.po.StockCasDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author Allenzsy
 * @Date 2022/1/27 23:24
 * @Description:
 */
@Mapper
public interface StockCasMapper {

    @Update("update stock_cas set count = count-1 where id = #{id}")
    int updateById(@Param("id") Integer id);

    @Select("SELECT s.id, s.`name`, s.count, s.version FROM stock_cas s WHERE s.id = #{id}")
    StockCasDo selectById(@Param("id") Integer id);
}