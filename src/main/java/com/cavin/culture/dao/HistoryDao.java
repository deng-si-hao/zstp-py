package com.cavin.culture.dao;

import com.cavin.culture.model.History;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryDao {

    List<History> getHistoriesByNameAndType(@Param("userName") String userName, @Param("historyType") String historyType);

    Integer insertHistory(History history);

    Integer deleteHistoryById(long historyId);

    List<History> getInputSuggestion(String userName);


}
