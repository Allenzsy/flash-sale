package com.zsy.flashsale.dao.po;

import java.util.Date;

public class OrderDo {

    private Integer id;
    private Integer pid;
    private String name;
    private Date createTime;

    public OrderDo() {
    }

    public OrderDo(Integer pid, String name, Date createTime) {
        this.pid = pid;
        this.name = name;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
