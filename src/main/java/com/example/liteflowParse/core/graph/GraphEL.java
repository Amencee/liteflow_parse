package com.example.liteflowParse.core.graph;

import com.example.liteflowParse.core.node.CmpInfo;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class GraphEL {
    private Map<Node, List<Node>> list;//正序
    private List<Edge> edgeList;
    private Map<Node, List<Node>> reverseList;//倒序
    private Map<Long, CmpInfo> nodeInfoMap;
    private List<Node> groupParallelList;
    private List<Node> preList;
    private List<Node> finallyList;
    private List<Node> fallbackList;

    private Node startNode;
    private List<Node> startNodeList;
    private List<Node> endNode;

    public GraphEL() {
        this.list = new LinkedHashMap<>();
        this.reverseList = new LinkedHashMap<>();
    }



}
