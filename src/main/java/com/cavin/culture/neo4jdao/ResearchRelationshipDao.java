package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jRelationship.ResearchRelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ResearchRelationshipDao extends Neo4jRepository<ResearchRelationship,Long> {

    Iterable<ResearchRelationship> findByName(@Param("name")String name);
}
