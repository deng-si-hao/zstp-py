package com.cavin.culture.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GraphDao {

    List<Map<String,Object>> getLabelList(@Param("creater")String creater);

    int addGraphData(@Param("params") Map<String,Object> params);//TODO

    String getLabelByName(@Param("label") String label);

    void delLabelInMysql(@Param("id") long id);
}
