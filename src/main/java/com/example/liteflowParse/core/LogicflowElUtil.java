package com.example.liteflowParse.core;


import com.example.liteflowParse.core.graph.LogicFlowData;
import com.yomahub.liteflow.builder.el.ELWrapper;

import java.util.List;

public class LogicflowElUtil {

    public static ELWrapper transform(LogicFlow flow) throws Exception {
        List<LogicFlowData> flowDataList = LogicflowJsonUtil.getLogicFlowGroup(flow.json);
        //单起点嵌套处理
        LogicFlowData data = flowDataList.get(0);
        LogicFlowGraphEL graphEL = LogicFlowGraphEL.getGraphEL(data);
        flow.graphELList.add(graphEL);
        return logicFlow(graphEL);
    }

    public static ELWrapper logicFlow(LogicFlowGraphEL graphEL) throws Exception {
        ELWrapper wrapper = LogicflowExecutor.elWrapper(graphEL);
        return wrapper;
    }

}
