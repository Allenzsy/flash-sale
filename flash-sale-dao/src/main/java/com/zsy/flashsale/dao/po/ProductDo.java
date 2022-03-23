package com.zsy.flashsale.dao.po;

import lombok.ToString;

import java.io.Serializable;

/**
 * @Author Allenzsy
 * @Date 2022/1/27 23:30
 * @Description:
 */
@ToString
public class ProductDo implements Serializable {

    private static final long serialVersionUID = -3006960596174960544L;

    private Integer id;
    private String name;
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDo productDo = (ProductDo) o;

        if (id != null ? !id.equals(productDo.id) : productDo.id != null) return false;
        if (name != null ? !name.equals(productDo.name) : productDo.name != null) return false;
        return count != null ? count.equals(productDo.count) : productDo.count == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }
}
