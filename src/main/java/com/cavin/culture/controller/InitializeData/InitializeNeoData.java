package com.cavin.culture.controller.InitializeData;

import com.cavin.culture.neo4jdao.E1Dao;
import com.cavin.culture.util.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.util.*;



@Component
public class InitializeNeoData {

    //所有数据
    public List<Map<String,Object>> Neo4jData=new ArrayList<>();
    //所有实体类标签
    public static List<Object> label=new ArrayList<>();

    @Autowired
    private E1Dao e1Dao;



    @PostConstruct
    public void init() throws IllegalAccessException {
        List<Object> labelAll=new ArrayList<>();
        //初始加载Neo4j的所有数据到缓存区
        List<Map<String,Object>> result=e1Dao.findAllNodes();
        for(Map<String,Object> map:result){
            for(String str:map.keySet()){
                Neo4jData.add(Neo4jUtil.objectToMap(map.get(str)));
            }
        }
        //对实体标签去重
        for(Map<String,Object> map:Neo4jData) {
            labelAll.add(map.get("label"));
        }
        for(int i=0; i<labelAll.size(); i++){
            Object str = labelAll.get(i);  //获取传入集合对象的每一个元素
            if(!label.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
                label.add(str);
            }
        }
        labelAll.clear();
    }

    @PreDestroy
    public void destroy(){
        System.out.println("系统运行结束！");
    }

    @Scheduled(cron = "0 0 0/2 * * ?")//cron表达式语法[秒] [分] [小时] [日] [月] [周] [年]
    public void twoHourTryOnce() throws IllegalAccessException {
        //每2小时执行一次缓存
        init();
    }

}
