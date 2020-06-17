package com.cavin.culture.util;

import com.alibaba.fastjson.JSONObject;
import com.github.jsonldjava.utils.Obj;
import org.apache.jena.atlas.json.JSON;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.omg.CORBA.MARSHAL;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Neo4jUtil {

    static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678!a"));
    private static Session session = driver.session();

    //Object转Map，用于处理数据
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }


    /*
    * 子图翻译
    * */
    public List<Map<String,Object>> SubGraph(List<Map<String,Object>> nodes, List<Map<String,Object>> link) throws IllegalAccessException {
        //定义一个数组用于存储结果集
        List<Map<String,Object>> result=new ArrayList<>();
        int i=0;//用于子图的index序号
        //用于存储nodes的单个实体
        Map<String,Object> new_dict=new HashMap<>();
        //起过渡作用
        List<Object> tran=new ArrayList<>();
        //用于将new_dict存储到result中
        Map<String,Object> transition=new HashMap<>();
        //用于处理list时
        Map<String,Object> node_dict=new HashMap<>();
        //处理nodes
        for(Map<String,Object> map:nodes){
                new_dict.put("index",i);
                new_dict.put("id",map.get("id"));
                new_dict.put("name",map.get("name"));
                if(map.get("image")!=null){
                    new_dict.put("image",map.get("image"));
                }else {
                    new_dict.put("image",null);
                }
                tran.add(new_dict);//暂存在一个list中，后面用于整合为一个key，value形式的map存在返回结果中
               node_dict.put((String) map.get("name"),new_dict);//将new_dict以key,value形式存在一个map中
               new_dict.clear();//清空new_dict,便于下次循环
        }
        transition.put("nodes",tran);//将所有的node以key,value的形式存在一个map中，后面将它放入返回的list中
        tran.clear();//tran作用结束，清空
        result.add(transition);
        transition.clear();//transition作用结束，清空
        //处理list
        //1.定义两个list用于存放分割后的links
        List<String> res=new ArrayList<>();
        List<String> res1=new ArrayList<>();
        for(Map<String,Object> mapa:link){
            String regx= JSONObject.toJSONString(mapa);
            Matcher m = Pattern.compile("[(](.*?)[)]").matcher(regx);
            while (m.find()){
                res.add(m.group());
            }
            String entity1=res.get(0);
            String entity2=res.get(1);
            String typeregx=regx.split("-")[1];
            Matcher m2=Pattern.compile("[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+").matcher(typeregx);
            while (m2.find()){
                res1.add(m2.group());
            }
            String type=res1.get(0);
            //用于取出存在node_dict中的map的数据
            Map<String,Object> map1= objectToMap(node_dict.get(entity1));
            Map<String,Object> map2= objectToMap(node_dict.get(entity2));
            new_dict.put("source",map1.get("index"));
            new_dict.put("target",map2.get("index"));
            new_dict.put("source_name",entity1);
            new_dict.put("target_name",entity2);
            new_dict.put("type",type);
            transition.put("links",new_dict);
            new_dict.clear();
        }
        result.add(transition);
        return result;
    }

    /**
     * cql 查询 返回节点和关系
     * @param nodeName 节点名称
     * @param cql 查询语句
     * @param nodeList 节点
     * @param edgeList 关系
     * @return List<Map<String,Object>>
     */
    public static <T> void RunCypher(String nodeName,String cql, List<Map<String,Object>> nodeList, Set<Map<String,Object>> edgeList) {
        try {
            int i=0;
            StatementResult result = session.run(cql);
            List<Record> list = result.list();
            for (Record r : list) {
                for (String index : r.keys()) {
                    Path path = r.get(index).asPath();
                    //节点
                    Iterable<Node> nodes = path.nodes();
                    for (Iterator iter = nodes.iterator(); iter.hasNext();) {
                        InternalNode nodeInter = (InternalNode) iter.next();
                        Map<String, Object> map = new HashMap<>();
                        //节点上设置的属性
                        map.putAll(nodeInter.asMap());
                        //外加一个固定属性
                        map.put("id", nodeInter.id());
                        map.put("index",i);
                        //node去重
                        for(Map<String,Object> node:nodeList){
                            if(node.get("id")!=null && node.get("id").equals(map.get("id"))){
                                map.clear();
                               break;
                            }
                        }
                        if(!map.isEmpty()){
                            nodeList.add(map);
                            i++;
                        }
                    }
                    //关系
                    Iterable<Relationship> edges = path.relationships();
                    for (Iterator iter = edges.iterator(); iter.hasNext(); ) {
                        InternalRelationship relationInter = (InternalRelationship) iter.next();
                        Map<String, Object> map = new HashMap<>();
                        map.putAll(relationInter.asMap());
                        //关系上设置的属性
                        map.put("id", relationInter.id());
                        for(Map<String,Object> t:nodeList){
                            if(t.get("id").equals(relationInter.startNodeId())){
                                map.put("source",t.get("index"));
                                map.put("source_name",t.get("name"));
                            }
                            if(t.get("id").equals(relationInter.endNodeId())){
                                map.put("target",t.get("index"));
                                map.put("target_name",t.get("name"));
                            }
                            map.put("type",relationInter.type());

                        }
//                        map.put("type",relationInter.type());
                        //判断一度关系查询和路径查询
                        if(nodeName==null){
                            edgeList.add(map);
                        }else {
                            if(map.get("source_name")!=null&&!map.get("source_name").equals(nodeName)){
                                map.clear();
                            }else {
                                edgeList.add(map);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * neo4j导入数据
    * */
    public static void importNeo4j(String cql){
        try {
            session.run(cql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导入信息错误！");
        }

    }

}
