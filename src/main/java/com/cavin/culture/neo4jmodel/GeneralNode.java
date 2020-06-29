package com.cavin.culture.neo4jmodel;

/**
*用于插入实体，属性即参数
* session.run方法执行，不用jpa通道
* */
public class GeneralNode {
    private String label;

    private String name;

    //5个通用属性
    private String propertya;

    private String propertyb;

    private String propertyc;

    private String propertyd;

    private String propertye;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public GeneralNode() {
    }

    public GeneralNode(String label, String name, String propertya, String propertyb, String propertyc, String propertyd, String propertye) {
        this.label = label;
        this.name = name;
        this.propertya = propertya;
        this.propertyb = propertyb;
        this.propertyc = propertyc;
        this.propertyd = propertyd;
        this.propertye = propertye;
    }

    @Override
    public String toString() {
        return "generalNode{" +
                "label='" + label + '\'' +
                ", name='" + name + '\'' +
                ", propertya='" + propertya + '\'' +
                ", propertyb='" + propertyb + '\'' +
                ", propertyc='" + propertyc + '\'' +
                ", propertyd='" + propertyd + '\'' +
                ", propertye='" + propertye + '\'' +
                '}';
    }
}
