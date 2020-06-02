package com.cavin.culture.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.jena.atlas.json.JSON;
import org.omg.CORBA.MARSHAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Neo4jUtil {
    /*
    * 子图翻译
    * */
    public List<Map<String,Object>> SubGraph(List<Map<String,Object>> nodes, List<Map<String,Object>> link){
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
            Map<String,Object> map1= (Map)node_dict.get(entity1);
            Map<String,Object> map2= (Map)node_dict.get(entity2);
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
}
