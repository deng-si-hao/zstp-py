package com.cavin.culture.controller.InitializeData;

import com.cavin.culture.neo4jdao.E1Dao;
import com.cavin.culture.util.Neo4jUtil;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.util.*;



@Component
public class InitializeNeoData {
    
    //所有实体类标签
    public static List<Object> label=new ArrayList<>();

    //初始化连接
    static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678!a"));
    private static Session session = driver.session();

    @Autowired
    private E1Dao e1Dao;



    @PostConstruct
    public void init() throws IllegalAccessException {
        List<Object> labelAll=new ArrayList<>();
        List<Map<String,Object>> res = new ArrayList<>();
        String cql="match l=(n) return l";
        StatementResult result = session.run(cql);
        List<Record> list = result.list();
        for (Record r : list) {
            for (String index : r.keys()) {
                Path path = r.get(index).asPath();
                //节点
                Iterable<Node> nodes = path.nodes();
                for (Iterator iter = nodes.iterator(); iter.hasNext();) {
                    InternalNode nodeInter = (InternalNode) iter.next();
                    Map<String,Object> map=new HashMap<>();
                    //节点上设置的属性
                    map.putAll(nodeInter.asMap());
                    map.put("label",nodeInter.get("label"));
                    res.add(map);
                }
            }
        }
        //对实体标签去重
        for(Map<String,Object> map:res) {
            labelAll.add(map.get("label"));
        }
        for(int i=0; i<labelAll.size(); i++){
            Object str =labelAll.get(i);  //获取传入集合对象的每一个元素
            String one=str.toString();
            String two=one.substring(1,one.length()-1);
            if(!label.contains(two)){   //查看新集合中是否有指定的元素，如果没有则加入
                label.add(two);
            }
        }
        labelAll.clear();


/*        List<Object> labelAll=new ArrayList<>();
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
        labelAll.clear();*/
    }

    @PreDestroy
    public void destroy(){
        System.out.println("系统运行结束！");
    }

    @Scheduled(cron = "0 0 0/2 * * ?")//cron表达式语法[秒] [分] [小时] [日] [月] [周] [年]
    public void twoHourTryOnce() throws IllegalAccessException {
        //每2小时执行一次缓存
        init();
        System.out.println("刷新了系统数据！！！");
    }

}
