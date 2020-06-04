package com.cavin.culture.neo4jmodel;

import com.cavin.culture.neo4jRelationship.DemandBRelationship;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class e3 extends BaseNode{

    @GraphId
    private Long id;

    private String name;

    private String label;

    @Relationship
    public List<DemandBRelationship> need;


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

    public e3(){};
}
