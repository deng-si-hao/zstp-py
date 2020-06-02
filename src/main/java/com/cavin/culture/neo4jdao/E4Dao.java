package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jmodel.e4;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface E4Dao extends Neo4jRepository<e4,Long> {
    e4 findByName(@Param("name") String name);

    @Query("MATCH (n:e4) RETURN n.name;")
    List<String> findBodesByLabel();
}
