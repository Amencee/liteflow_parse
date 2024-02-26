package com.example.liteflowParse.core.el;


import com.example.liteflowParse.core.node.CmpInfo;
import com.example.liteflowParse.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.ELBus;
import com.yomahub.liteflow.builder.el.ThenELWrapper;

public class ELBusThen {

    private ThenELWrapper wrapper = ELBus.then();

    public static ELBusThen NEW(){
        return new ELBusThen();
    }

    public ELBusThen node(CmpInfo info){
        ThenELWrapper thenELWrapper = ELBus.then(ELBusNode.NEW().node(info).toELWrapper());
        if(StrUtil.isNotEmpty(info.getCmpId())){
            thenELWrapper.id(info.getCmpId());
        }
        if(StrUtil.isNotEmpty(info.getCmpTag())){
            thenELWrapper.tag(info.getCmpTag());
        }
        wrapper = thenELWrapper;
        return this;
    }


    public ThenELWrapper toELWrapper(){
        return wrapper;
    }

}
