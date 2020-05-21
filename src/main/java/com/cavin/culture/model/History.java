package com.cavin.culture.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class History {

    private long historyId;
    private String historySubject;
    private String historyPredicate;
    private String historyObject;
    private String historyScope;
    private String historyType;
    private Date createDate;
    private long userId;
    private String userName;

    public History() {
    }

    public History(long historyId, String historySubject, String historyPredicate, String historyObject, String historyScope, String historyType, Date createDate,long userId, String userName) {
        this.historyId = historyId;
        this.historySubject = historySubject;
        this.historyPredicate = historyPredicate;
        this.historyObject = historyObject;
        this.historyScope = historyScope;
        this.historyType = historyType;
        this.createDate = createDate;
        this.userId = userId;
        this.userName = userName;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public String getHistorySubject() {
        return historySubject;
    }

    public void setHistorySubject(String historySubject) {
        this.historySubject = historySubject;
    }

    public String getHistoryPredicate() {
        return historyPredicate;
    }

    public void setHistoryPredicate(String historyPredicate) {
        this.historyPredicate = historyPredicate;
    }

    public String getHistoryObject() {
        return historyObject;
    }

    public void setHistoryObject(String historyObject) {
        this.historyObject = historyObject;
    }

    public String getHistoryScope() {
        return historyScope;
    }

    public void setHistoryScope(String historyScope) {
        this.historyScope = historyScope;
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "History{" +
                "historyId=" + historyId +
                ", historySubject='" + historySubject + '\'' +
                ", historyPredicate='" + historyPredicate + '\'' +
                ", historyObject='" + historyObject + '\'' +
                ", historyScope='" + historyScope + '\'' +
                ", historyType='" + historyType + '\'' +
                ", createDate=" + createDate +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }

}
