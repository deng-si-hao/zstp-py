package com.cavin.culture.model;

import java.util.HashMap;
import java.util.Map;

public class JsonMessage {

    private int code;
    private String message;
    private Map<String, Object> data = new HashMap<>();
    private String level;

    public JsonMessage() {
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public static JsonMessage success(){
        JsonMessage message = new JsonMessage();
        message.setCode(200);
        message.setMessage("请求成功！");
        return message;
    }
    public static JsonMessage adminLogin(String level){
        JsonMessage message = new JsonMessage();
        message.setCode(200);
        message.setMessage("请求成功！");
        message.setLevel(level);
        return message;
    }


    public static JsonMessage error(Integer code, String errorMsg){
        JsonMessage message = new JsonMessage();
        message.setCode(code);
        message.setMessage(errorMsg);
        return message;
    }

    public JsonMessage addData(String key, Object val) {
        this.getData().put(key, val);
        return this;
    }

}
