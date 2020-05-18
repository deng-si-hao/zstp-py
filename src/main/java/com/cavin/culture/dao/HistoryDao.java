package com.cavin.culture.dao;

import com.cavin.culture.model.History;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryDao {

    List<History> getHistoriesByNameAndType(String userName, String historyScope);

    Integer insertHistory(History history);

    Integer deleteHistoryById(Integer historyId);


}
