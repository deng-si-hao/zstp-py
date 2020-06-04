package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jRelationship.ReportingRelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ReportingRelationshipDao extends Neo4jRepository<ReportingRelationship,Long> {

    Iterable<ReportingRelationship> findByName(@Param("name")String name);
}
