package com.cavin.culture.controller;

import com.alibaba.fastjson.JSONObject;
import com.cavin.culture.model.PythonModel;
import com.cavin.culture.util.PythonUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/MapDisplay")
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class MapDisplayController {

//    private static final String path="F:\\zhishitupu\\zstp\\src\\main\\resources\\static\\py\\neo4j2json_cons.py";
    static Resource resource= new ClassPathResource("static/py/neo4j2json_cons.py");

    //创建库
    @RequestMapping(value = "/createDb")
    public String createDb() throws IOException {
        String method="--cons";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method);
        return PythonUtil.noParam(pythonModel);
    }

    //获取全部实体标签
    @RequestMapping(value = "/getLabel")
    public String getAllLabel() throws IOException{
        String method="--getlabel";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method);
        String result=PythonUtil.noParam(pythonModel);
        return JSONObject.toJSONString(result);
    }

    //获取标签下所有实体
    @RequestMapping(value = "/getEntityByLabel")
    public String getEntityByLabel(String label) throws IOException{
        String method="--getentitybylabel";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,label);
       /* String result=PythonUtil.oneParam(pythonModel);
        result = new String(result.getBytes("UTF-8"), "UTF-8");*/
        return PythonUtil.oneParam(pythonModel);
    }

    //获取一度关系
    @RequestMapping(value = "/getkgR1")
    public String getKgR1(String node) throws IOException{
        String method="--getkgR1";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,node);
        String result= PythonUtil.oneParam(pythonModel);
        return PythonUtil.oneParam(pythonModel);
    }
    //获取最短路径
    @RequestMapping(value = "/getKgShortestPath")
    public String getKgShortestPath(String param1, String param2) throws IOException{
        String method="--getkgShortestPath";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,param1,param2);
        return PythonUtil.oneParam(pythonModel);
    }
    //获取全图
    @RequestMapping(value = "/getalldata")
    public String getAllData() throws IOException{
        String method="--getalldata";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method);
        return PythonUtil.oneParam(pythonModel);
    }
    //查询子图
    @RequestMapping(value = "/searchSubKg")
    public String searchSubKg(String param) throws IOException{
        String method="--searchsubkg";
        PythonModel pythonModel = new PythonModel(String.valueOf(resource.getFile()),method,param);
        return PythonUtil.oneParam(pythonModel);
    }
}
