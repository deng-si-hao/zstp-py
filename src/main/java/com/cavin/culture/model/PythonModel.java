package com.cavin.culture.model;

public class PythonModel {
    String path;
    String method;
    String param1;
    String param2;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public PythonModel(String path, String method, String param1) {
        this.path = path;
        this.method = method;
        this.param1 = param1;
    }

    public PythonModel(String path, String method, String param1, String param2) {
        this.path = path;
        this.method = method;
        this.param1 = param1;
        this.param2 = param2;
    }

    public PythonModel(String path, String method) {
        this.path = path;
        this.method = method;
    }

}
