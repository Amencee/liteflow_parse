package com.example.liteflowParse.core.node;


import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.Data;

@Data
public class IvyCmp {

    
    private Long id;


    private String componentId;


    private String componentName;

    
    private String type;//

    private NodeTypeEnum nodeType;//switch,for

    

    private String script;//

    
    private String language;//


    private String clazz;//

    
    private Long fallbackId;//

    private String fallbackType;//


    private String el;


    private String elFormat;


    private String cmpPre;


    private String cmpFinallyOpt;

    
    private String cmpId;

    
    private String cmpTag;

    
    private Integer cmpMaxWaitSeconds;


    private String cmpTo;


    private String cmpDefaultOpt;

    private Object cmpToEL;
    private Object cmpDefaultOptEL;


    private String cmpTrueOpt;
    private Object cmpTrueOptEL;


    private String cmpFalseOpt;
    private Object cmpFalseOptEL;

    
    private Boolean cmpParallel;


    private String cmpDoOpt;


    private String cmpBreakOpt;


    private String cmpDataName;


    
    private String cmpData;
}
