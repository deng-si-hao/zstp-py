package com.cavin.culture.controller;

import com.auth0.jwt.interfaces.Claim;
import com.cavin.culture.config.WebMvcConfig;
import com.cavin.culture.model.JsonMessage;
import com.cavin.culture.model.User;
import com.cavin.culture.service.Neo4jService;
import com.cavin.culture.util.ExcelResolve;
import com.cavin.culture.util.JWTMEUtil;
import com.cavin.culture.util.JWTUtil;
import com.cavin.culture.util.Neo4jUtil;
import com.csvreader.CsvWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


@RestController
@RequestMapping(value = "/MapDisplay")
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class MapDisplayController {

    @Autowired
    private Neo4jUtil neo4jUtil;

    @Resource
    private Neo4jService neo4jService;

    @Resource
    private WebMvcConfig config;


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

    /**
    * 获取节点的所有关系类型
    *
    * */
    @RequestMapping("/getShipByNode")
    public List<String> getShipByNode(String nodeName){
        List<String> resultList = new ArrayList<>();
        String cql = String.format("match l = (n)-[r]-() where n.name='%s' return r",nodeName);
        resultList = neo4jService.getShipByNode(cql);
        return resultList;
    }


    /**
    * neo4j数据库excl导入
    * @path 文件路径
    *
    * */
    @RequestMapping("/insertData")
    public JsonMessage importNeo4jData(HttpServletRequest request,MultipartFile file){
/*        String token = null;
        String level = null;
        Map<String, Claim> tokenRes = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("access_token")){
                    token = cookie.getValue();
                    try {
                        tokenRes = JWTMEUtil.verifyToken(token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    level = tokenRes.get("level").asString();
                }
            }
        }
        if(level==null){
            return JsonMessage.error(400,"您没有访问权限");
        }
        if(level.equals(User.commander)){
            if(neo4jService.importData(file)){
                return JsonMessage.success();
            }else {
                return JsonMessage.error(500,"导入数据失败！");
            }

        }else {
            return JsonMessage.error(400,"您的权限不足，请联系管理员");
        }*/
        if(neo4jService.importData(file)){
            return JsonMessage.success();
        }else {
            return JsonMessage.error(500,"导入失败");
        }

        /*//获取提交文件名称
        String filename = file.getOriginalFilename();
        //创建关系的cql语句
        List<String> relationCql= new ArrayList<>();
        //创建用于去重得list
        List<String> delRepeatCql= new ArrayList<>();
        //装所有cql语句的list
        List<List<String>> allcql=new ArrayList<>();
        //读取excl文件

        Workbook wb = readExcel(filename);
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
        }*/

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
    @RequestMapping("/extensionNodes")
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

    /**
    * 导出neo4j数据为csv文件
    *
    * */
    @RequestMapping("/exportData")
    public void exportData(HttpServletRequest request, HttpServletResponse response){
        String cypher=null;
        String source = request.getParameter("source");
        String target = request.getParameter("target");
        String filePath = config.getLocation();
        String fileName = JWTUtil.getNewId() + ".csv";
        String fileUrl = filePath + File.separator + fileName;
        if(source!=null && target == null){
            cypher = String.format(
                    "MATCH (n) -[r]-(m) where n.name= '%s' return n.name as source,m.name as target,r.name as relation", source);
        }else if(source != null && target != null){
            cypher = String.format(
                    "MATCH (n) -[r]-(m) where n.name= '%s' and m.name='%s' return n.name as source,m.name as target,r.name as relation", source,target);
        }else {
            cypher = "MATCH (n) -[r]-(m) return n.name as source,m.name as target,r.name as relation";
        }
       List<HashMap<String, Object>> list = neo4jService.GetGraphItem(cypher);
        File file = new File(fileUrl);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("文件不存在，新建成功！");
            } else {
                System.out.println("文件存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CsvWriter csvWriter = new CsvWriter(fileUrl, ',', Charset.forName("UTF-8"));
        String[] header = { "source", "target", "relation" };
        try {
            csvWriter.writeRecord(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (HashMap<String, Object> hashMap : list) {
            int colSize = hashMap.size();
            String[] cntArr = new String[colSize];
            cntArr[0] = hashMap.get("source").toString().replace("\"", "");
            cntArr[1] = hashMap.get("target").toString().replace("\"", "");
            cntArr[2] = hashMap.get("relation").toString().replace("\"", "");
            try {
                csvWriter.writeRecord(cntArr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        csvWriter.close();
            if (file.exists()) {
                //response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    }

}
