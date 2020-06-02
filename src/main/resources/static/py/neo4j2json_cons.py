#!/usr/bin/env python
# -*- coding: utf-8 -*-
import py2neo
from py2neo import Graph,Node, Relationship,Subgraph,NodeMatcher,RelationshipMatch
import os,sys
import json
import jieba

class KG_constructor:
    # 初始化，neo4j连接
    # def __init__(self):
    #     self.g = Graph(
    #         host="192.168.50.90",
    #         http_port=7687,
    #         user="neo4j",
    #         password="111111")

    def __init__(self):
        self.g = Graph(
            host="localhost",
            http_port=7687,
            user="neo4j",
            password="12345678!a")

        # self.num_limit = 200000
        # self.g.delete_all()

        self.Nodematcher = NodeMatcher(self.g)
        self.Relationmatcher = RelationshipMatch(self.g)

        # 全图数据
        self.graph_data = None
        self.search_all_graph()
        self.node,self.relation = {},{}
        self.__process_graph_data__()

    def constructor(self):
        # 初始清空
        self.g.delete_all()

        # import 地址下新建csvf文件夹
        # 将data/platform_test_mathdatas中的csv文件拷贝过去
        self.g.run("load csv from \"file:///csvf/e1.csv\" as line \n create (:e1{name:line[1],label:\"e1\"})")
        self.g.run(
            "load csv from \"file:///csvf/e2.csv\" as line \n create (:e2{name:line[1],label:\"e2\", person:line[2],flag:line[3],coll:line[4]})")
        self.g.run("load csv from \"file:///csvf/e3.csv\" as line \n create (:e3{name:line[1],label:\"e3\"})")
        self.g.run("load csv from \"file:///csvf/e4.csv\" as line \n create (:e4{name:line[1],label:\"e4\"})")
        self.g.run(
            "Load csv FROM \"file:///csvf/r1.csv\" as line \n match (from:e1{name:toString(line[1])}),(to:e2{name:toString(line[2])}) \n merge (from)-[r:need{name:toString(line[1]),name:toString(line[2])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r2.csv\" as line \n match (from:e2{name:toString(line[2])}),(to:e1{name:toString(line[1])}) \n merge (from)-[r:apply{name:toString(line[2]),name:toString(line[1])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r3.csv\" as line \n match (from:e2{name:toString(line[1])}),(to:e2{name:toString(line[2])}) \n merge (from)-[r:coop{name:toString(line[1]),name:toString(line[2])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r4.csv\" as line \n match (from:e1{name:toString(line[1])}),(to:e3{name:toString(line[2])}) \n merge (from)-[r:need{name:toString(line[1]),name:toString(line[2])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r5.csv\" as line \n match (from:e2{name:toString(line[1])}),(to:e4{name:toString(line[2])}) \n merge (from)-[r:research{name:toString(line[1]),name:toString(line[2])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r6.csv\" as line \n match (from:e3{name:toString(line[1])}),(to:e4{name:toString(line[2])}) \n merge (from)-[r:has{name:toString(line[1]),name:toString(line[2])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r7.csv\" as line \n match (from:e2{name:toString(line[1])}),(to:e2{name:toString(line[2])}) \n merge (from)-[r:has{name:toString(line[1]),name:toString(line[2])}]->(to)")

        self.g.run(
            "Load csv FROM \"file:///csvf/r1.csv\" as line \n match (from:e2{name:toString(line[2])}),(to:e1{name:toString(line[1])}) \n merge (from)-[r:need{name:toString(line[2]),name:toString(line[1])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r2.csv\" as line \n match (from:e1{name:toString(line[1])}),(to:e2{name:toString(line[2])}) \n merge (from)-[r:apply{name:toString(line[1]),name:toString(line[2])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r3.csv\" as line \n match (from:e2{name:toString(line[2])}),(to:e2{name:toString(line[1])}) \n merge (from)-[r:coop{name:toString(line[2]),name:toString(line[1])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r4.csv\" as line \n match (from:e3{name:toString(line[2])}),(to:e1{name:toString(line[1])}) \n merge (from)-[r:need{name:toString(line[2]),name:toString(line[1])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r5.csv\" as line \n match (from:e4{name:toString(line[2])}),(to:e2{name:toString(line[1])}) \n merge (from)-[r:research{name:toString(line[2]),name:toString(line[1])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r6.csv\" as line \n match (from:e4{name:toString(line[2])}),(to:e3{name:toString(line[1])}) \n merge (from)-[r:has{name:toString(line[2]),name:toString(line[1])}]->(to)")
        self.g.run(
            "Load csv FROM \"file:///csvf/r7.csv\" as line \n match (from:e2{name:toString(line[2])}),(to:e2{name:toString(line[1])}) \n merge (from)-[r:has{name:toString(line[2]),name:toString(line[1])}]->(to)")

    def search_all_graph(self):
        links_data = self.g.run("MATCH ()-[r]->() RETURN r").data()
        nodes_data = self.g.run("MATCH (n) RETURN n").data()

        # print(nodes_data)
        # print(links_data)

        finded_dict = {}
        finded_dict['nodes'], finded_dict['links'] = [], []
        node_dict = {}

        i = 0
        for v in nodes_data:
            new_dict = {}
            # 关系的连线依据index编号
            new_dict['index'] = i
            i = i + 1

            new_dict['name'] = v['n']['name']
            new_dict['name'] = new_dict['name'].replace('(','')
            new_dict['name'] = new_dict['name'].replace(')','')
            # 图片属性：暂时无用
            new_dict['id'] = ""
            new_dict['image'] = ""

            new_dict['label'] = v['n']['label']
            node_dict[new_dict['name']] = new_dict

            #print(new_dict['name'])
            finded_dict['nodes'].append(new_dict)


        for v in links_data:
            new_dict = {}
            import re
            vstr = str(v['r'])
            # 最小匹配
            vstr = vstr.replace('(','')
            vstr = vstr.replace(')', '')
            entity_list = vstr.split('-')
            if len(entity_list)>3:
                continue
            #print(entity_list[2].split('>')[1])
            entity_1 = entity_list[0]
            entity_2 = entity_list[2].split('>')[1]

            typestr = vstr.split('-')[1]
            m2 = re.compile(r'[A-Za-z0-9_\-\u4e00-\u9fa5_A-Za-z0-9]+', re.S)
            relation = re.findall(m2, typestr)[0]

            #print(entity_1,relation,entity_2)
            new_dict['type'] = relation
            new_dict['source'] = node_dict[entity_1]['index']
            new_dict['target'] = node_dict[entity_2]['index']
            new_dict['source_name'] = entity_1
            new_dict['target_name'] = entity_2
            finded_dict['links'].append(new_dict)
            #print(new_dict)

        self.graph_data = finded_dict
        return str(self.graph_data)

    def __process_graph_data__(self):
        for node in self.graph_data['nodes']:
            self.node[node['name']] = node
        for relation in self.graph_data['links']:
            self.relation[str(relation['source_name']) + '_to_' + str(relation['target_name'])] = relation

    def subgraph_return(self, nodes_data, links_data):
        # 子图翻译
        # print(nodes_data)
        # print(links_data)

        finded_dict = {}
        finded_dict['nodes'], finded_dict['links'] = [], []
        node_dict = {}
        i = 0
        for v in nodes_data:
            new_dict = {}
            # 关系的连线依据index编号
            import random
            new_dict['index'] = i
            i = i + 1
            new_dict['id'] = str(v['id'])
            new_dict['name'] = v['name']
            if "image" in v:
                new_dict['image'] = v['image']
            else:
                new_dict['image'] = ""
            node_dict[new_dict['name']] = new_dict
            finded_dict['nodes'].append(new_dict)

        for v in links_data:
            new_dict = {}
            import re
            vstr = str(v)
            # 最小匹配
            m1 = re.compile(r'[(](.*?)[)]', re.S)
            entity_list = re.findall(m1, vstr)
            entity_1 = entity_list[0]
            entity_2 = entity_list[1]

            typestr = vstr.split('-')[1]
            m2 = re.compile(r'[A-Za-z0-9_\-\u4e00-\u9fa5]+', re.S)
            type = re.findall(m2, typestr)[0]
            # print(vstr,entity_1,type,entity_2)
            new_dict['source'] = node_dict[entity_1]['index']
            new_dict['target'] = node_dict[entity_2]['index']
            new_dict['source_name'] = entity_1
            new_dict['target_name'] = entity_2
            new_dict['type'] = type
            finded_dict['links'].append(new_dict)
        return json.dumps(finded_dict)


    def kgR1(self,node):
        nodes_set = set()
        nodes = self.Nodematcher.match(name=node).first()
        nodes_set.add(nodes)
        relations = self.g.match(nodes=[nodes])
        for r in relations:
            for n in r.nodes:
                nodes_set.add(n)
        return self.subgraph_return(nodes_set,relations)

    # 1 kgClassification ------------------------------------------
    # 知识分类
    def kgLabels(self):
        nodes_data = self.g.run("MATCH (n) RETURN n").data()
        result_set = set()
        for n in nodes_data:
            label = str(n['n'].labels).split(":")[1]
            result_set.add(label)
        return list(result_set)

    # 2 kgDesc ------------------------------------------
    # 知识分类
    def node2list(self, Node_list):
        Nodes_list = []
        for n in Node_list:
            newNode = {}
            newNode['name'] = n.__name__
            newNode['label'] = str(n.labels).split(":")[1]
            newNode['id'] = str(n['id'])
            for k in n:
                newNode[k] = n[k]
            Nodes_list.append(newNode)
        return Nodes_list

    # 3 按照标签查节点列表 ------------------------------------------
    def kgNodesByLabel(self, label):
        finded = self.Nodematcher.match(label)
        return self.node2list(finded)


    def subgraph_return_for_main(self, nodes_data, links_data):
        # 子图翻译
        # print(nodes_data)
        # print(links_data)

        finded_dict = {}
        finded_dict['nodes'], finded_dict['links'] = [], []
        node_dict = {}
        i = 0
        for v in nodes_data:
            new_dict = {}
            # 关系的连线依据index编号
            import random
            new_dict['index'] = i
            i = i + 1
            new_dict['id'] = str(v['id'])
            new_dict['name'] = v['name']
            if "image" in v:
                new_dict['image'] = v['image']
            else:
                new_dict['image'] = ""
            node_dict[new_dict['name']] = new_dict
            finded_dict['nodes'].append(new_dict)

        for v in links_data:
            v['source'] = node_dict[v['source_name']]['index']
            v['target'] = node_dict[v['target_name']]['index']
        finded_dict['links'] = links_data
        return json.dumps(finded_dict)
    def __text2word__(self,text):
        keyword = []
        for v in self.node:
            if v in text:
                keyword.append(v)
        return keyword
    def search_main(self, text):
        keyword = self.__text2word__(text)
        entity = []
        res = {
            'graph': None,
            'entity':None
        }
        # 1- 分析keyword里面有什么:keyword是列表
        # 包含所有关系在内的子图
        # 关系:
        # 实体:
        graph = []
        for word in keyword:
            if word in self.node:
                entity.append(word)
                graph.append(self.kgR1(word))
        res['entity'] = entity
        entity = []
        # 属性
        # graph中加入最短路径
        from itertools import permutations
        for i in permutations( res['entity'],2):
            short_path_graph = self.shortestpathfinding(i[0],i[1])

            if not short_path_graph:
                return

            entity =  entity + short_path_graph['entity']
            graph.append(short_path_graph['graph'])
        for e in entity:
            if e not in res['entity']:
                res['entity'].append(e)
        # 实体一度图合并
        nodes_set = set()
        relations_set = set()
        relations = []
        for g in graph:
            g = json.loads(g)
            for node in g['nodes']:
                nodes = self.Nodematcher.match(name=node['name']).first()
                nodes_set.add(nodes)
            for link in g["links"]:
                key = link['source_name']+'_'+link['type']+'_' + link['target_name']
                if key not in relations_set:
                    relations.append(link)
                else:
                    relations_set.add(key)
            res['graph'] = self.subgraph_return_for_main(nodes_set,relations)
        return res

    def shortestpathfinding(self, startNodeName, endNodeName):
        startType = self.node[startNodeName]['label']
        endType =self.node[endNodeName]['label']

        if not startType or not endType:
            # 节点不存在
            return False
        p = self.g.run(
            "MATCH(p1:" + startType + "{name: \"" + startNodeName + "\"}), (p2:" + endType + "{name:\"" + endNodeName + "\"}),\n p = shortestpath((p1) - [*..10]-(p2)) \n  RETURN p")
        res = {'graph':None,'path':[],'entity':None}

        nodes,relations = [],[]
        for i in p:
            p = str(i)
            import re
            m2 = re.compile(r'[\u4e00-\u9fa5]+', re.S)
            entitylist = re.findall(m2, p)
            res['entity'] = entitylist
            for i in range(len(entitylist)-1):
                if entitylist[i] not in self.node:
                    continue
                nodes.append(self.node[entitylist[i]])
                if entitylist[i] + '_to_' + entitylist[i + 1] not in self.relation:
                    continue
                r = self.relation[entitylist[i]+'_to_'+entitylist[i+1]]
                relations.append(r)
            nodes.append(self.node[entitylist[len(entitylist)-1]])
            res['path'].append(p)
        res['graph'] = self.subgraph_return_for_main(nodes,relations)
        res['path'] = str(res['path'])
        return json.dumps(res)
        
        
        

