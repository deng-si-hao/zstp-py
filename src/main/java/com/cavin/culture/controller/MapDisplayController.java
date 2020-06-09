package com.cavin.culture.controller;

import com.cavin.culture.controller.InitializeData.InitializeNeoData;
import com.cavin.culture.neo4jdao.E1Dao;
import com.cavin.culture.neo4jdao.E2Dao;
import com.cavin.culture.neo4jdao.E3Dao;
import com.cavin.culture.neo4jdao.E4Dao;
import com.cavin.culture.util.Neo4jUtil;
import com.cavin.culture.util.PythonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping(value = "/MapDisplay")
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class MapDisplayController {

//    private static final String path="F:\\zhishitupu\\zstp\\src\\main\\resources\\static\\py\\neo4j2json_cons.py";
    static Resource resource= new ClassPathResource("static/py/neo4j2json_cons.py");

    @Autowired
    private E1Dao e1Dao;

    @Autowired
    private E2Dao e2Dao;

    @Autowired
    private E3Dao e3Dao;

    @Autowired
    private E4Dao e4Dao;


    //创建库
    @RequestMapping(value = "/createDb")
    public String createDb(){
        String method="--cons";
//        PythonModel pythonModel = null;
        try {
//            pythonModel = new PythonModel(String.valueOf(resource.getFile()),method);
            return PythonUtil.noParam(String.valueOf(resource.getFile()),method);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    //获取全部实体标签
    @RequestMapping(value = "/getLabel")
    public List<Object> getAllLabel(){
        return InitializeNeoData.label;
    }

    //获取标签下所有实体
    @RequestMapping(value = "/getEntityByLabel")
    public List<?> getEntityByLabel(String label) throws IOException{
/*        String method="--getentitybylabel";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,label);
        return PythonUtil.oneParam(pythonModel);*/
        List<Map<String,Object>> result=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        List<String> res=new ArrayList<>();
        switch (label){
            case "e1":
//                result.add("label":"e1","name":e1Dao.findBodesByLabel())
                   res= e1Dao.findBodesByLabel();
                   map.put("name",res) ;
                   map.put("label",label);
                   result.add(map);
                break;
            case "e2":
//                result=e2Dao.findBodesByLabel();
                res= e2Dao.findBodesByLabel();
                map.put("name",res) ;
                map.put("label",label);
                result.add(map);
                break;
            case "e3":
//                result=e3Dao.findBodesByLabel();
                res= e3Dao.findBodesByLabel();
                map.put("name",res) ;
                map.put("label",label);
                result.add(map);
                break;
            case "e4":
//                result=e4Dao.findBodesByLabel();
                res= e4Dao.findBodesByLabel();
                map.put("name",res) ;
                map.put("label",label);
                result.add(map);
                break;
            default:
                result=null;
        }
        return result;
    }

    //获取一度关系
    @RequestMapping(value = "/getkgR1")
    public String getKgR1(String node, String label) throws IOException {

        String method="--getkgR1";
//        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,node);
        String result= PythonUtil.oneParam(String.valueOf(resource.getFile()),method,node);

//        System.out.println(">>>>>>"+result);
        return result;
/*        List<Map<String,Object>> result=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
//        List<String> res=new ArrayList<>();
        switch (label){
            case "e1":
                map.put("value",e1Dao.findByName(node));
                break;
            case "e2":
                map.put("value",e2Dao.findByName(node));
//                result=e2Dao.findByName(node);
                break;
            case "e3":
                map.put("value",e3Dao.findByName(node));
//                result=e3Dao.findByName(node);
                break;
            case "e4":
                map.put("value",e4Dao.findByName(node));
//                result=e4Dao.findByName(node);
                break;
            default:
                map.put("value","");
        }
        return map;*/
    }
    //获取最短路径
    @RequestMapping(value = "/getKgShortestPath")
    public String getKgShortestPath(String node1Name, String node2Name) throws IOException{
        String method="--getkgShortestPath";
//        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,node1Name,node2Name);
        String res=PythonUtil.twoParam(String.valueOf(resource.getFile()),method,node1Name,node2Name);
//        System.out.println(">>>>>>"+res);
        return res;
    }

    /**
    * 获取最短路径（java实现）
    * */
    @RequestMapping("/getShortestPath")
    public Map<String, Object> getShortPath(String node1Name,String node2Name){
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match l=shortestPath(({name:'"+node1Name+"'})-[*]-({name:'"+node2Name+"'})) return l";
        //待返回的值，与cql return后的值顺序对应
        Set<Map<String ,Object>> nodeList = new HashSet<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        Neo4jUtil.RunCypher(cql,nodeList,edgeList);
        retMap.put("nodes",nodeList);
        retMap.put("links",edgeList);
        return retMap;
    }

    /**
    * 子图查询（java）
    * */
    @RequestMapping("/subGraph")
    public Map<String, Object> getSubGraph(String nodeName){
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match l = (n)-[]-(m) where n.name='"+nodeName+"' return l;";
        //待返回的值，与cql return后的值顺序对应
        Set<Map<String ,Object>> nodeList = new HashSet<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        Neo4jUtil.RunCypher(cql,nodeList,edgeList);
        retMap.put("nodes",nodeList);
        retMap.put("links",edgeList);
        return retMap;
    }
    //获取全图
    @RequestMapping(value = "/getalldata")
    public String getAllData() throws IOException{
        String method="--getalldata";
//        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method);
        return PythonUtil.noParam(String.valueOf(resource.getFile()),method);
    }
    //查询子图
    @RequestMapping(value = "/searchSubKg")
    public String searchSubKg(String param) throws IOException{
        String method="--searchsubkg";
//        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,param);
        return PythonUtil.oneParam(String.valueOf(resource.getFile()),method,param);
    }
}
