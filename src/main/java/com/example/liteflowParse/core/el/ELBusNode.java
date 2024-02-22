package com.example.liteflowParse.core.el;

import com.alibaba.fastjson2.JSON;

import com.example.liteflowParse.core.node.IvyCmp;
import com.example.liteflowParse.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.ELBus;
import com.yomahub.liteflow.builder.el.NodeELWrapper;

import java.util.Map;

public class ELBusNode {

    private NodeELWrapper wrapper = null;

    public static ELBusNode NEW(){
        return new ELBusNode();
    }

    public ELBusNode node(IvyCmp info){
        NodeELWrapper nodeELWrapper = ELBus.node(info.getComponentId());
        String jsonString = info.getCmpData();
        if(StrUtil.isNotEmpty(jsonString)){
            try {
                JSON.parseObject(jsonString, Map.class);
                nodeELWrapper.data(info.getCmpDataName(), "'"+jsonString+"'");
            }catch (Exception e){
                System.err.println("ELBusNode: Invalid JSON format");
            }
        }
        wrapper = nodeELWrapper;
        return this;
    }


    public NodeELWrapper toELWrapper(){
        return wrapper;
    }

}
