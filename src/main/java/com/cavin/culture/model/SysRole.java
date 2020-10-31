package com.cavin.culture.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色表，用于给用户添加不同的角色
 */
public class SysRole {
    private Integer roleId;

    private String roleName;

    private String roleType;

    private Integer roleStatus;

    private String roleRemark;

    private String operator;

    private Date operateTime;

    private Set<SysPermission> sysPermissions = new HashSet<>();

    public SysRole() {
    }

    public Set<SysPermission> getSysPermissions() {
        return sysPermissions;
    }

    public void setSysPermissions(Set<SysPermission> sysPermissions) {
        this.sysPermissions = sysPermissions;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Integer getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(Integer roleStatus) {
        this.roleStatus = roleStatus;
    }

    public String getRoleRemark() {
        return roleRemark;
    }

    public void setRoleRemark(String roleRemark) {
        this.roleRemark = roleRemark;
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

    private Set<SysPermission> permissions = new HashSet<>();

    private Set<User> users = new HashSet<>();


    public Set<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
