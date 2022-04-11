package com.zsy.flashsale.dao.mapper;

import com.zsy.flashsale.dao.po.ExportFile;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Author Allenzsy
 * @Date 2022/4/10 0:50
 * @Description:
 */
@Mapper
public interface TradeLogMapper {

    List<ExportFile> selectPage(@Param("primaryKey") String primaryKey, @Param("pageSize") int pageSize);

    List<ExportFile> selectPagePart(@Param("primaryKeyBegin") String primaryKeyBegin, @Param("primaryKeyEnd") String primaryKeyEnd, @Param("pageSize") int pageSize);

}
