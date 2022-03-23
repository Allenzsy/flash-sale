package com.zsy.flashsale.dao.mapper;

import com.zsy.flashsale.dao.po.ProductDo;
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
public interface ProductMapper {

    /**
     * 不加锁查询
     * @param id
     * @return
     */
    @Select("SELECT s.id, s.`name`, s.count FROM product s WHERE s.id = #{id}")
    ProductDo selectById(@Param("id") Integer id);

    /**
     * 加排他锁 XLock 提前锁住要变更的行
     * @param id
     * @return
     */
    @Select("SELECT s.id, s.`name`, s.count FROM product s WHERE s.id = #{id} for update")
    ProductDo selectByIdXLock(@Param("id") Integer id);

    /**
     * 不使用乐观锁
     * @param id
     * @return
     */
    @Update("UPDATE product set count = count-1 WHERE id = #{id}")
    int updateById(@Param("id") Integer id);

    /**
     * 使用乐观锁
     * @param id
     * @return
     */
    @Update("UPDATE product set count = count-1 WHERE id = #{id} AND count = #{count}")
    int updateByIdCasLock(@Param("id") Integer id, @Param("count") Integer count);
}
