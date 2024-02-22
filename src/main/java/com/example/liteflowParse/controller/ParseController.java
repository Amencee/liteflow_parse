package com.example.liteflowParse.controller;


import com.example.liteflowParse.core.LogicFlow;
import com.example.liteflowParse.core.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/parse")
public class ParseController {


    @PostMapping("/start")
    public Result<?> buildNew(@RequestBody Map<String, Object> map) throws Exception {
        return Result.OK(LogicFlow.NEW().json((String) map.get("json")).transform().buildEL((Boolean) map.get("formatEL")));
    }
}
