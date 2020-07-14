package com.cavin.culture.model;

import java.io.Serializable;

public class Neo4jEntity implements Serializable {
    private long uuid;

    private String name;//显示名称

    private String color;//对应关系数据库字段

    private Integer r;

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }
}
