package com.cavin.culture.model;

import java.util.Date;

public class Image {
    String picName;

    String userId;

    String picId;

    String picUrl;

    String createBy;

    String createDate;

    private byte[] photo;

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Image() {
    }

    public Image(String picName, String userId, String picId, String picUrl, String createBy, String createDate) {
        this.picName = picName;
        this.userId = userId;
        this.picId = picId;
        this.picUrl = picUrl;
        this.createBy = createBy;
        this.createDate = createDate;
    }
}
