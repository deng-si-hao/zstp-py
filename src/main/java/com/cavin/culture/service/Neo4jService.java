package com.cavin.culture.service;

import com.alibaba.fastjson.JSON;
import com.cavin.culture.dao.GraphDao;
import com.cavin.culture.model.JsonMessage;
import com.cavin.culture.model.Neo4jEntity;
import com.cavin.culture.util.CSVUtil;
import com.cavin.culture.util.ExcelUtil;
import com.cavin.culture.util.Neo4jUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class Neo4jService {

    @Autowired
    private Neo4jUtil neo4jUtil;

    @Autowired
    private GraphDao graphDao;

    /**
    * 获取label标签
    *
    * */
    public List<Map<String,Object>> getLabelList(String creater){
        return graphDao.getLabelList(creater);
    }

    /**
    * 向mysql中添加label数据
    * 用于记录label的数量
    * */
    public int addGraphData(Map<String,Object> params){
       return graphDao.addGraphData(params);
    }

    /**
    * 根据名称查询mysql中的label名称
    * */
    public String getLabelByName(String label){
        return graphDao.getLabelByName(label);
    }

    /**
     * 创建label,默认创建一个新的节点,给节点附上默认属性
     *
     * @param domain
     */
    public void createLabel(String domain) {
        try {
            String cypherSql = String.format(
                    "create (n:`%s`{type:0,name:''}) return id(n)", domain);
            neo4jUtil.excuteCypherSql(cypherSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建单个节点
     *
     * @param domain
     * @param entity
     * @return
     */
    public HashMap<String, Object> createNode(String domain, Neo4jEntity entity) {
        HashMap<String, Object> rss = new HashMap<String, Object>();
        List<HashMap<String, Object>> graphNodeList = new ArrayList<HashMap<String, Object>>();
        try {
            if (entity.getUuid() != 0) {
                String sqlkeyval = neo4jUtil.getkeyvalCyphersql(entity);
                String cypherSql = String.format("match (n:`%s`) where id(n)=%s set %s return n", domain,
                        entity.getUuid(), sqlkeyval);
                graphNodeList = neo4jUtil.GetGraphNode(cypherSql);
            } else {
                entity.setColor("#ff4500");// 默认颜色
                entity.setR(30);// 默认半径
                String propertiesString = neo4jUtil.getFilterPropertiesJson(JSON.toJSONString(entity));
                String cypherSql = String.format("create (n:`%s` %s) return n", domain, propertiesString);
                graphNodeList = neo4jUtil.GetGraphNode(cypherSql);
            }
            if (graphNodeList.size() > 0) {
                rss = graphNodeList.get(0);
                return rss;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rss;
    }
    /**
     * 更新节点名称
     *
     * @param label
     * @param nodeid
     * @param nodename
     * @return 修改后的节点
     */
    public HashMap<String, Object> updatenodename(String label, String nodeid, String nodename) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        List<HashMap<String, Object>> graphNodeList = new ArrayList<HashMap<String, Object>>();
        try {
            String cypherSql = String.format("MATCH (n:`%s`) where id(n)=%s set n.name='%s' return n", label, nodeid,
                    nodename);
            graphNodeList = neo4jUtil.GetGraphNode(cypherSql);
            if (graphNodeList.size() > 0) {
                return graphNodeList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 添加关系
     *
     * @param domain
     *            领域
     * @param sourceid
     *            源节点id
     * @param targetid
     *            目标节点id
     * @param ship
     *            关系
     * @return
     */
    public HashMap<String, Object> createlink(String domain, long sourceid, long targetid, String ship) {
        HashMap<String, Object> rss = new HashMap<String, Object>();
        try {
            String cypherSql = String.format("MATCH (n:`%s`),(m:`%s`) WHERE id(n)=%s AND id(m) = %s "
                    + "CREATE (n)-[r:%s{name:'%s'}]->(m)" + "RETURN r", domain, domain, sourceid, targetid,ship,ship);
            List<HashMap<String, Object>> cypherResult = neo4jUtil.GetGraphRelationShip(cypherSql);
            if (cypherResult.size() > 0) {
                rss = cypherResult.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rss;
    }
    /**
     * 更新关系
     *
     * @param domain
     *            领域
     * @param shipid
     *            关系id
     * @param shipname
     *            关系名称
     * @return
     */
    public HashMap<String, Object> updateLink(String domain, long shipid, String shipname) {
        HashMap<String, Object> rss = new HashMap<String, Object>();
        try {
            String cypherSql = String.format("MATCH (n:`%s`) -[r]->(m) where id(r)=%s set r.name='%s' return r", domain,
                    shipid, shipname);
            //更新关系名称的同时更新关系
            //MATCH (n:aaa)-[r]->(m) where id(r)=812 set r.name = 'ooxx'
            //CREATE (n)-[r2:xxoo]->(m)
            //SET r2 = r
            //WITH r
            //DELETE r
            List<HashMap<String, Object>> cypherResult = neo4jUtil.GetGraphRelationShip(cypherSql);
            if (cypherResult.size() > 0) {
                rss = cypherResult.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rss;
    }

    /**
     * 删除节点(先删除关系再删除节点)
     *
     * @param domain
     * @param nodeid
     * @return
     */
    public List<HashMap<String, Object>> deletenode(String domain, long nodeid) {
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        try {
            String nSql = String.format("MATCH (n:`%s`)  where id(n)=%s return n", domain, nodeid);
            result = neo4jUtil.GetGraphNode(nSql);
            String rSql = String.format("MATCH (n:`%s`) <-[r]->(m) where id(n)=%s return r", domain, nodeid);
            neo4jUtil.GetGraphRelationShip(rSql);
            String deleteRelationSql = String.format("MATCH (n:`%s`) <-[r]->(m) where id(n)=%s delete r", domain, nodeid);
            neo4jUtil.excuteCypherSql(deleteRelationSql);
            String deleteNodeSql = String.format("MATCH (n:`%s`) where id(n)=%s delete n", domain, nodeid);
            neo4jUtil.excuteCypherSql(deleteNodeSql);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除关系
     *
     * @param domain
     * @param shipid
     */
    public void deleteLink(String domain, long shipid) {
        try {
            String cypherSql = String.format("MATCH (n:`%s`) -[r]->(m) where id(r)=%s delete r", domain, shipid);
            neo4jUtil.excuteCypherSql(cypherSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    *//**
    * 删除带有关系的节点
    *
    * *//*
    public List<HashMap<String, Object>> delNodeAndLink(String label, long nodeid){
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        try {
            String nSql = String.format("MATCH (n:`%s`)  where id(n)=%s return n", label, nodeid);
            result = neo4jUtil.GetGraphNode(nSql);
            String rSql = String.format("MATCH (n:`%s`) <-[r]->(m) where id(n)=%s return r", label, nodeid);
            neo4jUtil.GetGraphRelationShip(rSql);
            String deleteRelationSql = String.format("MATCH (n:`%s`) <-[r]->(m) where id(n)=%s delete r", label, nodeid);
            neo4jUtil.excuteCypherSql(deleteRelationSql);
            String deleteNodeSql = String.format("MATCH (n:`%s`) where id(n)=%s delete n", label, nodeid);
            neo4jUtil.excuteCypherSql(deleteNodeSql);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }*/

    /**
    * 删除label
    * */
    public void deleteLabel(String label){
        try {
            String rSql = String.format("MATCH (n:`%s`) -[r]-(m)  delete r", label);
            neo4jUtil.excuteCypherSql(rSql);
            String deleteNodeSql = String.format("MATCH (n:`%s`) delete n", label);
            neo4jUtil.excuteCypherSql(deleteNodeSql);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    /**
    * 删除mysql中的label记录
    * */
    public void delLabelInMysql(long labelid){
        graphDao.delLabelInMysql(labelid);
    }

    /**
    * 批量创建节点及关系
    * */
    public HashMap<String,Object> batchAddNode(String domain, String sourcename, String relation,
                                    String[] targetnames){
        HashMap<String,Object> res = new HashMap<>();
        List<HashMap<String, Object>> nodes = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> ships = new ArrayList<HashMap<String, Object>>();
        try {
            String cypherSqlFmt = "create (n:`%s` {name:'%s',color:'#ff4500',r:30}) return n";
            String cypherSql = String.format(cypherSqlFmt, domain, sourcename);// 概念实体
            List<HashMap<String, Object>> graphNodeList = neo4jUtil.GetGraphNode(cypherSql);
            if (graphNodeList.size() > 0) {
                HashMap<String, Object> sourceNode = graphNodeList.get(0);
                nodes.add(sourceNode);
                String sourceuuid = String.valueOf(sourceNode.get("uuid"));
                for (String tn : targetnames) {
                    String targetnodeSql = String.format(cypherSqlFmt, domain, tn);
                    List<HashMap<String, Object>> targetNodeList = neo4jUtil.GetGraphNode(targetnodeSql);
                    if (targetNodeList.size() > 0) {
                        HashMap<String, Object> targetNode = targetNodeList.get(0);
                        nodes.add(targetNode);
                        String targetuuid = String.valueOf(targetNode.get("uuid"));
                        String rSql = String.format(
                                "match(n:`%s`),(m:`%s`) where id(n)=%s and id(m)=%s create (n)-[r:RE {name:'%s'}]->(m) return r",
                                domain, domain, sourceuuid, targetuuid, relation);
                        List<HashMap<String, Object>> rshipList = neo4jUtil.GetGraphRelationShip(rSql);
                        ships.addAll(rshipList);
                    }

                }
            }
            res.put("nodes", nodes);
            res.put("ships", ships);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
    * 批量创建下级节点
    *
    * */
    public HashMap<String, Object> batchLowNode(String label, String sourceid, String[] targetnames, String relation){
        HashMap<String, Object> rss = new HashMap<String, Object>();
        List<HashMap<String, Object>> nodes = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> ships = new ArrayList<HashMap<String, Object>>();
        try {
            String cypherSqlFmt = "create (n:`%s`{name:'%s',color:'#ff4500',r:30}) return n";
            String cypherSql = String.format("match (n:`%s`) where id(n)=%s return n", label, sourceid);
            List<HashMap<String, Object>> sourcenodeList = neo4jUtil.GetGraphNode(cypherSql);
            if (sourcenodeList.size() > 0) {
                nodes.addAll(sourcenodeList);
                for (String tn : targetnames) {
                    String targetnodeSql = String.format(cypherSqlFmt, label, tn);
                    List<HashMap<String, Object>> targetNodeList = neo4jUtil.GetGraphNode(targetnodeSql);
                    if (targetNodeList.size() > 0) {
                        HashMap<String, Object> targetNode = targetNodeList.get(0);
                        nodes.add(targetNode);
                        String targetuuid = String.valueOf(targetNode.get("uuid"));
                        // 创建关系
                        String rSql = String.format(
                                "match(n:`%s`),(m:`%s`) where id(n)=%s and id(m)=%s create (n)-[r:RE {name:'%s'}]->(m) return r",
                                label, label, sourceid, targetuuid, relation);
                        List<HashMap<String, Object>> shipList = neo4jUtil.GetGraphRelationShip(rSql);
                        ships.addAll(shipList);
                    }
                }
            }
            rss.put("nodes", nodes);
            rss.put("ships", ships);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rss;
    }
    /**
    * 批量创建同级节点
    * */
    public List<HashMap<String, Object>> batchSameNode(String label, Integer entitytype, String[] sourcenames){
        List<HashMap<String, Object>> rss = new ArrayList<HashMap<String, Object>>();
        try {
            String cypherSqlFmt = "create (n:`%s`{name:'%s',color:'#ff4500',r:30}) return n";
            for (String tn : sourcenames) {
                String sourcenodeSql = String.format(cypherSqlFmt, label, tn, entitytype);
                List<HashMap<String, Object>> targetNodeList = neo4jUtil.GetGraphNode(sourcenodeSql);
                rss.addAll(targetNodeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rss;
    }
    /**
    * 导入的文件处理，csv/excl
    * */
    public List<Map<String, Object>> getFormatData(MultipartFile file) throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".csv")) {
                Workbook workbook = null;
                if (ExcelUtil.isExcel2007(fileName)) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                } else {
                    workbook = new HSSFWorkbook(file.getInputStream());
                }
                // 有多少个sheet
                int sheets = workbook.getNumberOfSheets();
                for (int i = 0; i < sheets; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    int rowSize = sheet.getPhysicalNumberOfRows();
                    for (int j = 0; j < rowSize; j++) {
                        Row row = sheet.getRow(j);
                        int cellSize = row.getPhysicalNumberOfCells();
                        if (cellSize != 3) continue; //只读取3列
                        row.getCell(0).setCellType(CellType.STRING);
                        Cell cell0 = row.getCell(0);//节点1
                        row.getCell(1).setCellType(CellType.STRING);
                        Cell cell1 = row.getCell(1);//节点2
                        row.getCell(2).setCellType(CellType.STRING);
                        Cell cell2 = row.getCell(2);//关系
                        if (null == cell0 || null == cell1 || null == cell2) {
                            continue;
                        }
                        String sourceNode = cell0.getStringCellValue();
                        String targetNode = cell1.getStringCellValue();
                        String relationShip = cell2.getStringCellValue();
                        if (StringUtils.isBlank(sourceNode) || StringUtils.isBlank(targetNode) || StringUtils.isBlank(relationShip))
                            continue;
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("sourcenode", sourceNode);
                        map.put("targetnode", targetNode);
                        map.put("relationship", relationShip);
                        mapList.add(map);
                    }
                }
            } else if (fileName.endsWith(".csv")) {
                List<List<String>> list = CSVUtil.readCsvFile(file);
                for (int i = 0; i < list.size(); i++) {
                    List<String> lst = list.get(i);
                    if (lst.size() != 3) continue;
                    String sourceNode = lst.get(0);
                    String targetNode = lst.get(1);
                    String relationShip = lst.get(2);
                    if (StringUtils.isBlank(sourceNode) || StringUtils.isBlank(targetNode) || StringUtils.isBlank(relationShip))
                        continue;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("sourcenode", sourceNode);
                    map.put("targetnode", targetNode);
                    map.put("relationship", relationShip);
                    mapList.add(map);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return mapList;
    }

    /**
    * 批量导入csv文件
    *
    * */
    public void batchInsertByCSV(String domain, String csvUrl, int status) {
        String loadNodeCypher1 = null;
        String loadNodeCypher2 = null;
        String addIndexCypher = null;
        addIndexCypher = " CREATE INDEX ON :" + domain + "(name);";
        loadNodeCypher1 = " USING PERIODIC COMMIT 500 LOAD CSV FROM '" + csvUrl + "' AS line " + " MERGE (:`" + domain
                + "` {name:line[0]});";
        loadNodeCypher2 = " USING PERIODIC COMMIT 500 LOAD CSV FROM '" + csvUrl + "' AS line " + " MERGE (:`" + domain
                + "` {name:line[1]});";
        // 拼接生产关系导入cypher
        String loadRelCypher = null;
        String type = "RE";
        loadRelCypher = " USING PERIODIC COMMIT 500 LOAD CSV FROM  '" + csvUrl + "' AS line " + " MATCH (m:`" + domain
                + "`),(n:`" + domain + "`) WHERE m.name=line[0] AND n.name=line[1] " + " MERGE (m)-[r:" + type + "]->(n) "
                + "	SET r.name=line[2];";
        neo4jUtil.excuteCypherSql(addIndexCypher);
        neo4jUtil.excuteCypherSql(loadNodeCypher1);
        neo4jUtil.excuteCypherSql(loadNodeCypher2);
        neo4jUtil.excuteCypherSql(loadRelCypher);
    }

    /**
    * 导出csv，获取图谱数据
    *
    * */
    public List<HashMap<String, Object>> GetGraphItem(String cypherSql) {
        List<HashMap<String, Object>> ents = new ArrayList<HashMap<String, Object>>();
        List<String> nodeids = new ArrayList<String>();
        List<String> shipids = new ArrayList<String>();
        try {
            StatementResult result = neo4jUtil.excuteCypherSql(cypherSql);
            if (result.hasNext()) {
                List<Record> records = result.list();
                for (Record recordItem : records) {
                    List<Pair<String, Value>> f = recordItem.fields();
                    HashMap<String, Object> rss = new HashMap<String, Object>();
                    for (Pair<String, Value> pair : f) {
                        String typeName = pair.value().type().name();
                        if (typeName.equals("NODE")) {
                            Node noe4jNode = pair.value().asNode();
                            String uuid = String.valueOf(noe4jNode.id());
                            if(!nodeids.contains(uuid)) {
                                Map<String, Object> map = noe4jNode.asMap();
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    rss.put(key, entry.getValue());
                                }
                                rss.put("uuid", uuid);
                            }
                        }else if (typeName.equals("RELATIONSHIP")) {
                            Relationship rship = pair.value().asRelationship();
                            String uuid = String.valueOf(rship.id());
                            if (!shipids.contains(uuid)) {
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
                            }
                        }else {
                            rss.put(pair.key(),pair.value().toString());
                        }
                    }
                    ents.add(rss);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ents;
    }

}
