package com.cavin.culture.neo4jmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

public class e4{

    @GraphId
    private Long id;

    private String name;

    private String label;

    @Relationship(type = "research")
    @JsonProperty("research")
    public List<e2> research;


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

    public e4(){};
}
