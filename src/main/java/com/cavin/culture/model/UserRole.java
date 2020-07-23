package com.cavin.culture.model;

public class UserRole {
    private Integer id;

    private Integer rid;

    private Integer uid;

    private String remark;

    public UserRole() {
    }

    public UserRole(Integer id, Integer rid, Integer uid, String remark) {
        this.id = id;
        this.rid = rid;
        this.uid = uid;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