if __name__ == '__main__':
    print(sys.argv)
    funcname = sys.argv[1]
    # 方法
    # 1-- 图谱构建
    cons = KG_constructor()
    if funcname == '--cons':
        cons.constructor()
    # 2-- 获取全部实体标签
    # 对应：http://localhost:8000/kgClassification
    elif funcname == '--getlabel':
        print(cons.kgLabels())

    # 3-- 获取标签下所有实体
    # 对应：http://localhost:8000/kgByLable?label=e1
    elif funcname == '--getentitybylabel':
        # 'e2'
        print(json.dumps(cons.kgNodesByLabel(sys.argv[2])))


    # 4-- 获取一度关系
    # 对应：http://localhost:8000/kgR1?node=飞行器材料损伤传感信号的特征分析和损伤模式识别
    elif funcname == '--getkgR1':
        #"'飞行器材料损伤传感信号的特征分析和损伤模式识别'"
        print(cons.kgR1(sys.argv[2]))
    # 5---------------------------------------------
    # 对应：http://localhost:8000/kgR1?node=飞行器材料损伤传感信号的特征分析和损伤模式识别
    elif funcname == '--getkgShortestPath':
        # "加密", "三维热传导"
        # python3 /Users/gunanxi/Downloads/md/project/2020-03_304/tornado_kg/kg_304/kg/neo4j2json.py --getkgShortestPath "加密"  "三维热传导"
        print(cons.shortestpathfinding(sys.argv[2],sys.argv[3]))


    # 6-- 获取全图
    # 对应：http://localhost:8000/alldata
    elif funcname == '--getalldata':
        print(cons.search_all_graph())

    # 7-- 查询子图
    # 对应：http://localhost:8000/search_main
    elif funcname == '--searchsubkg':
        search_text = "飞行器材料损伤传感信号的特征分析和损伤模式识别和三维热传导的关系是什么"
        print(json.dumps(cons.search_main(sys.argv[2])))





