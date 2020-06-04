package com.cavin.culture.neo4jRelationship;

import com.cavin.culture.neo4jmodel.e1;
import com.cavin.culture.neo4jmodel.e3;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
/*
* 需求关系
* */

public class DemandBRelationship {
    @GraphId
    private Long id;

    private String name;

    //关系的一端节点
    @StartNode
    private e1 start;

    //关系另一端节点
    @EndNode
    private e3 end;

    public DemandBRelationship() {
    }

    public DemandBRelationship(Long id, String name, e1 start, e3 end) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
    }

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

    public e1 getStart() {
        return start;
    }

    public void setStart(e1 start) {
        this.start = start;
    }

    public e3 getEnd() {
        return end;
    }

    public void setEnd(e3 end) {
        this.end = end;
    }
}
