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
    static Resource resource= new ClassPathResource("static/py/neo4j2json_cons.py");

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
    public void neo4jTest() throws IOException {
        String path = "F:\\0工作\\1知识图谱\\元器件知识图谱\\元器件筛选、DPA数据.xlsx";
        //创建实体的cql语句
        List<String> labelCql= new ArrayList<>();
        //创建关系的cql语句
        List<String> relationCql= new ArrayList<>();
        //读取excl文件
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(path));
        XSSFSheet xSheet = xwb.getSheetAt(0);
        for (int i = 2; i <=xSheet.getLastRowNum(); i++) {
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
            String fengzhuangguige =  (xSheet.getRow(i)).getCell(111).toString();
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
            String lacql = "create (:co{name:\"" + yuanqijianmingcheng + "\",label:\"co\"})";
            //创建关系
            String recql = "create (from:co{name:\"" + yuanqijianmingcheng + "\"})-[:" + weituodanwei + "]->(to:co{name:\"" + weituodanwei + "\"})";

            labelCql.add(lacql);
            relationCql.add(recql);
        }
        //执行添加实体
        for(String s:labelCql){
            session.run(s);
        }
        //执行添加关系
        for(String s:relationCql){
            session.run(s);
        }
    }
}
