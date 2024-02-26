package com.example.liteflowParse.core.el;


import com.example.liteflowParse.core.node.CmpInfo;
import com.example.liteflowParse.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.ELWrapper;

public class NodeInfoToELUtil {
    public static ELWrapper buildELWrapper(CmpInfo nodeInfoWrapper) throws Exception {
        String componentId = nodeInfoWrapper.getComponentId();
        if(cn.hutool.core.util.StrUtil.isBlank(componentId)){
            throw new Exception("节点组件ID为空");
        }
        return toELWrapper(nodeInfoWrapper);
    }

    public static ELWrapper toELWrapper(CmpInfo info){
        handler(info);
        ELWrapper el = null;
        switch (info.getType()){
            case "common": el = ELBusThen.NEW().node(info).toELWrapper();break;
            case "fallback":
            default:
        }
        return el;
    }


    public static void handler(CmpInfo info) {
        if(StrUtil.isEmpty(info.getCmpId())){info.setCmpId(null);}
        if(StrUtil.isEmpty(info.getCmpTag())){info.setCmpTag(null);}
        if(StrUtil.isEmpty(info.getCmpData())){info.setCmpData(null);}
    }

}
