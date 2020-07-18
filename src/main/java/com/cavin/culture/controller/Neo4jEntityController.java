package com.cavin.culture.controller;

import com.alibaba.fastjson.JSONObject;
import com.cavin.culture.model.JsonMessage;
import com.cavin.culture.model.Neo4jEntity;
import com.cavin.culture.service.Neo4jService;
import com.cavin.culture.util.CSVUtil;

import com.cavin.culture.config.WebMvcConfig;
import com.csvreader.CsvWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/neo4j")
public class Neo4jEntityController {

    @Resource
    private Neo4jService neo4jService;

    @Autowired
    private WebMvcConfig config;



    /**
    * 获取自己创建的label
    * */
    public JsonMessage getLabelList(HttpServletRequest request){
        JsonMessage result = new JsonMessage();
        HashMap<String,Object> res = new HashMap<>();
        try {
            List<Map<String, Object>> labelList = neo4jService.getLabelList("");
            res.put("lableList",labelList);
            result.setCode(200);
            result.setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("获取失败！");
        }
        return result;
    }

    /**
    * 创建一个空的label
    * 默认一个空白node，无关系
    * */
    @RequestMapping(value = "/createlabel")
    public JsonMessage createLabel(String domain) {
        JsonMessage result = new JsonMessage();
        try {
            if (!StringUtils.isBlank(domain)) {
                String label = neo4jService.getLabelByName(domain);
                if (StringUtils.isNotEmpty(label)) {
                    result.setCode(401);
                    result.setMessage("已存在的label，创建失败！");
                } else {
                    String name = "tc";//TODO 获取当前登录用户
                    Map<String, Object> maps = new HashMap<String, Object>();
                    maps.put("label", domain);
                    maps.put("nodeCount", 1);
                    maps.put("linkCount", 0);
                    maps.put("status", 1);
                    maps.put("creater", name);
                    neo4jService.addGraphData(maps);// 保存到mysql
                    neo4jService.createLabel(domain);// 保存到图数据
                    result.setCode(200);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("服务器错误");
        }
        return result;
    }

    /**
    * 在已存在的label中添加node
     *
     *
    * */
    @RequestMapping("/addnode")
    public JsonMessage addNode(Neo4jEntity entity, HttpServletRequest request){
       JsonMessage result = new JsonMessage();
        HashMap<String, Object> graphNode = new HashMap<String, Object>();
        try {
            String domain = request.getParameter("label");
            graphNode=neo4jService.createNode(domain, entity);
            if (graphNode!=null&&graphNode.size() > 0) {
                result.setCode(200);
                result.setData(graphNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("服务器错误");
        }
        return result;
    }

    /**
    * 修改node名称
    * @Param label
     * @Param nodeName
     * @Param nodeId
     *
    * */
    @RequestMapping("/updatenodename")
    public JsonMessage updateNodeName(String label,String nodeName,String nodeId){
        JsonMessage result = new JsonMessage();
        HashMap<String,Object> resultList =  new HashMap<>();
        try {
            if(StringUtils.isNotEmpty(label)){
                resultList = neo4jService.updatenodename(label,nodeId,nodeName);
                if(resultList.size()>0){
                    result.setCode(200);
                    result.setData(resultList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("更新出错！请检查");
        }
        return result;
    }

    /**
    * 创建单个关系
    *
    * */
    @RequestMapping("/createLink")
    public JsonMessage createLink(String label,long sourceId,long targetId,String shipName){
        JsonMessage result = new JsonMessage();
        try {
            HashMap<String, Object> cypherResult = neo4jService.createlink(label, sourceId, targetId, shipName);
            result.setCode(200);
            result.setData(cypherResult);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("创建失败！");
        }
        return result;
    }
    /**
    * 修改关系名称
    *
    * */
    @RequestMapping("/updateLink")
    public JsonMessage updateLink(String label,String newname,long shipid){
        JsonMessage result = new JsonMessage();
        try {
            HashMap<String,Object> cypherRes = neo4jService.updateLink(label,shipid,newname);
            result.setData(cypherRes);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("更新失败!");
        }
        return result;
    }

    /**
    * 删除关系
    *
    * */
    @RequestMapping("/delLink")
    public JsonMessage delLink(String label,long shipid){
        JsonMessage result = new JsonMessage();
        try {
            neo4jService.deleteLink(label,shipid);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("删除失败!");
        }
        return result;
    }

    /**
    * 删除node
    *
    * */
    @RequestMapping("/delNode")
    public JsonMessage delNode(String label,long nodeid){
        JsonMessage result = new JsonMessage();
        try {
            neo4jService.deletenode(label,nodeid);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("删除失败!");
        }
        return result;

    }
    /**
    * 删除label
    *
    * */
    @RequestMapping("/delLabel")
    public JsonMessage delLabel(String label,long labelid){
        //前端显示label的数据来源于mysql
        JsonMessage result = new JsonMessage();
        try {
            neo4jService.deleteLabel(label);
            neo4jService.delLabelInMysql(labelid);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("删除失败!");
        }
        return result;

    }
    /**
    * 批量创建node
    * */
    @RequestMapping("/batchAddNode")
    public JsonMessage batchAddNode(String label, String sourcename, String relation, String[] targetnames){
        JsonMessage result = new JsonMessage();
        HashMap<String, Object> res = new HashMap<String, Object>();
        try {
            res =  neo4jService.batchAddNode(label,sourcename,relation,targetnames);
            result.setCode(200);
            result.setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("创建失败!");
        }
        return result;
    }
    /**
    * 批量创建下级接节点
    * */
    @RequestMapping("/batchLowNode")
    public JsonMessage batchLowNode(String label, String sourceid, String[] targetnames, String relation){
        JsonMessage result = new JsonMessage();
        HashMap<String, Object> res = new HashMap<String, Object>();
        try {
            res =  neo4jService.batchLowNode(label,sourceid,targetnames,relation);
            result.setCode(200);
            result.setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("创建失败!");
        }
        return result;
    }
    /**
    * 批量创建同级节点
    * */
    @RequestMapping("/batchSameNode")
    public JsonMessage batchSameNode(String label, Integer entitytype, String[] sourcenames){
        JsonMessage result = new JsonMessage();
       HashMap<String, Object> rss = new HashMap<>();
        try {
            List<HashMap<String, Object>> res =  neo4jService.batchSameNode(label,entitytype,sourcenames);
            rss.put("result",res);
            result.setData(rss);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("创建失败!");
        }
        return result;
    }

    /**
    * CSV文件导入
    *
    * */
    @ResponseBody
    @RequestMapping(value = "/importgraph")
    public JSONObject importgraph(@RequestParam(value = "file", required = true) MultipartFile file,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject res = new JSONObject();
        if (file == null) {
            res.put("code", "500");
            res.put("msg", "请先选择有效的文件");
            return res;
        }
        // 领域不能为空
        String label = request.getParameter("domain");
        if (StringUtils.isBlank(label)) {
            res.put("code", "500");
            res.put("msg", "请先选择领域");
            return res;
        }
        List<Map<String, Object>> dataList = neo4jService.getFormatData(file);
        try {
            List<List<String>> list = new ArrayList<>();
            for (Map<String, Object> item : dataList) {
                List<String> lst = new ArrayList<>();
                lst.add(item.get("sourcenode").toString());
                lst.add(item.get("targetnode").toString());
                lst.add(item.get("relationship").toString());
                list.add(lst);
            }
            String savePath = config.getLocation();
            String filename = "tc" + System.currentTimeMillis() + ".csv";
            CSVUtil.createCsvFile(list, savePath,filename);
            String serverUrl=request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String csvUrl = "http://"+serverUrl+ "/download/" + filename;
            //String csvUrl = "https://neo4j.com/docs/cypher-manual/3.5/csv/artists.csv";
            neo4jService.batchInsertByCSV(label, csvUrl, 0);
            res.put("code", 200);
            res.put("message", "success!");
            return res;
        } catch (Exception e) {
            res.put("code", 500);
            res.put("message", "服务器错误!");
        }
        return res;
    }

    /**
    * 导出csv文件
    *
    * */
    @RequestMapping(value = "/exportgraph")
    public JSONObject exportgraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject res = new JSONObject();
        String label = request.getParameter("domain");
        String filePath = config.getLocation();
        String fileName = UUID.randomUUID() + ".csv";
        String fileUrl = filePath + File.separator + fileName;
        String cypher = String.format(
                "MATCH (n:%s) -[r]->(m:%s) return n.name as source,m.name as target,r.name as relation", label, label);
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
        csvWriter.writeRecord(header);
        for (HashMap<String, Object> hashMap : list) {
            int colSize = hashMap.size();
            String[] cntArr = new String[colSize];
            cntArr[0] = hashMap.get("source").toString().replace("\"", "");
            cntArr[1] = hashMap.get("target").toString().replace("\"", "");
            cntArr[2] = hashMap.get("relation").toString().replace("\"", "");
            try {
                csvWriter.writeRecord(cntArr);
            } catch (IOException e) {
                System.out.println("CSVUtil->createFile: 文件输出异常" + e.getMessage());
            }
        }
        csvWriter.close();
        String serverUrl=request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String csvUrl = serverUrl + "/kg/download/" + fileName;
        res.put("code", 200);
        res.put("csvurl", csvUrl);
        res.put("message", "success!");
        return res;

    }


    /**
    * 传入一个图，即nodeList和edgeList
    *
    * */
    public void addGraph(List<Map<String,Object>> nodeList,List<Map<String,Object>> edgeList){
        //TODO 处理node
        for(Map map:nodeList){
            String nodeId = (String) map.get("id");
            String label = (String) map.get("label");
            String name = (String) map.get("name");
        }

        //TODO 处理edge
        for(Map map:edgeList){
            String sourceType= (String) map.get("sourceLabel");
            String source = (String) map.get("source");
            String targetType= (String) map.get("targetLabel");
            String target = (String) map.get("target");
            String type = (String) map.get("type");
            String relationRight="match (from:"+sourceType+"{name:\"" +source+ "\"}),(to:"+targetType
                    +"{name:\""+target+"\"})  merge (from)-[r:"+type+"{name:\""+source+
                    "\",name:\""+target+"\"}]->(to)";

        }

    }

}
