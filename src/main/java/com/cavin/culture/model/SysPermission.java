package com.cavin.culture.model;

import java.util.Date;

/**
 * 权限表，用于给用户分配权限
 */
public class SysPermission {
    private Integer permissionId;

    private String permissionName;

    private String permissionCode;

    private Integer permissionStatus;

    private String permissionRemark;

    private String operator;

    private Date operateTime;

    public SysPermission() {
    }

    public SysPermission(Integer permissionId, String permissionName, String permissionCode, Integer permissionStatus, String permissionRemark, String operator, Date operateTime) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.permissionCode = permissionCode;
        this.permissionStatus = permissionStatus;
        this.permissionRemark = permissionRemark;
        this.operator = operator;
        this.operateTime = operateTime;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Integer getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(Integer permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public String getPermissionRemark() {
        return permissionRemark;
    }

    public void setPermissionRemark(String permissionRemark) {
        this.permissionRemark = permissionRemark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
