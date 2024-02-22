package com.example.liteflowParse.core;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.example.liteflowParse.core.graph.Edge;
import com.example.liteflowParse.core.graph.LogicFlowData;
import com.example.liteflowParse.core.graph.Node;
import com.example.liteflowParse.core.node.NodeInfoWrapper;

import java.util.*;
import java.util.stream.Collectors;

public class LogicflowJsonUtil {

    public static List<LogicFlowData> getLogicFlowGroup(String json) throws Exception {
        LogicFlowData logicFlowData = getLogicFlowData(json);
        Map<String, List<Node>> groupMap = logicFlowData.getNodes().stream().collect(Collectors.groupingBy(Node::getType));
        handlerLogicFlowData(groupMap, logicFlowData, Constant.GROUP_PARALLEL, Constant.GROUP_SERIAL);
        logicFlowData.getNodes().removeIf(m->Constant.GROUP_PARALLEL.equalsIgnoreCase(m.getType()));
        logicFlowData.getNodes().removeIf(m->Constant.GROUP_SERIAL.equalsIgnoreCase(m.getType()));
        logicFlowData.getNodes().removeIf(m->Constant.NODE_PRE_COMPONENT.equalsIgnoreCase(m.getType()));
        List<Node> groupList = handlerGroup(groupMap.get(Constant.GROUP_PARALLEL));
        List<Node> preList = handlerPreFinally(groupMap.get(Constant.NODE_PRE_COMPONENT));
        List<Node> finallyList = handlerPreFinally(groupMap.get(Constant.NODE_FINALLY_COMPONENT));
        List<Node> fallbackList = groupMap.get(Constant.NODE_FALLBACK_COMPONENT);

        List<LogicFlowData> dataList = splitByPaths(logicFlowData, groupList, preList, finallyList, fallbackList);
        handlerCmp(dataList);
        return dataList;
    }

    private static void handlerCmp(List<LogicFlowData> dataList) {
        if(CollUtil.isNotEmpty(dataList)){
            List<Node> flattenedList = new ArrayList<>();
            dataList.forEach(m-> {
                flattenedList.addAll(m.getNodes());
                if(CollUtil.isNotEmpty(m.getPreList())){
                    flattenedList.addAll(m.getPreList());
                }
                if(CollUtil.isNotEmpty(m.getFinallyList())) {
                    flattenedList.addAll(m.getFinallyList());
                }
                if(CollUtil.isNotEmpty(m.getFallbackList())) {
                    flattenedList.addAll(m.getFallbackList());
                }
            });

            Set<Long> idSet = flattenedList.stream().filter(m -> m.getProperties() != null && m.getProperties().getId() != null)
                    .map(m -> m.getProperties().getId()).collect(Collectors.toSet());

        }
    }

    public static LogicFlowData getLogicFlowData(String json){
        LogicFlowData data = JSON.parseObject(json, LogicFlowData.class);
        data.getNodes().forEach(m->{
            NodeInfoWrapper properties = m.getProperties();
            if(properties.getType() == null){
                properties.setType(m.getType());
            }
        });
        return data;
    }

    //处理json数据，去除无用的连接线【比如节点连接分组】
    private static void handlerLogicFlowData(Map<String, List<Node>> groupMap,LogicFlowData logicFlowData,String... keys){
        for (String key : keys){
            List<Node> list = groupMap.get(key);
            if(list != null){
                for (Node node : list){
                    logicFlowData.getEdges().removeIf(m->m.getSourceNodeId().equals(node.getId()) || m.getTargetNodeId().equals(node.getId()));
                }
            }
        }
    }

    private static List<Node> handlerGroup(List<Node> nodes) throws Exception {
        if(nodes == null){
            return null;
        }
        boolean flag = nodes.stream().anyMatch(node -> node.getChildren() == null || node.getChildren().isEmpty());
        if (flag) {
            throw new Exception("分组中未找到节点，请将节点移入分组中！");
        }
        return nodes;
    }

    private static List<Node> handlerPreFinally(List<Node> nodes) throws Exception {
        return nodes;
    }



