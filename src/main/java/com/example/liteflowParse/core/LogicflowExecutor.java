package com.example.liteflowParse.core;


import com.example.liteflowParse.core.el.FlowConvertELUtil;
import com.example.liteflowParse.core.el.NodeInfoToELUtil;
import com.example.liteflowParse.core.graph.Node;
import com.example.liteflowParse.core.node.NodeInfoWrapper;
import com.yomahub.liteflow.builder.el.ELBus;
import com.yomahub.liteflow.builder.el.ELWrapper;
import com.yomahub.liteflow.builder.el.ThenELWrapper;
import com.yomahub.liteflow.builder.el.WhenELWrapper;

import java.util.List;

public class LogicflowExecutor {

    public static ELWrapper elWrapper(LogicFlowGraphEL graphEL) throws Exception {
        ELWrapper elWrapper = ELBus.then();
        //单起点
        if(graphEL.isSingeStart()){
            parseFlow(elWrapper, graphEL, graphEL.getStartNode(), null);
        }
        return elWrapper;
    }

    /**
     * 根据起始点和终止点来拼接
     * 1.如果该节点是分叉节点就解析分叉几点的各个分支
     * 2.如果该节点是聚合节点，与之前的表达式then拼接
     * 3.如果节点为普通的子节点，then拼接
     * 4.如果该节点不是最后一个节点，还有自己的分叉就要递归执行
     * @param elWrapper
     * @param graphEL
     * @param startNode
     * @param endNode
     */
    public static void parseFlow(ELWrapper elWrapper, LogicFlowGraphEL graphEL, Node startNode, Node endNode) throws Exception {
        if(graphEL.isFork(startNode)){
            flowFork(elWrapper, startNode, graphEL);
            return;
        }else if(graphEL.isJoin(startNode)){
            flowThen(elWrapper, startNode);
        }else{
            flowThen(elWrapper, startNode);
        }
        if(!graphEL.isLastNode(startNode)){
            Node nextNode = graphEL.getNextNode(startNode).get(0);
            if(endNode != null && endNode == nextNode) return;
            parseFlow(elWrapper, graphEL, nextNode, endNode);
        }
    }

    // 分叉节点处理

    /**
     * 分叉处理逻辑：
     * 1.从开始节点到聚合节点
     * 2.需要when包裹中间的节点
     * 3.如果包含聚合节点，需要重新解析parseFlow
     * @param wrapper
     * @param startNode
     * @param graphEL
     */
    public static void flowFork(ELWrapper wrapper, Node startNode, LogicFlowGraphEL graphEL) throws Exception {
        flowThen(wrapper, startNode);
        Node joinNode = graphEL.getJoinNode(startNode);
        flowWhen(wrapper,startNode,joinNode,graphEL);
        if(joinNode != null){
            parseFlow(wrapper, graphEL, joinNode, null);
        }
    }

    /**
     * 解析when
     * 1.获取分叉节点的所有分叉链
     * 2.在外部使用when包裹
     * 3.顺序解析每一个节点
     * @param wrapper
     * @param startNode
     * @param endNode
     * @param graphEL
     */
    public static void flowWhen(ELWrapper wrapper, Node startNode, Node endNode, LogicFlowGraphEL graphEL) throws Exception {
        List<Node> nextNodeList = graphEL.getNextNode(startNode);
        WhenELWrapper whenELWrapper = ELBus.when();
        for (Node nextNode : nextNodeList){
            ThenELWrapper thenELWrapper = ELBus.then();
            parseFlow(thenELWrapper, graphEL, nextNode, endNode);
            whenELWrapper.when(thenELWrapper);
        }
        FlowConvertELUtil.convert(wrapper,whenELWrapper);
    }

    // 串行处理
    public static void flowThen(ELWrapper wrapper, Node startNode) throws Exception {
        FlowConvertELUtil.convert(wrapper,nodeToEL(startNode));
    }

    public static Object nodeToEL(Node node) throws Exception {
        if(node == null ){ return null; }
        return getELWrapper(node);
    }

    public static Object getELWrapper(Node node) throws Exception {
        NodeInfoWrapper nodeInfoWrapper = node.getProperties();
        return NodeInfoToELUtil.buildELWrapper(nodeInfoWrapper);
    }


}
