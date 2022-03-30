package com.zsy.flashsale.dao.mapper;

import com.zsy.flashsale.dao.po.ExportFile;
import com.zsy.flashsale.dao.po.OrderDo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert({"insert into `order` (`pid`, `name`, `create_time`) values (#{pid}, #{name}, #{createTime, jdbcType=TIMESTAMP})" })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    int insertOrder(OrderDo orderDo);

    @Select({"SELECT CONCAT(RPAD(`pid`,11,' ')," +
            "CONCAT(`name`, RPAD(' ', 30 - LENGTH(`name`), ' '))," +
            "RPAD(`create_time`, 19,' ')) as content " +
            "FROM `order`" +
            "LIMIT #{index}, #{pageSize}"})
    @Results({
            @Result(column = "content", property = "content", jdbcType = JdbcType.VARCHAR)
    })
    List<ExportFile> selectPage(int index, int pageSize);
}
