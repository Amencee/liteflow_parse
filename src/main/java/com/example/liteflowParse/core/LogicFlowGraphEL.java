package com.example.liteflowParse.core;

import com.example.liteflowParse.core.graph.Edge;
import com.example.liteflowParse.core.graph.LogicFlowData;
import com.example.liteflowParse.core.graph.Node;
import com.example.liteflowParse.core.node.IvyCmp;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class LogicFlowGraphEL {
    private Map<Node, List<Node>> list;//正序
    private List<Edge> edgeList;
    private Map<Node, List<Node>> reverseList;//倒序
    private Map<Long, IvyCmp> nodeInfoMap;
    private List<Node> groupParallelList;
    private List<Node> preList;
    private List<Node> finallyList;
    private List<Node> fallbackList;

    private Node startNode;
    private List<Node> startNodeList;
    private Node endNode;
    private List<Node> endNodeList;
    private List<Node> forkNodeList;//分叉节点
    private List<Node> joinNodeList;//聚合节点

    public LogicFlowGraphEL() {
        this.list = new LinkedHashMap<>();
        this.reverseList = new LinkedHashMap<>();
    }

    public static LogicFlowGraphEL getGraphEL(LogicFlowData logicFlowData){
        return getGraphEL(logicFlowData,logicFlowData.getIvyCmpMap());
    }

    public static LogicFlowGraphEL getGraphEL(LogicFlowData logicFlowData, Map<Long,IvyCmp> nodeInfoMap){
        LogicFlowGraphEL graph = new LogicFlowGraphEL();
        graph.setNodeInfoMap(nodeInfoMap);
        graph.setGroupParallelList(logicFlowData.getGroupParallelList());
        List<Node> nodes = logicFlowData.getNodes();
        List<Node> startNodeList = new ArrayList<>();
        Map<String, Node> nodeMap = nodes.stream().collect(Collectors.toMap(Node::getId, m -> m));
        for (Node node : nodes) {
            graph.addNode(node);
        }
        List<Edge> edges = logicFlowData.getEdges();
        graph.setEdgeList(edges);
        Set<String> targetNodes = new HashSet<>();
        for (Edge edge : edges) {
            graph.addEdge(nodeMap.get(edge.getSourceNodeId()), nodeMap.get(edge.getTargetNodeId()));
            targetNodes.add(edge.getTargetNodeId());
        }
        if(!edges.isEmpty()){
            for (Edge edge : edges) {
                String sourceNodeId = edge.getSourceNodeId();
                if (!targetNodes.contains(sourceNodeId)) {
                    Node startNode = nodeMap.get(sourceNodeId);
                    graph.setStartNode(startNode);
                    if(!startNodeList.contains(startNode)){
                        startNodeList.add(startNode);
                    }
                }
            }
        }else{
            graph.setStartNode(nodes.get(0));
        }

        List<Node> endNodeList = graph.getList().entrySet().stream()
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        graph.setEndNode(endNodeList.stream().findFirst().orElse(null));
        graph.setStartNodeList(startNodeList);
        graph.setEndNodeList(endNodeList);
        handlerFork(graph);
        handlerJoin(graph);
        return graph;
    }

    private static void handlerFork(LogicFlowGraphEL graph) {
        graph.setForkNodeList(graph.getList().entrySet().stream().map(m -> {
            if (m.getValue().size() > 1) {
                return m.getKey();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }
    private static void handlerJoin(LogicFlowGraphEL graph) {
        graph.setJoinNodeList(graph.getReverseList().entrySet().stream().map(m -> {
            if (m.getValue().size() > 1) {
                return m.getKey();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }


    public void addNode(Node node) {
        list.put(node, new ArrayList<>());
        reverseList.put(node, new ArrayList<>());
    }

    public void addEdge(Node sourceNode, Node targetNode) {
        list.get(sourceNode).add(targetNode);
        reverseList.get(targetNode).add(sourceNode);
    }

    // 获取起始节点
    // 获取结束节点
    // 获取下一个节点
    public List<Node> getNextNode(Node startNode) {
        return list.get(startNode);
    }
    // 获取上一个节点

    // 判断是否是单起点
    public boolean isSingeStart(){
        return startNodeList.size() <= 1;
    }

    // 判断是否是多起点
    public boolean isMultipleStart(){
        return !isSingeStart();
    }

    // 判断是否是分叉节点
    public boolean isFork(Node node){
        return forkNodeList.contains(node);
    }

    // 判断是否是聚合节点
    public boolean isJoin(Node node){
        return joinNodeList.contains(node);
    }

    // 判断是否是结束节点
    public boolean isLastNode(Node node){
        return list.get(node).size() == 0;
    }

    public Node getJoinNode(Node startNode) {
        for (Node node : joinNodeList){
            List<List<Node>> allPaths = getAllPaths(startNode, node, false);
            // 获取在所有路径中相同节点只有2个的聚合节点
            List<Node> commonNodes = allPaths.stream()
                    .flatMap(List::stream) // 扁平化为节点流
                    .collect(Collectors.groupingBy(Node::getId)) // 按节点值分组
                    .entrySet().stream()
                    .filter(entry -> entry.getValue().size() == allPaths.size()) // 筛选在所有列表中都存在的节点
                    .map(entry -> entry.getValue().get(0))
                    .collect(Collectors.toList());
            if(commonNodes.size() == 2){
                return node;
            }
        }
        return null;
    }


    // 获取从给定节点开始到结束节点之间的所有路径
    public List<List<Node>> getAllPaths(Node startNode, Node endNode, boolean excludeStartAndEnd) {
        List<List<Node>> paths = new ArrayList<>();
        List<Node> currentPath = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        dfsGetAllPaths(startNode, endNode, currentPath, paths, visited);
        if(excludeStartAndEnd){
            return paths.stream()
                    .map(list -> list.stream()
                            .filter(node -> !node.getId().equals(startNode.getId()) && !node.getId().equals(endNode.getId()))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }
        return paths;
    }



    private void dfsGetAllPaths(Node currentNode, Node endNode, List<Node> currentPath, List<List<Node>> paths, Set<Node> visited) {
        currentPath.add(currentNode);
        visited.add(currentNode);

        if (currentNode.equals(endNode)) {
            // 当前节点是结束节点，将路径加入结果
            paths.add(new ArrayList<>(currentPath));
        } else {
            // 继续深度优先搜索
            for (Node nextNode : getNextNode(currentNode)) {
                if (!visited.contains(nextNode)) {
                    dfsGetAllPaths(nextNode, endNode, currentPath, paths, visited);
                }
            }
        }

        // 回溯
        currentPath.remove(currentPath.size() - 1);
        visited.remove(currentNode);
    }






}
