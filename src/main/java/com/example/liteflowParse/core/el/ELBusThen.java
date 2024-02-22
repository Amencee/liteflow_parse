package com.example.liteflowParse.core.el;


import com.example.liteflowParse.core.node.IvyCmp;
import com.example.liteflowParse.core.util.StrUtil;
import com.yomahub.liteflow.builder.el.ELBus;
import com.yomahub.liteflow.builder.el.ThenELWrapper;

public class ELBusThen {

    private ThenELWrapper wrapper = ELBus.then();

    public static ELBusThen NEW(){
        return new ELBusThen();
    }

    public ELBusThen node(IvyCmp info){
        ThenELWrapper thenELWrapper = ELBus.then(ELBusNode.NEW().node(info).toELWrapper());
        if(StrUtil.isNotEmpty(info.getCmpPre())){
            thenELWrapper.pre(info.getCmpPre());
        }
        if(StrUtil.isNotEmpty(info.getCmpFinallyOpt())){
            thenELWrapper.finallyOpt(info.getCmpFinallyOpt());
        }
        if(StrUtil.isNotEmpty(info.getCmpId())){
            thenELWrapper.id(info.getCmpId());
        }
        if(StrUtil.isNotEmpty(info.getCmpTag())){
            thenELWrapper.tag(info.getCmpTag());
        }
        if(info.getCmpMaxWaitSeconds() != null){
            thenELWrapper.maxWaitSeconds(info.getCmpMaxWaitSeconds());
        }
        wrapper = thenELWrapper;
        return this;
    }


    public ThenELWrapper toELWrapper(){
        return wrapper;
    }

}
