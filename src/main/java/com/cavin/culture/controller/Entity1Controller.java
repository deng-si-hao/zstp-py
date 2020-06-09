package com.cavin.culture.controller;

import com.cavin.culture.controller.InitializeData.InitializeNeoData;
import com.cavin.culture.neo4jRelationship.ContainsARelationship;
import com.cavin.culture.neo4jdao.*;
import com.cavin.culture.neo4jmodel.e1;
import com.cavin.culture.neo4jmodel.e2;
import com.cavin.culture.util.Neo4jUtil;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/test/neo4j")
public class Entity1Controller {

    @Autowired
    E1Dao e1Dao;
    @Autowired
    E2Dao e2Dao;
    @Autowired
    E3Dao e3Dao;
    @Autowired
    E4Dao e4Dao;

    @Autowired
    ContainsARelationshipDao containsARelationshipDao;
/*
    @Autowired
    ContainsBRelationship containsBRelationship;*/


    @RequestMapping("/get")
    public e1 GetE1ByName(String name){
        return e1Dao.findByName(name);
    }
    @RequestMapping("/gete1all")
    public Iterable<e1> findAll(){
        return e1Dao.findAll();
    }
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
    @RequestMapping("/getBNameByLabel")
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
    }

    //测试关系接口
/*    @RequestMapping("/ralationB")
    public Iterable<ContainsBRelationship> getByName(String name){
        return containsBRelationship.findByName(name);
    }*/
    @RequestMapping("/shortestPath")
    public Map<String, Object> getShortPath(String param1,String param2){
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match l=shortestPath(({name:'"+param1+"'})-[*]-({name:'"+param2+"'})) return l";
        //待返回的值，与cql return后的值顺序对应
        Set<Map<String ,Object>> nodeList = new HashSet<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        Neo4jUtil.RunCypher(cql,nodeList,edgeList);
        retMap.put("nodeList",nodeList);
        retMap.put("edgeList",edgeList);
        return retMap;
    }





}
