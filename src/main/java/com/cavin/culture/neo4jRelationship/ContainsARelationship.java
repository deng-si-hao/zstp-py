package com.cavin.culture.neo4jRelationship;


import com.cavin.culture.neo4jmodel.e2;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/*
* 包含关系
* */
@RelationshipEntity(type = "has")
public class ContainsARelationship {
    @GraphId
    private Long id;

    private String name;

    //关系的一端节点
    @StartNode
    private e2 start;

    //关系另一端节点
    @EndNode
    private e2 end;
}
