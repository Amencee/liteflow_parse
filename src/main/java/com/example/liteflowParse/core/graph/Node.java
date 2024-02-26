package com.example.liteflowParse.core.graph;

import com.example.liteflowParse.core.node.CmpInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Node implements Serializable {

    private String id;
    private String type;
    private CmpInfo properties;
    private String text;
    List<String> children;

}
