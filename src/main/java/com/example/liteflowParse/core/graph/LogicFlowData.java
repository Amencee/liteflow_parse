package com.example.liteflowParse.core.graph;

import com.example.liteflowParse.core.node.CmpInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LogicFlowData {

    private List<Node> nodes;//节点

    private List<Edge> edges;//连线

    private List<CmpInfo> cmpInfoList;//组件模板
    private Map<Long, CmpInfo> ivyCmpMap;//组件模板

}
