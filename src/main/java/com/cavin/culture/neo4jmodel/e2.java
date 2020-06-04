package com.cavin.culture.neo4jmodel;

import com.cavin.culture.neo4jRelationship.*;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


import java.util.List;

@NodeEntity
public class e2 extends BaseNode{

    @GraphId
    private Long id;

    private String name;

    private String label;

    private String flag;

    private String coll;

    private String person;

    @Relationship
    private List<ContainsARelationship> has;
    @Relationship
    private List<ReportingRelationship> apply;
    @Relationship
    private List<CooperationRelationship> coop;
    @Relationship
    private List<ResearchRelationship> research;
    @Relationship
    private List<DemandBRelationship> need;

/*
    @Relationship(type = "need")
    @JsonProperty("need")
    private List<e1> need;
*/


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
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

    public e2(Long id, String name, String label, String flag, String coll, String person, List<ContainsARelationship> has) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.flag = flag;
        this.coll = coll;
        this.person = person;
//        this.has = has;
    }
}
