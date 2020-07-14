package com.cavin.culture.controller;

import com.cavin.culture.util.Neo4jUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/test/neo4j")
public class Entity1Controller {

/*    @Autowired
    E1Dao e1Dao;
    @Autowired
    E2Dao e2Dao;
    @Autowired
    E3Dao e3Dao;
    @Autowired
    E4Dao e4Dao;

    @Autowired
    ContainsARelationshipDao containsARelationshipDao;*/
/*
    @Autowired
    ContainsBRelationship containsBRelationship;*/


/*    @RequestMapping("/get")
    public e1 GetE1ByName(String name){
        return e1Dao.findByName(name);
    }
    @RequestMapping("/gete1all")
    public Iterable<e1> findAll(){
        return e1Dao.findAll();
    }*/
   /* @RequestMapping("/all")
    public List<Map<String, Object>> GetAll() throws IllegalAccessException {
      *//*  result=baseDao.findAllNodes();
        for(Map<String,Object> map:result){
            System.out.println("keyset****="+map.keySet());
            for(String res:map.keySet()){
               Map<String, Object> model= objectToMap(map.get(res));
                System.out.println(model.get("label"));
            }
        }
        List<Map<String,Object>> result=InitializeNeoData.Neo4jData;
        for(Map<String,Object> map:result) {
            System.out.println("keySet:::"+map.keySet());
            for (String res : map.keySet()) {
                System.out.println("###"+map);
                Map<String, Object> model = objectToMap(map.get(res));
                System.out.println(model.get("label"));
            }
            System.out.println("label:::"+map.get("label"));
        }*//*
        return InitializeNeoData.Neo4jData;
    }*/

/*    @RequestMapping("getNameByLabel")
    public List<String> getNameByLabel(){
        List<String> res=e1Dao.findBodesByLabel();
        System.out.println(">>>>>>>>"+res);
        return res;
    }*/

    /*
    *
    * 会有内存溢出异常/修改为只返回name
    * */
/*    @RequestMapping("/getBNameByLabel")
    public List<?> findBodesByLabel(String label){
        List<?> result=null;
       switch (label){
           case "e1":
               result=e1Dao.findBodesByLabel();
               break;
           case "e2":
               result=e2Dao.findBodesByLabel();
               break;
           case "e3":
               result=e3Dao.findBodesByLabel();
               break;
           case "e4":
               result=e4Dao.findBodesByLabel();
               break;
           default:
               result=null;
       }
        System.out.println("*******************"+result);
        return result;
    }

    @RequestMapping("/getTuByName")
    public e1 getTuByName(String name){
        e1 res= e1Dao.findByName(name);
        System.out.println(res);
        return res;
    }

    @RequestMapping("/ContainsARelationship")
    public Iterable<ContainsARelationship> findByName(String name){
        return containsARelationshipDao.findAll();
    }

    @RequestMapping("/findrelationAll")
    public e2 findReslation(String name){
        e2 res=e2Dao.findByName(name);
        return res;
    }

    @RequestMapping("/findgetContainsARelationship")
    public Iterable<ContainsARelationship> findgetContainsARelationship(String name){
        return containsARelationshipDao.findByname(name);
    }*/

    //测试关系接口
/*    @RequestMapping("/ralationB")
    public Iterable<ContainsBRelationship> getByName(String name){
        return containsBRelationship.findByName(name);
    }*/
    /*@RequestMapping("/shortestPath")
    public Map<String, Object> getShortPath(String param1,String param2){
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match l=shortestPath(({name:'"+param1+"'})-[*]-({name:'"+param2+"'})) return l";
        //待返回的值，与cql return后的值顺序对应
        List<Map<String ,Object>> nodeList = new ArrayList<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        Neo4jUtil.RunCypher(null,cql,nodeList,edgeList);
        retMap.put("nodeList",nodeList);
        retMap.put("edgeList",edgeList);
        return retMap;
    }*/

/*    @RequestMapping("/insertNeo4j")
    public void insertNeo4j(){
        //productionRelation
        String clearcql="";
        String cql="load csv from \"file:///csvf/conpents.csv\" as line  create (:components{type:line[1],label:\"components\", name:line[2],specification:line[3],batch:line[4],count:line[5],level:line[6],package:line[7],cost:line[8],classification:line[9]})";
        String cql1="load csv from \"file:///csvf/manufacturer.csv\" as line  create (:manufacturer{name:line[1],label:\"manufacturer\", type:line[2]})";
        String cql2="load csv from \"file:///csvf/unit.csv\" as line  create (:unit{name:line[1],label:\"unit\"})";
        String cqlRelationPro="Load csv FROM \"file:///csvf/productionRelation.csv\" as line  match (from:components{name:toString(line[1])}),(to:manufacturer{name:toString(line[2])})  merge (from)-[r:production{name:toString(line[2]),name:toString(line[1])}]->(to)";
        String cqlRelationScreen="Load csv FROM \"file:///csvf/screen.csv\" as line  match (from:unit{name:toString(line[1])}),(to:components{name:toString(line[2])})  merge (from)-[r:screen{name:toString(line[2]),name:toString(line[1])}]->(to)";
        List<String> cqlList=new ArrayList<>();
        cqlList.add(cql);
        cqlList.add(cql1);
        cqlList.add(cql2);
        cqlList.add(cqlRelationPro);
        cqlList.add(cqlRelationScreen);
        for(String s:cqlList){
            Neo4jUtil.importNeo4j(s);
        }

    }*/

/*    public void insertNode(GeneralNode generalNode){
        //TODO 多个属性的node插入语句
        String cql="create (:"+generalNode.getLabel()+"{name:\""+generalNode.getName()+"\",label:\""+generalNode.getLabel()+"\"})";

    }

    public void insertRelation(Relationship relationship){
        //TODO 插入关系的cql语句 （相互指向）双线
        String relationLeft="match (from:"+relationship.getStartType()+"{name:\"" + relationship.getStart() + "\"})," +
                "(to:"+relationship.getEndType()+"{name:\""+relationship.getEnd()+"\"})  merge (from)-[r:"+relationship.getRelationType()+"" +
                "{name:\""+relationship.getStart()+"\",name:\""+relationship.getEnd()+"\"}]->(to)";
        String relationRight="match (from:"+relationship.getEndType()+"{name:\"" + relationship.getEnd() + "\"}),(to:"+relationship.getStartType()
                +"{name:\""+relationship.getStart()+"\"})  merge (from)-[r:"+relationship.getRelationType()+"{name:\""+relationship.getEnd()+
                "\",name:\""+relationship.getStart()+"\"}]->(to)";
    }*/
    /**
    * 传入一个图，即nodeList和edgeList
    *
    * */
    public void addGraph(List<Map<String,Object>> nodeList,List<Map<String,Object>> edgeList){
        //TODO 处理node
        for(Map map:nodeList){
            String nodeId = (String) map.get("id");
            String label = (String) map.get("label");
            String name = (String) map.get("name");
        }

        //TODO 处理edge
        for(Map map:edgeList){
            String sourceType= (String) map.get("sourceLabel");
            String source = (String) map.get("source");
            String targetType= (String) map.get("targetLabel");
            String target = (String) map.get("target");
            String type = (String) map.get("type");
            String relationRight="match (from:"+sourceType+"{name:\"" +source+ "\"}),(to:"+targetType
                    +"{name:\""+target+"\"})  merge (from)-[r:"+type+"{name:\""+source+
                    "\",name:\""+target+"\"}]->(to)";

        }

    }

}
