package com.cavin.culture.controller;

import com.cavin.culture.controller.InitializeData.InitializeNeoData;
import com.cavin.culture.neo4jdao.E1Dao;
import com.cavin.culture.neo4jdao.E2Dao;
import com.cavin.culture.neo4jdao.E3Dao;
import com.cavin.culture.neo4jdao.E4Dao;
import com.cavin.culture.neo4jmodel.e1;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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


    @RequestMapping("/get")
    public e1 GetE1ByName(String name){
        return e1Dao.findByName(name);
    }
    @RequestMapping("/all")
    public List<Map<String, Object>> GetAll() throws IllegalAccessException {
      /*  result=baseDao.findAllNodes();
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
        }*/
        return InitializeNeoData.Neo4jData;
    }

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






}
