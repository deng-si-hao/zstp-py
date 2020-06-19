package com.cavin.culture.controller;

import com.cavin.culture.controller.InitializeData.InitializeNeoData;
import com.cavin.culture.neo4jdao.E1Dao;
import com.cavin.culture.neo4jdao.E2Dao;
import com.cavin.culture.neo4jdao.E3Dao;
import com.cavin.culture.neo4jdao.E4Dao;
import com.cavin.culture.util.Neo4jUtil;
import com.cavin.culture.util.PythonUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping(value = "/MapDisplay")
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class MapDisplayController {

//    private static final String path="F:\\zhishitupu\\zstp\\src\\main\\resources\\static\\py\\neo4j2json_cons.py";
    static Resource resource= new ClassPathResource("static/excl/元器件筛选、DPA数据.xlsx");

    //初始化连接
    static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678!a"));
    private static Session session = driver.session();

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
        List<Map<String,Object>> res=new ArrayList<>();
//        List<String> res=new ArrayList<>();
        String cql="match l= (n:"+label+") return l";
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
                   res.add(map);
                }
            }
        }
        return res;
       /* switch (label){
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
        }*/

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
        List<Map<String ,Object>> nodeList = new ArrayList<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        Neo4jUtil.RunCypher(null,cql,nodeList,edgeList);
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
        List<Map<String ,Object>> nodeList = new ArrayList<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        Neo4jUtil.RunCypher(nodeName,cql,nodeList,edgeList);
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

    /**
    * neo4j数据库excl导入
    * @path 文件路径
    *
    * */
    @RequestMapping("/insertData")
    public void neo4jTest() throws IOException {
        String path = String.valueOf(resource.getFile());
        //创建实体的cql语句
        List<String> labelCql= new ArrayList<>();
        //创建关系的cql语句
        List<String> relationCql= new ArrayList<>();
        //创建用于去重得list
        List<String> weituoCql= new ArrayList<>();
        List<String> shiyanCql= new ArrayList<>();
        List<String> guanxiCql= new ArrayList<>();
        //装所有cql语句的list
        List<List<String>> allcql=new ArrayList<>();
        //读取excl文件
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(path));
        XSSFSheet xSheet = xwb.getSheetAt(0);
        for (int i = 2; i <=10; i++) {
            if (xSheet.getRow(i) == null) {
                continue;
            }
            String shiyanbianhao =  (xSheet.getRow(i)).getCell(1).toString();
            String weituodanwei =  (xSheet.getRow(i)).getCell(2).toString();
            String weituoshijian =  (xSheet.getRow(i)).getCell(3).toString();
            String suoshugongcheng =  (xSheet.getRow(i)).getCell(4).toString();
            String zhixingbiaozhun =  (xSheet.getRow(i)).getCell(5).toString();
            String yuanqijianleixing =  (xSheet.getRow(i)).getCell(6).toString();
            String yuanqijianmingcheng =  (xSheet.getRow(i)).getCell(7).toString();
            String xinghaoguige =  (xSheet.getRow(i)).getCell(8).toString();
            String fengzhuangcailiao =  (xSheet.getRow(i)).getCell(9).toString();
            String fengzhuangfangshi =  (xSheet.getRow(i)).getCell(10).toString();
            String fengzhuangguige =  (xSheet.getRow(i)).getCell(11).toString();
            String kekaoxingyuji =  (xSheet.getRow(i)).getCell(12).toString();
            String shengchanbaozheng =  (xSheet.getRow(i)).getCell(13).toString();
            String shengchanpici =  (xSheet.getRow(i)).getCell(14).toString();
            String shengchancangjia =  (xSheet.getRow(i)).getCell(15).toString();
            String guochanjinkou =  (xSheet.getRow(i)).getCell(16).toString();
            String shiyanleixing =  (xSheet.getRow(i)).getCell(17).toString();
            String songjianshuliang = (xSheet.getRow(i)).getCell(18).toString();
            String shiyandanwei = (xSheet.getRow(i)).getCell(19).toString();
            String shiyanjielun = (xSheet.getRow(i)).getCell(20).toString();
            String zhiliangdengji = (xSheet.getRow(i)).getCell(29).toString();
            String fengzhuangxingshi = (xSheet.getRow(i)).getCell(30).toString();
            String feiyong = (xSheet.getRow(i)).getCell(31).toString();
            String yijifenlei = (xSheet.getRow(i)).getCell(32).toString();
            String caigoubianhao = (xSheet.getRow(i)).getCell(36).toString();

            //创建实体
            //元器件
            String yqjcql = "create (:co{name:\"" + yuanqijianmingcheng + "\",label:\"co\",type:\""+yuanqijianleixing+"\",specification:\""+xinghaoguige+"\"," +
                    "batch:\""+shengchanpici+"\",manufacturer:\""+shengchancangjia+"\",rank:\""+zhiliangdengji+"\",encapsulation:\""+fengzhuangxingshi+"\",fenlei:\""+yijifenlei+"\"" +
                    ",caigoubianhao:\""+caigoubianhao+"\"})";
            //委托单位
            String wtdwcql="create (:entrust{name:\""+weituodanwei+"\",label:\"entrust\"})";
            //试验单位
            String sydw="create (:unit{name:\""+shiyandanwei+"\",label:\"unit\"})";
            //委托关系
//            String recql = "create (from:co{name:\"" + yuanqijianmingcheng + "\"})-[r:entrust{name:\"" + weituodanwei + "\"}]->(to:entrust{name:\"" + weituodanwei + "\"})";
            String recql="match (from:entrust{name:\"" + weituodanwei + "\"}),(to:co{name:\""+yuanqijianmingcheng+"\"})  merge (from)-[r:entrust{name:\""+weituodanwei+"\",name:\""+yuanqijianmingcheng+"\"}]->(to)";
            String recql1="match (from:co{name:\"" + yuanqijianmingcheng + "\"}),(to:entrust{name:\""+weituodanwei+"\"})  merge (from)-[r:entrust{name:\""+yuanqijianmingcheng+"\",name:\""+weituodanwei+"\"}]->(to)";

            //实验关系
//            String syrecql="create (from:co{name:\"" + yuanqijianmingcheng + "\"})-[r:test{name:\"" + shiyandanwei + "\"}]->(to:unit{name:\"" + shiyandanwei + "\"})";
            String syrecql="match (from:unit{name:\"" + shiyandanwei + "\"}),(to:co{name:\""+yuanqijianmingcheng+"\"})  merge (from)-[r:test{name:\""+shiyandanwei+"\",name:\""+yuanqijianmingcheng+"\"}]->(to)";
            String syrecql1="match (from:co{name:\"" + yuanqijianmingcheng + "\"}),(to:unit{name:\""+shiyandanwei+"\"})  merge (from)-[r:test{name:\""+yuanqijianmingcheng+"\",name:\""+shiyandanwei+"\"}]->(to)";

            //数据去重
            weituoCql.add(wtdwcql);
            shiyanCql.add(sydw);
            guanxiCql.add(syrecql);
            labelCql.add(yqjcql);
            relationCql.add(recql);
            relationCql.add(recql1);
            guanxiCql.add(syrecql1);
        }
        allcql.add(delRepeat(weituoCql));
        allcql.add(delRepeat(shiyanCql));
        allcql.add(delRepeat(labelCql));
        allcql.add(delRepeat(guanxiCql));
        allcql.add(delRepeat(relationCql));
        //执行cql
        for(List<String> l:allcql){
            for(String s:l){
                session.run(s);
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


}
