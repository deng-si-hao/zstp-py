package com.cavin.culture.model;

public class GraphEntity {
    private int domainid;

    private String domain;//对应数据库label

    private String nodename;//对应数据库node

    private String[] relation;//对应数据库关系

    private int matchtype;

    // private int pageSize = 10;

    // private int pageIndex = 1;


    public int getDomainid() {
        return domainid;
    }

    public void setDomainid(int domainid) {
        this.domainid = domainid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }

    public String[] getRelation() {
        return relation;
    }

    public void setRelation(String[] relation) {
        this.relation = relation;
    }

    public int getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(int matchtype) {
        this.matchtype = matchtype;
    }
}
