package com.cavin.culture.service;

import com.alibaba.fastjson.JSON;
import com.cavin.culture.dao.GraphDao;
import com.cavin.culture.model.JsonMessage;
import com.cavin.culture.model.Neo4jEntity;
import com.cavin.culture.util.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
