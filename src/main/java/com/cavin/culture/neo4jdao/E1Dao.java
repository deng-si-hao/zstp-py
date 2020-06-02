package com.cavin.culture.neo4jdao;

import com.cavin.culture.neo4jmodel.e1;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface E1Dao extends Neo4jRepository<e1,Long> {

    e1 findByName(@Param("name") String name);

//    List<e1> findByLabel(@Param("label")String label);


    @Query("MATCH (n:e1) RETURN n.name;")
    List<String> findBodesByLabel();

    //获取所有实体标签
    @Query("MATCH (n) RETURN n")
    List<Map<String,Object>> findAllNodes();

}
