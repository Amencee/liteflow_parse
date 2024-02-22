package com.example.liteflowParse.core.graph;

import com.example.liteflowParse.core.node.IvyCmp;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LogicFlowData {

    private List<Node> nodes;//节点

    private List<Edge> edges;//连线

    private List<Node> groupParallelList;//并行分组

    private List<Node> preList;//前置组件

    private List<Node> finallyList;//后置组件

    private List<Node> fallbackList;//降级组件

    private List<IvyCmp> ivyCmpList;//组件模板
    private Map<Long,IvyCmp> ivyCmpMap;//组件模板

}
