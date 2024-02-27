package com.example.liteflowParse.core.graph;

import lombok.Data;

import java.util.List;

@Data
public class LogicFlowData {
    private List<Node> nodes;//节点
    private List<Edge> edges;//连线

}
