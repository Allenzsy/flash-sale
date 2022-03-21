package com.zsy.flashsale.dao.po;

/**
 * @Author Allenzsy
 * @Date 2022/1/27 23:30
 * @Description:
 */
public class StockDo {
    private Integer id;
    private String name;
    private Integer count;
    private Integer version;

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
    public String toString() {
        return "StockDo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
