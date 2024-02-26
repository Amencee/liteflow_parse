package com.example.liteflowParse.core.node;


import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.Data;

@Data
public class CmpInfo {

    private Long id;

    private String componentId;

    private String componentName;

    private String type;//

    private NodeTypeEnum nodeType;

    private String cmpId;
    
    private String cmpTag;
    private String cmpDataName;
    private String cmpData;
}
