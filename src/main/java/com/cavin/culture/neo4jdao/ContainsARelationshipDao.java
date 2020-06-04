package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jRelationship.ContainsARelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface ContainsARelationshipDao extends Neo4jRepository<ContainsARelationship,Long> {

/*    @Query("match p = (n)-[r:has]-(b) return p")
    List<Map<String,Object>> getHas();*/

    Iterable<ContainsARelationship> findByname(@Param("name")String name);

    Iterable<ContainsARelationship> findAll();
/*
    @Query("match p = (n)-[r:has]-(b) return p limit 25")
    List<ContainsARelationship> getContainsARelationship();*/
}
