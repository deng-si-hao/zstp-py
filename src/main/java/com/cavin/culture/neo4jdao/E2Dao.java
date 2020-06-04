package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jmodel.e2;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface E2Dao extends Neo4jRepository<e2,Long> {

    e2 findByName(@Param("name") String name);

    @Query("MATCH (n:e2) RETURN n.name;")
    List<String> findBodesByLabel();

    Iterable<e2> findAll();

/*    @Query("match p = (n)-[r:has]-(b) return p")
    List<Map<String,Object>> getrelation();*/
}
