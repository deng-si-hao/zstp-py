package com.cavin.culture.neo4jmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class e2{

    @GraphId
    private Long id;

    private String name;

    private String label;

    private String flag;

    private String coll;

    private String person;

    @Relationship(type = "apply")
    @JsonProperty("apply")
    private List<e1> apply;

    @Relationship(type = "need")
    @JsonProperty("need")
    private List<e1> need;

    @Relationship(type = "has")
    @JsonProperty("has")
    private List<e2> has;

    @Relationship(type = "coop")
    @JsonProperty("coop")
    private List<e2> coop;

/*
    @Relationship(type = "need")
    @JsonProperty("need")
    private List<e3> e3s;
*/


    @Relationship(type = "research")
    @JsonProperty("research")
    private List<e4> e4s;



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getLabel() {
        return label;
    }


    public void setLabel(String label) {
        this.label = label;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getColl() {
        return coll;
    }

    public void setColl(String coll) {
        this.coll = coll;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
    public e2(){}
}
