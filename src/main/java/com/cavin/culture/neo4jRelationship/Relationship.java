package com.cavin.culture.neo4jRelationship;

/**
* 创建通用关系，属性即关系参数
* session.run方法运行
* */
public class Relationship {
    private String start;

    private String startType;

    private String end;

    private String endType;

    private String relationType;

    private String propertya;

    private String propertyb;

    private String propertyc;

    private String propertyd;

    private String propertye;

    public Relationship() {
    }

    public Relationship(String start, String end, String relationType, String propertya, String propertyb, String propertyc, String propertyd, String propertye) {
        this.start = start;
        this.end = end;
        this.relationType = relationType;
        this.propertya = propertya;
        this.propertyb = propertyb;
        this.propertyc = propertyc;
        this.propertyd = propertyd;
        this.propertye = propertye;
    }

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public String getEndType() {
        return endType;
    }

    public void setEndType(String endType) {
        this.endType = endType;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getPropertya() {
        return propertya;
    }

    public void setPropertya(String propertya) {
        this.propertya = propertya;
    }

    public String getPropertyb() {
        return propertyb;
    }

    public void setPropertyb(String propertyb) {
        this.propertyb = propertyb;
    }

    public String getPropertyc() {
        return propertyc;
    }

    public void setPropertyc(String propertyc) {
        this.propertyc = propertyc;
    }

    public String getPropertyd() {
        return propertyd;
    }

    public void setPropertyd(String propertyd) {
        this.propertyd = propertyd;
    }

    public String getPropertye() {
        return propertye;
    }

    public void setPropertye(String propertye) {
        this.propertye = propertye;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", relationType='" + relationType + '\'' +
                ", propertya='" + propertya + '\'' +
                ", propertyb='" + propertyb + '\'' +
                ", propertyc='" + propertyc + '\'' +
                ", propertyd='" + propertyd + '\'' +
                ", propertye='" + propertye + '\'' +
                '}';
    }
}
