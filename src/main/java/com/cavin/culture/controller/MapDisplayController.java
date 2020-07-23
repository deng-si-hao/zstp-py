package com.cavin.culture.controller;

import com.cavin.culture.util.Neo4jUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@RestController
@RequestMapping(value = "/MapDisplay")
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class MapDisplayController {
    static Resource resource= new ClassPathResource("static/excl/ZhiLiangKaPian.xls");
/*
//    private static final String path="F:\\zhishitupu\\zstp\\src\\main\\resources\\static\\py\\neo4j2json_cons.py";



    //初始化连接
    static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678!a"));
    private static Session session = driver.session();
*/

    @Autowired
    private Neo4jUtil neo4jUtil;

    //创建库
/*    @RequestMapping(value = "/createDb")
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
    }*/

    //获取全部实体标签
    @RequestMapping(value = "/getLabel")
    public List<Object> getAllLabel(){
        return neo4jUtil.init();
    }

    //获取标签下所有实体
    @RequestMapping(value = "/getEntityByLabel")
    public List<?> getEntityByLabel(String label){
        return neo4jUtil.nodeByLabel(label);
    }

/*    //获取一度关系
    @RequestMapping(value = "/getkgR1")
    public String getKgR1(String node, String label) throws IOException {

        String method="--getkgR1";
        String result= PythonUtil.oneParam(String.valueOf(resource.getFile()),method,node);

        return result;
    }*/
/*    //获取最短路径
    @RequestMapping(value = "/getKgShortestPath")
    public String getKgShortestPath(String node1Name, String node2Name) throws IOException{
        String method="--getkgShortestPath";
        String res=PythonUtil.twoParam(String.valueOf(resource.getFile()),method,node1Name,node2Name);
        return res;
    }*/

    /**
    * 获取最短路径（java实现）
    * */
    @RequestMapping("/getShortestPath")
    public Map<String, Object> getShortPath(String node1Name,String node2Name){
        HashMap<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match l=shortestPath(({name:'"+node1Name+"'})-[*]-({name:'"+node2Name+"'})) return l";
        //待返回的值，与cql return后的值顺序对应
//        List<Map<String ,Object>> nodeList = new ArrayList<>();
//        Set<Map<String ,Object>> edgeList = new HashSet<>();
//        neo4jUtil.RunCypher(null,cql,nodeList,edgeList);
        retMap = neo4jUtil.GetGraphNodeAndShip(cql);
//        retMap.put("nodes",nodeList);
//        retMap.put("links",edgeList);
        return retMap;
    }


    /**
    * 子图查询（java）
    * */
    @RequestMapping("/subGraph")
    public Map<String, Object> getSubGraph(String nodeName){
//        HashMap<String, Object> ress = new HashMap<>();
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match l = (n)-[]-(m) where n.name='"+nodeName+"' return l;";
//        String cql = "match l=(n)-[]-(m)-[]-(j) where n.name='"+nodeName+"' return l;";
        //待返回的值，与cql return后的值顺序对应
        List<Map<String ,Object>> nodeList = new ArrayList<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        neo4jUtil.RunCypher(nodeName,cql,nodeList,edgeList);
//        retMap = neo4jUtil.GetGraphNodeAndShip(cql);
        retMap.put("nodes",nodeList);
        retMap.put("links",edgeList);
        return retMap;
    }
/*    //获取全图
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
    }*/

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
        List<String> delRepeatCql= new ArrayList<>();
        //装所有cql语句的list
        List<List<String>> allcql=new ArrayList<>();
        //读取excl文件

        Workbook wb = readExcel(path);
        Sheet sheet = wb.getSheetAt(0);
//        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(path));
//        XSSFSheet sheet = xwb.getSheetAt(0);
        for (int i = 3; i < sheet.getLastRowNum()-1; i++) {
            if (sheet.getRow(i) == null) {
                continue;
            }
//            column+i= (sheet.getRow(i)).getCell(1).toString();
            for(int j = 0;j < sheet.getRow(i).getLastCellNum();j++){
                delRepeatCql.add((sheet.getRow(i)).getCell(j).toString());
            }
            //取表头作为label，添加各项数据
            String zlwt = "create (n:"+(sheet.getRow(0)).getCell(1).toString()+"{name:\""+delRepeatCql.get(1)+"\"}) return n";
            String glbg = "create (n:"+(sheet.getRow(0)).getCell(2).toString()+"{name:\""+delRepeatCql.get(2)+"\"}) return n";
            String wtbj = "create (n:"+(sheet.getRow(0)).getCell(3).toString()+"{name:\""+delRepeatCql.get(3)+"\"}) return n";
            String ycms = "create (n:"+(sheet.getRow(0)).getCell(4).toString()+"{name:\""+delRepeatCql.get(4)+"\"}) return n";
            String xhdh = "create (n:"+(sheet.getRow(0)).getCell(5).toString()+"{name:\""+delRepeatCql.get(5)+"\"}) return n";
            String xtzy = "create (n:"+(sheet.getRow(0)).getCell(6).toString()+"{name:\""+delRepeatCql.get(6)+"\"}) return n";
            String cp = "create (n:"+(sheet.getRow(0)).getCell(7).toString()+"{name:\""+delRepeatCql.get(7)+"\"}) return n";
            String gzdwbj = "create (n:"+(sheet.getRow(0)).getCell(12).toString()+"{name:\""+delRepeatCql.get(12)+"\"}) return n";
            String zrdw = "create (n:"+(sheet.getRow(0)).getCell(22).toString()+"{name:\""+delRepeatCql.get(22)+"\"}) return n";

            //添加对应关系
            String xtxhzcgx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                    + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(6).toString(),
                    (sheet.getRow(0)).getCell(5).toString(), delRepeatCql.get(6),
                    delRepeatCql.get(5),"系统_型号组成关系","系统_型号组成关系");
            String cpxtzggx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(7).toString(),
                    (sheet.getRow(0)).getCell(6).toString(), delRepeatCql.get(7),
                    delRepeatCql.get(6),"产品_系统组成关系","产品_系统组成关系");
            String xhwtfsgx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(5).toString(),
                    (sheet.getRow(0)).getCell(1).toString(), delRepeatCql.get(5),
                    delRepeatCql.get(1),"型号_问题发生关系","型号_问题发生关系");
            String cpwtfsgx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(7).toString(),
                    (sheet.getRow(0)).getCell(1).toString(), delRepeatCql.get(7),
                    delRepeatCql.get(1),"产品_问题发生关系","产品_问题发生关系");
            String xhjyfsgx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(1).toString(),
                    (sheet.getRow(0)).getCell(5).toString(), delRepeatCql.get(1),
                    delRepeatCql.get(5),"型号举一反三关系","型号举一反三关系");
            String xtjyfsgx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(1).toString(),
                    (sheet.getRow(0)).getCell(6).toString(), delRepeatCql.get(1),
                    delRepeatCql.get(6),"系统举一反三关系","系统举一反三关系");
            String cpjyfsgx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s'"
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(1).toString(),
                    (sheet.getRow(0)).getCell(7).toString(), delRepeatCql.get(1),
                    delRepeatCql.get(7),"产品举一反三关系","产品举一反三关系");
            String zrgx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(22).toString(),
                    (sheet.getRow(0)).getCell(1).toString(), delRepeatCql.get(22),
                    delRepeatCql.get(1),"责任关系","责任关系");
            String gygx = String.format("MATCH (n:`%s`),(m:`%s`) WHERE n.name='%s' AND m.name = '%s' "
                            + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", (sheet.getRow(0)).getCell(22).toString(),
                    (sheet.getRow(0)).getCell(7).toString(), delRepeatCql.get(22),
                    delRepeatCql.get(7),"供应关系","供应关系");


            labelCql.add(zlwt);
            labelCql.add(glbg);
            labelCql.add(wtbj);
            labelCql.add(ycms);
            labelCql.add(xhdh);
            labelCql.add(xtzy);
            labelCql.add(cp);
            labelCql.add(gzdwbj);
            labelCql.add(zrdw);
            relationCql.add(xtxhzcgx);
            relationCql.add(cpxtzggx);
            relationCql.add(xhwtfsgx);
            relationCql.add(cpwtfsgx);
            relationCql.add(xhjyfsgx);
            relationCql.add(xtjyfsgx);
            relationCql.add(cpjyfsgx);
            relationCql.add(zrgx);
            relationCql.add(gygx);
        }
        allcql.add(delRepeat(labelCql));
        allcql.add(delRepeat(relationCql));
        //执行cql
        for(List<String> l:allcql){
            for(String s:l){
               neo4jUtil.excuteCypherSql(s);
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
    //判断文件格式
    private static Workbook readExcel(String filePath){
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));

        try {
            InputStream is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
    * 导入自选输出表
    *
    * */
    @RequestMapping("/importData")
    public void importData() throws IOException {
        try {
            neo4jUtil.importData();
            System.out.println("导入数据成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
    * 按关系扩展下一个实体
    *
    * */
    @RequestMapping("extensionNodes")
    public Map<String,Object> OpenRelation(String node,String edge){
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match l = (n)-[r:"+edge+"]-(m) where n.name='"+node+"'  return l;";
        //待返回的值，与cql return后的值顺序对应
        List<Map<String ,Object>> nodeList = new ArrayList<>();
        Set<Map<String ,Object>> edgeList = new HashSet<>();
        neo4jUtil.RunCypher(node,cql,nodeList,edgeList);
        retMap.put("nodes",nodeList);
        retMap.put("links",edgeList);
        return retMap;
    }
}
