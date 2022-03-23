package com.zsy.flashsale.dao.mapper;

import com.zsy.flashsale.dao.po.OrderDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface OrderMapper {

    @Insert({"insert into `order` (`pid`, `name`, `create_time`) values (#{pid}, #{name}, #{createTime, jdbcType=TIMESTAMP})" })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    int insertOrder(OrderDo orderDo);
}
