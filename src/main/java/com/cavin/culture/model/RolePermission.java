package com.cavin.culture.model;

public class RolePermission {

    private Integer id;

    private Integer rid;

    private Integer pid;

    private String remark;

    public RolePermission() {
    }

    public RolePermission(Integer id, Integer rid, Integer pid, String remark) {
        this.id = id;
        this.rid = rid;
        this.pid = pid;
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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
