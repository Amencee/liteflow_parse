package com.example.liteflowParse.controller;


import com.example.liteflowParse.core.*;
import com.example.liteflowParse.core.graph.LogicFlowData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parse")
public class ParseController {


    @PostMapping("/start")
    public Result<?> buildNew(@RequestBody Map<String, Object> map) throws Exception {
        String json = (String) map.get("json");
        List<LogicFlowData> flowDataList = LogicflowJsonUtil.getLogicFlowGroup(json);
        LogicFlowData data = flowDataList.get(0);
        LogicFlowGraphEL graphEL = LogicFlowGraphEL.getGraphEL(data);
        String el = LogicflowExecutor.elWrapper(graphEL).toEL(true);
        return Result.OK(el);
    }
}
