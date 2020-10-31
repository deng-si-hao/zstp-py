package com.cavin.culture.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User {

    public static final String commander = "1";

    private Long id;
    private String userName;
    private String userPassword;
    private String email;
    private String registerDate;
    private String level;//默认为0，管理员为1，无需添加
    private String isDel;//默认为0，删除为1，无需添加

    private Set<SysRole> roles = new HashSet<>();

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    public User() {
    }

    public User(Long id, String userName, String userPassword, String email, String registerDate, String level, String isDel) {
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
        this.email = email;
        this.registerDate = registerDate;
        this.level = level;
        this.isDel = isDel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", email='" + email + '\'' +
                ", registerDate=" + registerDate +
                ", level='" + level + '\'' +
                ", isDel='" + isDel + '\'' +
                '}';
    }
}