    public static List<LogicFlowData> splitByPaths(LogicFlowData data,List<Node> groupList,List<Node> preList,List<Node> finallyList,List<Node> fallbackList) {
        List<LogicFlowData> result = new ArrayList<>();
        Map<String, Integer> outDegreeMap = new HashMap<>();//出线
        Map<String, Integer> inDegreeMap = new HashMap<>();//进线

        for (Edge edge : data.getEdges()) {
            outDegreeMap.put(edge.getSourceNodeId(), outDegreeMap.getOrDefault(edge.getSourceNodeId(), 0) + 1);
            inDegreeMap.put(edge.getTargetNodeId(), inDegreeMap.getOrDefault(edge.getTargetNodeId(), 0) + 1);
        }
        Set<String> startNodeSet = new HashSet<>();
        aa: for (Edge edge : data.getEdges()) {
            if(!inDegreeMap.containsKey(edge.getSourceNodeId()) && !startNodeSet.contains(edge.getSourceNodeId())){
                // 找到起始节点
                Node startNode = getNodeById(edge.getSourceNodeId(), data.getNodes());
                LogicFlowData pathData = new LogicFlowData();
                pathData.setNodes(new ArrayList<>());
                pathData.setEdges(new ArrayList<>());
                dfs(startNode, data, pathData, outDegreeMap);
                // 判断是否已存在路径
                for (LogicFlowData logicFlowData : result){
                    List<Node> nodes = logicFlowData.getNodes();
                    boolean hasCommonNodes = nodes.stream().anyMatch(node1 -> pathData.getNodes().stream().anyMatch(node2 -> node2.equals(node1)));
                    if(hasCommonNodes){ // 如果是多起点单有交集
                        logicFlowData.getEdges().addAll(pathData.getEdges());
                        logicFlowData.getNodes().addAll(pathData.getNodes());
                        logicFlowData.setEdges(logicFlowData.getEdges().stream().distinct().collect(Collectors.toList()));
                        logicFlowData.setNodes(logicFlowData.getNodes().stream().distinct().collect(Collectors.toList()));
                        continue aa;
                    }
                }
                pathData.setGroupParallelList(getGroup(pathData,groupList));//并行分组节点信息
                pathData.setPreList(preList);
                pathData.setFinallyList(finallyList);
                pathData.setFallbackList(fallbackList);
                result.add(pathData);
                startNodeSet.add(edge.getSourceNodeId());
            }
        }

        //单个节点
        Set<String> set1 = data.getEdges().stream().map(Edge::getSourceNodeId).collect(Collectors.toSet());
        Set<String> set2 = data.getEdges().stream().map(Edge::getTargetNodeId).collect(Collectors.toSet());
        set1.addAll(set2);
        for (Node node : data.getNodes()){
            if(!set1.contains(node.getId())){
                LogicFlowData pathData = new LogicFlowData();
                List<Node> nodeList = new ArrayList<>();
                nodeList.add(node);
                pathData.setNodes(nodeList);
                pathData.setEdges(new ArrayList<>());
                pathData.setGroupParallelList(getGroup(pathData,groupList));
                pathData.setPreList(preList);
                pathData.setFinallyList(finallyList);
                pathData.setFallbackList(fallbackList);
                result.add(pathData);
            }
        }
        return result;
    }

    private static void dfs(Node currentNode, LogicFlowData data, LogicFlowData pathData, Map<String, Integer> outDegreeMap) {
        // 避免重复添加节点
        if(!pathData.getNodes().contains(currentNode)){
            pathData.getNodes().add(currentNode);
        }
        for (Edge edge : data.getEdges()) {
            if (edge.getSourceNodeId().equals(currentNode.getId())) {
                // 避免重复添加连线
                if(!pathData.getEdges().contains(edge)){
                    pathData.getEdges().add(edge);
                }
                outDegreeMap.put(currentNode.getId(), outDegreeMap.get(currentNode.getId()) - 1);
                // 获取目标节点对象
                Node targetNode = getNodeById(edge.getTargetNodeId(), data.getNodes());
                dfs(targetNode, data, pathData, outDegreeMap);
            }
        }
    }

    private static Node getNodeById(String nodeId, List<Node> nodes) {
        for (Node node : nodes) {
            if (node.getId().equals(nodeId)) {
                return node;
            }
        }
        return null;
    }

    private static List<Node> getGroup(LogicFlowData pathData, List<Node> groupList){
        if(groupList == null) { return null;}
        Set<String> nodeSet = pathData.getNodes().stream().map(Node::getId).collect(Collectors.toSet());
        return groupList.stream().filter(m -> retainAll(m.getChildren(), nodeSet)).collect(Collectors.toList());
    }

    public static boolean retainAll(List<String> list,Set<String> nodeSet){
        return list.stream().anyMatch(nodeSet::contains);
    }
}
