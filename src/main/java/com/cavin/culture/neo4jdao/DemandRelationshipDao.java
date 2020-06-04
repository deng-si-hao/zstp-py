package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jRelationship.DemandBRelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DemandRelationshipDao extends Neo4jRepository<DemandBRelationship,Long> {

    Iterable<DemandBRelationship> findByName(@Param("name")String name);
}
