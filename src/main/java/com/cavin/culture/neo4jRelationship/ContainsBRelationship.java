package com.cavin.culture.neo4jRelationship;


import com.cavin.culture.neo4jmodel.e3;
import com.cavin.culture.neo4jmodel.e4;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "has")
public class ContainsBRelationship {

    @GraphId
    private Long id;

    private String name;

    //关系的一端节点
    @StartNode
    private e3 start;

    //关系另一端节点
    @EndNode
    private e4 end;
}
