package com.example.liteflowParse.core;

import com.yomahub.liteflow.builder.el.ELWrapper;

import java.util.ArrayList;
import java.util.List;

public class LogicFlow {

    public String json;
    public List<LogicFlowGraphEL> graphELList;
    public boolean formatEL = false;
    public ELWrapper wrapper;

    private LogicFlow(){
        this.graphELList = new ArrayList<>();
    }

    public static LogicFlow NEW(){
        return new LogicFlow();
    }

    public LogicFlow json(String json){
        this.json = json;
        return this;
    }

    public LogicFlow transform() throws Exception {
        this.wrapper = LogicflowElUtil.transform(this);
        return this;
    }


    public String buildEL(boolean formatEL){
        if(wrapper != null){
            return wrapper.toEL(formatEL);
        }
        return null;
    }

}
