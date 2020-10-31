package com.cavin.culture.service;

import com.cavin.culture.dao.HistoryDao;
import com.cavin.culture.model.History;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {
    @Resource
    private HistoryDao historyDao;

    public List<History> getHistoriesByNameAndType(String userName, String historyType) {
        List<History> histories = historyDao.getHistoriesByNameAndType(userName, historyType);
        return histories;
    }


    public Integer insertHistory(History history) {
        return historyDao.insertHistory(history);
    }


    public Integer deleteHistoryById(long historyId) {
        return historyDao.deleteHistoryById(historyId);
    }

    public List<String> getInputSuggestion(String queryString, String userName) {
        List<String> list = new ArrayList<>();
        List<History> histories = historyDao.getInputSuggestion(userName);
        for (History h : histories) {
            list.add(h.getHistorySubject());
        }
        return list;
    }
}
