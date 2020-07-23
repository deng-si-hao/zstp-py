package com.cavin.culture.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.jena.ext.com.google.common.collect.HashMultimap;
import org.apache.jena.ext.com.google.common.collect.Multimap;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Neo4jUtil {

    @Autowired
    private Driver neo4jDriver;

    /*static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678!a"));*/


    static Resource resourceOther=new ClassPathResource("static/excl/自选输出表.xlsx");

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

    /**
    * 执行cypher的通用方法
    *
    * */
    public StatementResult excuteCypherSql(String cypherSql) {
        StatementResult result = null;
        try (Session session = neo4jDriver.session()) {
            System.out.println(cypherSql);
            result = session.run(cypherSql);
        } catch (Exception e) {
            throw e;
        }
        return result;
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
    public <T> void RunCypher(String nodeName,String cql, List<Map<String,Object>> nodeList, Set<Map<String,Object>> edgeList) {
        try {
            StatementResult result = excuteCypherSql(cql);
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
                        map.put("labels",nodeInter.labels());
                        //node去重
                        for(Map<String,Object> node:nodeList){
                            if(node.get("id")!=null && node.get("id").equals(map.get("id"))){
                                map.clear();
                               break;
                            }
                        }
                        if(!map.isEmpty()){
                            nodeList.add(map);
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
                        map.put("source",relationInter.startNodeId());
                        map.put("target",relationInter.endNodeId());
                        map.put("type",relationInter.type());

//                        for(Map<String,Object> t:nodeList){
//                            if(t.get("id").equals(relationInter.startNodeId())){
//                                map.put("source",t.get("id"));
//                                map.put("source_name",t.get("name"));
//                            }
//                            if(t.get("id").equals(relationInter.endNodeId())){
//                                map.put("target",t.get("id"));
//                                map.put("target_name",t.get("name"));
//                            }
//                            map.put("type",relationInter.type());
//
//                        }
//                        map.put("type",relationInter.type());
                        //判断一度关系查询和路径查询
                        if(nodeName==null){
                            edgeList.add(map);
                        }else {
//                            if(map.get("source")!=null&&!map.get("source").equals(nodeName)){
//                                map.clear();
//                            }else {
                                edgeList.add(map);
//                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> GetGraphNodeAndShip(String cypherSql) {
        HashMap<String, Object> mo = new HashMap<String, Object>();
        try {
            StatementResult result = excuteCypherSql(cypherSql);
            if (result.hasNext()) {
                List<Record> records = result.list();
                List<HashMap<String, Object>> ents = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> ships = new ArrayList<HashMap<String, Object>>();
                List<String> uuids = new ArrayList<String>();
                List<String> shipids = new ArrayList<String>();
                for (Record recordItem : records) {
                    List<Pair<String, Value>> f = recordItem.fields();
                    for (Pair<String, Value> pair : f) {
                        HashMap<String, Object> rships = new HashMap<String, Object>();
                        HashMap<String, Object> rss = new HashMap<String, Object>();
                        String typeName = pair.value().type().name();
                        if (typeName.equals("NULL")) {
                            continue;
                        } else if (typeName.equals("NODE")) {
                            Node noe4jNode = pair.value().asNode();
                            Map<String, Object> map = noe4jNode.asMap();
                            String uuid = String.valueOf(noe4jNode.id());
                            if (!uuids.contains(uuid)) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    rss.put(key, entry.getValue());
                                }
                                rss.put("id", uuid);
                                uuids.add(uuid);
                            }
                            if (rss != null && !rss.isEmpty()) {
                                ents.add(rss);
                            }
                        } else if (typeName.equals("RELATIONSHIP")) {
                            Relationship rship = pair.value().asRelationship();
                            String uuid = String.valueOf(rship.id());
                            if (!shipids.contains(uuid)) {
                                String sourceid = String.valueOf(rship.startNodeId());
                                String targetid = String.valueOf(rship.endNodeId());
                                Map<String, Object> map = rship.asMap();
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    rships.put(key, entry.getValue());
                                }
                                rships.put("id", uuid);
                                rships.put("source", sourceid);
                                rships.put("target", targetid);
                                shipids.add(uuid);
                                if (rships != null && !rships.isEmpty()) {
                                    ships.add(rships);
                                }
                            }

                        } else if (typeName.equals("PATH")) {
                            Path path = pair.value().asPath();

//                            //节点
//                            Iterable<Node> nodes = path.nodes();
//                            for (Iterator iter = nodes.iterator(); iter.hasNext();) {
//                                InternalNode nodeInter = (InternalNode) iter.next();
//                                Map<String, Object> map = new HashMap<>();
//                                //节点上设置的属性
//                                map.putAll(nodeInter.asMap());
//                                //外加一个固定属性
//                                map.put("id", nodeInter.id());
//                                map.put("labels",nodeInter.labels());
//                            }

//                            Map<String, Object> startNodemap = path.start().asMap();
//                            String startNodeuuid = String.valueOf(path.start().id());
//                            if (!uuids.contains(startNodeuuid)) {
//                                rss=new HashMap<String, Object>();
//                                for (Map.Entry<String, Object> entry : startNodemap.entrySet()) {
//                                    String key = entry.getKey();
//                                    rss.put(key, entry.getValue());
//                                }
//                                rss.put("id", startNodeuuid);
//                                uuids.add(startNodeuuid);
//                                if (rss != null && !rss.isEmpty()) {
//                                    ents.add(rss);
//                                }
//                            }
//
//                            Map<String, Object> endNodemap = path.end().asMap();
//                            String endNodeuuid = String.valueOf(path.end().id());
//                            if (!uuids.contains(endNodeuuid)) {
//                                rss=new HashMap<String, Object>();
//                                for (Map.Entry<String, Object> entry : endNodemap.entrySet()) {
//                                    String key = entry.getKey();
//                                    rss.put(key, entry.getValue());
//                                }
//                                rss.put("id", endNodeuuid);
//                                uuids.add(endNodeuuid);
//                                if (rss != null && !rss.isEmpty()) {
//                                    ents.add(rss);
//                                }
//                            }
                            Iterator<Node> allNodes = path.nodes().iterator();
                            while (allNodes.hasNext()) {
                                Node next = allNodes.next();
                                String uuid = String.valueOf(next.id());
                                if (!uuids.contains(uuid)) {
                                    rss=new HashMap<String, Object>();
                                    Map<String, Object> map = next.asMap();
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        String key = entry.getKey();
                                        rss.put(key, entry.getValue());
                                    }
                                    rss.put("id", uuid);
                                    rss.put("labels",next.labels());
                                    uuids.add(uuid);
                                    if (rss != null && !rss.isEmpty()) {
                                        ents.add(rss);
                                    }
                                }
                            }
                            Iterator<Relationship> reships = path.relationships().iterator();
                            while (reships.hasNext()) {
                                Relationship next = reships.next();
                                String uuid = String.valueOf(next.id());
                                if (!shipids.contains(uuid)) {
                                    rships=new HashMap<String, Object>();
                                    String sourceid = String.valueOf(next.startNodeId());
                                    String targetid = String.valueOf(next.endNodeId());
                                    Map<String, Object> map = next.asMap();
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        String key = entry.getKey();
                                        rships.put(key, entry.getValue());
                                    }
                                    rships.put("id", uuid);
                                    rships.put("source", sourceid);
                                    rships.put("target", targetid);
                                    shipids.add(uuid);
                                    if (rships != null && !rships.isEmpty()) {
                                        ships.add(rships);
                                    }
                                }
                            }
                        } else if (typeName.contains("LIST")) {
                            Iterable<Value> val=pair.value().values();
                            Value next = val.iterator().next();
                            String type=next.type().name();
                            if (type.equals("RELATIONSHIP")) {
                                Relationship rship = next.asRelationship();
                                String uuid = String.valueOf(rship.id());
                                if (!shipids.contains(uuid)) {
                                    String sourceid = String.valueOf(rship.startNodeId());
                                    String targetid = String.valueOf(rship.endNodeId());
                                    Map<String, Object> map = rship.asMap();
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        String key = entry.getKey();
                                        rships.put(key, entry.getValue());
                                    }
                                    rships.put("id", uuid);
                                    rships.put("source", sourceid);
                                    rships.put("target", targetid);
                                    shipids.add(uuid);
                                    if (rships != null && !rships.isEmpty()) {
                                        ships.add(rships);
                                    }
                                }
                            }
                        } else if (typeName.contains("MAP")) {
                            rss.put(pair.key(), pair.value().asMap());
                        } else {
                            rss.put(pair.key(), pair.value().toString());
                            if (rss != null && !rss.isEmpty()) {
                                ents.add(rss);
                            }
                        }

                    }
                }
                mo.put("nodes", ents);
                mo.put("links", ships);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return mo;
    }


    /**
    * neo4j导入数据
    * */
    public void importNeo4j(String cql){
        try {
            Session session = neo4jDriver.session();
            session.run(cql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导入信息错误！");
        }
    }
    /**
    * 导入自主输出表数据
    *
    * */
    public void importData() throws IOException {
        String path = String.valueOf(resourceOther.getFile());

        //创建实体的cql语句
//        List<String> labelCql= new ArrayList<>();
        //创建关系的cql语句
//        List<String> relationCql= new ArrayList<>();
        //创建用于去重得list
        List<String> weituoCql= new ArrayList<>();
        //装所有cql语句的list
        List<List<String>> allcql=new ArrayList<>();
        //读取excl文件
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(path));
        XSSFSheet xSheet = xwb.getSheetAt(0);
        for (int i = 1; i <=xSheet.getLastRowNum(); i++) {
            if (xSheet.getRow(i) == null) {
                continue;
            }
            String chanpinfenlei =  (xSheet.getRow(i)).getCell(2).toString();
            String chanpinmingcheng =  (xSheet.getRow(i)).getCell(3).toString();
            String xilie =  (xSheet.getRow(i)).getCell(4).toString();
            String xinghaoguige =  (xSheet.getRow(i)).getCell(5).toString();
            String shengchanchangjia =  (xSheet.getRow(i)).getCell(6).toString();
            String gonghuozhuangtai =  (xSheet.getRow(i)).getCell(7).toString();
            String zhiliangdengji =  (xSheet.getRow(i)).getCell(8).toString();
            String yujidengji =  (xSheet.getRow(i)).getCell(9).toString();
            String fengzhuangxingshi =  (xSheet.getRow(i)).getCell(10).toString();
            String wendufanwei =  (xSheet.getRow(i)).getCell(11).toString();
            String xingnengcanshu =  (xSheet.getRow(i)).getCell(12).toString();
            String xuanyongdengji =  (xSheet.getRow(i)).getCell(13).toString();
            String anquanyansedengji =  (xSheet.getRow(i)).getCell(14).toString();
            String tidaixinxi =  (xSheet.getRow(i)).getCell(15).toString();
            String chuangjianshijian =  (xSheet.getRow(i)).getCell(16).toString();

            //创建实体
            //元器件
            String yqjcql = "create (:EEPROM{name:\"" + xilie + "\",label:\""+chanpinfenlei+"\",系列:\""+xilie+"\",型号规格:\""+xinghaoguige+"\"," +
                    "封装形式:\""+fengzhuangxingshi+"\",工作温度范围:\""+wendufanwei+"\",主要性能参数:\""+xingnengcanshu+"\",封装形式:\""+fengzhuangxingshi+"\"})";
            //委托单位
            String wtdwcql="create (:生产厂家{name:\""+shengchanchangjia+"\",label:\"生产厂家\"})";
            //试验单位
//            String sydw="create (:unit{name:\""+shiyandanwei+"\",label:\"unit\"})";
            //委托关系
//            String recql = "create (from:co{name:\"" + yuanqijianmingcheng + "\"})-[r:entrust{name:\"" + weituodanwei + "\"}]->(to:entrust{name:\"" + weituodanwei + "\"})";
//            String recql="match (from:entrust{name:\"" + weituodanwei + "\"}),(to:co{name:\""+yuanqijianmingcheng+"\"})  merge (from)-[r:entrust{name:\""+weituodanwei+"\",name:\""+yuanqijianmingcheng+"\"}]->(to)";
//            String recql1="match (from:co{name:\"" + yuanqijianmingcheng + "\"}),(to:entrust{name:\""+weituodanwei+"\"})  merge (from)-[r:entrust{name:\""+yuanqijianmingcheng+"\",name:\""+weituodanwei+"\"}]->(to)";
            //生产关系
//            String syrecql="create (from:co{name:\"" + yuanqijianmingcheng + "\"})-[r:test{name:\"" + shiyandanwei + "\"}]->(to:unit{name:\"" + shiyandanwei + "\"})";
            String syrecql="match (from:生产厂家{name:\"" + shengchanchangjia + "\"}),(to:EEPROM{name:\""+xilie+"\"})  create (from)-[r:"+gonghuozhuangtai+"{name:\""+shengchanchangjia+"\",name:\""+xilie+"\"}]->(to)";
            String syrecql1="match (from:EEPROM{name:\"" + xilie + "\"}),(to:生产厂家{name:\""+shengchanchangjia+"\"})  create (from)-[r:"+gonghuozhuangtai+"{name:\""+xilie+"\",name:\""+shengchanchangjia+"\"}]->(to)";

            //数据去重
            weituoCql.add(yqjcql);
            weituoCql.add(wtdwcql);
            weituoCql.add(syrecql);
            weituoCql.add(syrecql1);
        }
        allcql.add(delRepeat(weituoCql));
        //执行cql
        for(List<String> l:allcql){
            for(String s:l){
                excuteCypherSql(s);
            }
        }
    }
    // 遍历后判断赋给另一个list集合，保持原来顺序
    public static List<String> delRepeat(List<String> list) {
        List<String> listNew = new ArrayList<String>();
        for (String str : list) {
            if (!listNew.contains(str)) {
                listNew.add(str);
            }
        }
        return listNew ;
    }

    /**
    * 查询label下所有node
    *
    * */
    public List<Map<String,Object>> nodeByLabel(String label){
        //定义查询节点的cypher语句
        String cypher = String.format("MATCH l = (n:`%s`) return l", label);
        List<Map<String,Object>> res=new ArrayList<>();
        StatementResult result =  excuteCypherSql(cypher);
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
                    res.add(map);
                }
            }
        }
        return res;
    }

    /**
    * 获取所有的label
    *
    * */
    public List<Object> init(){
        List<Object> label=new ArrayList<>();
        List<Object> labelAll = new ArrayList<>();
        List<Map<String, Object>> res = new ArrayList<>();
        String cql = "match l=(n) return l";
        StatementResult result =excuteCypherSql(cql);
        List<Record> list = result.list();
        for (Record r : list) {
            for (String index : r.keys()) {
                Path path = r.get(index).asPath();
                //节点
                Iterable<Node> nodes = path.nodes();
                for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
                    InternalNode nodeInter = (InternalNode) iter.next();
                    Map<String, Object> map = new HashMap<>();
                    //节点上设置的属性
                    map.putAll(nodeInter.asMap());
                    map.put("label", nodeInter.get("label"));
                    res.add(map);
                }
            }
        }
        //对实体标签去重
        for (Map<String, Object> map : res) {
            labelAll.add(map.get("label"));
        }
        for (int i = 0; i < labelAll.size(); i++) {
            Object str = labelAll.get(i);  //获取传入集合对象的每一个元素
            String one = str.toString();
            String two = one.substring(1, one.length() - 1);
            if (!label.contains(two)) {   //查看新集合中是否有指定的元素，如果没有则加入
                label.add(two);
            }
        }
        labelAll.clear();
        return label;
    }


    public String getFilterPropertiesJson(String jsonStr) {
        String propertiesString = jsonStr.replaceAll("\"(\\w+)\"(\\s*:\\s*)", "$1$2"); // 去掉key的引号
        return propertiesString;
    }

    public <T>String getkeyvalCyphersql(T obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> sqlList=new ArrayList<String>();
        // 得到类对象
        Class userCla = obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            Class type = f.getType();

            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                if(val==null) {
                    val="";
                }
                String sql="";
                String key=f.getName();
                System.out.println("key:"+key+"type:"+type);
                if ( val instanceof   Integer ){
                    // 得到此属性的值
                    map.put(key, val);// 设置键值
                    sql="n."+key+"="+val;
                }
                else if ( val instanceof   String[] ){
                    //如果为true则强转成String数组
                    String [] arr = ( String[] ) val ;
                    String v="";
                    for ( int j = 0 ; j < arr.length ; j++ ){
                        arr[j]="'"+ arr[j]+"'";
                    }
                    v=String.join(",", arr);
                    sql="n."+key+"=["+val+"]";
                }
                else if (val instanceof List){
                    //如果为true则强转成String数组
                    List<String> arr = ( ArrayList<String> ) val ;
                    List<String> aa=new ArrayList<String>();
                    String v="";
                    for (String s : arr) {
                        s="'"+ s+"'";
                        aa.add(s);
                    }
                    v=String.join(",", aa);
                    sql="n."+key+"=["+v+"]";
                }
                else {
                    // 得到此属性的值
                    map.put(key, val);// 设置键值
                    sql="n."+key+"='"+val+"'";
                }

                sqlList.add(sql);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String finasql=String.join(",",sqlList);
        System.out.println("单个对象的所有键值==反射==" + map.toString());
        return finasql;
    }
    //TODO 得根据实际情况修改typename判断
    public List<HashMap<String, Object>> GetGraphNode(String cypherSql) {
        List<HashMap<String, Object>> ents = new ArrayList<HashMap<String, Object>>();
        try {
            StatementResult result = excuteCypherSql(cypherSql);
            if (result.hasNext()) {
                List<Record> records = result.list();
                for (Record recordItem : records) {
                    List<Pair<String, Value>> f = recordItem.fields();
                    for (Pair<String, Value> pair : f) {
                        HashMap<String, Object> rss = new HashMap<String, Object>();
                        String typeName = pair.value().type().name();
                        if (typeName.equals("NODE")) {
                            Node noe4jNode = pair.value().asNode();
                            String uuid = String.valueOf(noe4jNode.id());
                            Map<String, Object> map = noe4jNode.asMap();
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                String key = entry.getKey();
                                rss.put(key, entry.getValue());
                            }
                            rss.put("uuid", uuid);
                            ents.add(rss);
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ents;
    }
    //创建关系调用
    public List<HashMap<String, Object>> GetGraphRelationShip(String cypherSql) {
        List<HashMap<String, Object>> ents = new ArrayList<HashMap<String, Object>>();
        try {
            StatementResult result = excuteCypherSql(cypherSql);
            if (result.hasNext()) {
                List<Record> records = result.list();
                for (Record recordItem : records) {
                    List<Pair<String, Value>> f = recordItem.fields();
                    for (Pair<String, Value> pair : f) {
                        HashMap<String, Object> rss = new HashMap<String, Object>();
                        String typeName = pair.value().type().name();
                        if (typeName.equals("RELATIONSHIP")) {
                            Relationship rship = pair.value().asRelationship();
                            String uuid = String.valueOf(rship.id());
                            String sourceid = String.valueOf(rship.startNodeId());
                            String targetid = String.valueOf(rship.endNodeId());
                            Map<String, Object> map = rship.asMap();
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                String key = entry.getKey();
                                rss.put(key, entry.getValue());
                            }
                            rss.put("uuid", uuid);
                            rss.put("sourceid", sourceid);
                            rss.put("targetid", targetid);
                            ents.add(rss);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ents;
    }

}
