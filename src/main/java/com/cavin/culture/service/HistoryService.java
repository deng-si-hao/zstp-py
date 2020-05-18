package com.cavin.culture.service;

import com.cavin.culture.dao.HistoryDao;
import com.cavin.culture.model.History;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class HistoryService {
    @Resource
    private HistoryDao historyDao;

    public List<History> getHistoriesByNameAndType(String userName, String historyType) {
        return historyDao.getHistoriesByNameAndType(userName, historyType);
    }


    public Integer insertHistory(History history) {
        return historyDao.insertHistory(history);
    }


    public Integer deleteHistoryById(Integer historyId) {
        return historyDao.deleteHistoryById(historyId);
    }
}
