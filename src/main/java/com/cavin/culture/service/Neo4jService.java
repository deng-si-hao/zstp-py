package com.cavin.culture.service;

import com.alibaba.fastjson.JSON;
import com.cavin.culture.model.Neo4jEntity;
import com.cavin.culture.util.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Neo4jService {

    @Autowired
    private Neo4jUtil neo4jUtil;

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
                    + "CREATE (n)-[r:RE{name:'%s'}]->(m)" + "RETURN r", domain, domain, sourceid, targetid, ship);
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

}
