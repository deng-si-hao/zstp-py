package com.cavin.culture.model;

public class Image {
    String picName;

    String userId;

    String picId;

    String picUrl;

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

    public Image(String picName,String userId, String picId, String picUrl) {
        this.picName=picName;
        this.userId = userId;
        this.picId = picId;
        this.picUrl = picUrl;
    }
}
