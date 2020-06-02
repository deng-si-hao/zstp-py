package com.cavin.culture.neo4jmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class e1{
    @GraphId
    private Long id;

    private String name;

    private String label;

    @Relationship(type = "apply")
    @JsonProperty("apply")
    public List<e2> apply;

    @Relationship(type = "need")
    @JsonProperty("need")
    public List<e3> need;


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

    public e1(){};
}
