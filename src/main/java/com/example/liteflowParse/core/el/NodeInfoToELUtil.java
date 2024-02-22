package com.example.liteflowParse.core.el;


import com.example.liteflowParse.core.node.IvyCmp;
import com.example.liteflowParse.core.node.NodeInfoWrapper;
import com.example.liteflowParse.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.ELWrapper;

public class NodeInfoToELUtil {
    public static ELWrapper buildELWrapper(NodeInfoWrapper nodeInfoWrapper) throws Exception {
        String componentId = nodeInfoWrapper.getComponentId();
        if(cn.hutool.core.util.StrUtil.isBlank(componentId)){
            throw new Exception("节点组件ID为空");
        }
        return toELWrapper(nodeInfoWrapper);
    }

    public static ELWrapper toELWrapper(IvyCmp info){
        handler(info);
        ELWrapper el = null;
        switch (info.getType()){
            case "common": el = ELBusThen.NEW().node(info).toELWrapper();break;
            case "fallback":
            default:
        }
        return el;
    }


    public static void handler(IvyCmp info) {
        if(StrUtil.isEmpty(info.getScript())){info.setScript(null);}
        if(StrUtil.isEmpty(info.getLanguage())){info.setLanguage(null);}
        if(StrUtil.isEmpty(info.getClazz())){info.setClazz(null);}
        if(StrUtil.isEmpty(info.getCmpPre())){info.setCmpPre(null);}
        if(StrUtil.isEmpty(info.getCmpFinallyOpt())){info.setCmpFinallyOpt(null);}
        if(StrUtil.isEmpty(info.getCmpId())){info.setCmpId(null);}
        if(StrUtil.isEmpty(info.getCmpTag())){info.setCmpTag(null);}
        if(StrUtil.isEmpty(info.getCmpTo())){info.setCmpTo(null);}
        if(StrUtil.isEmpty(info.getCmpDefaultOpt())){info.setCmpDefaultOpt(null);}
        if(StrUtil.isEmpty(info.getCmpTrueOpt())){info.setCmpTrueOpt(null);}
        if(StrUtil.isEmpty(info.getCmpFalseOpt())){info.setCmpFalseOpt(null);}
        if(StrUtil.isEmpty(info.getCmpDoOpt())){info.setCmpDoOpt(null);}
        if(StrUtil.isEmpty(info.getCmpBreakOpt())){info.setCmpBreakOpt(null);}
        if(StrUtil.isEmpty(info.getCmpDataName())){info.setCmpDataName(null);}
        if(StrUtil.isEmpty(info.getCmpData())){info.setCmpData(null);}
    }

}
