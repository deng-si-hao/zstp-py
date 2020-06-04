package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jRelationship.CooperationRelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CooperationRelationshipDao extends Neo4jRepository<CooperationRelationship,Long> {

    Iterable<CooperationRelationship> findByName(@Param("name")String name);
}
