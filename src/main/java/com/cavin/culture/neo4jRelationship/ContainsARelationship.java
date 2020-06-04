package com.cavin.culture.neo4jRelationship;


import com.cavin.culture.neo4jmodel.BaseNode;
import com.cavin.culture.neo4jmodel.e1;
import com.cavin.culture.neo4jmodel.e2;
import org.neo4j.ogm.annotation.*;

/*
* 包含关系
* */
@RelationshipEntity(type = "has")
public class ContainsARelationship {
    @GraphId
    private Long id;

    @Property(name="name")
    private String name;

    //关系的一端节点
    @StartNode
    private BaseNode start;

    //关系另一端节点
    @EndNode
    private BaseNode end;

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

    public BaseNode getStart() {
        return start;
    }

    public void setStart(BaseNode start) {
        this.start = start;
    }

    public BaseNode getEnd() {
        return end;
    }

    public void setEnd(BaseNode end) {
        this.end = end;
    }

    public ContainsARelationship() {
    }

    public ContainsARelationship(Long id, String name, BaseNode start, BaseNode end) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
