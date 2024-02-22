package com.example.liteflowParse.core.node;

import lombok.Data;

@Data
public class NodeInfoWrapper extends IvyCmp {

    private String[] ids;
    private Boolean whenIgnoreError;
    private Boolean whenAny;
    private String whenMust;
    private Boolean ignoreType;


    private Long fallbackCommonId;
    private Long fallbackSwitchId;
    private Long fallbackIfId;
    private Long fallbackForId;
    private Long fallbackWhileId;
    private Long fallbackBreakId;
    private Long fallbackIteratorId;

}
