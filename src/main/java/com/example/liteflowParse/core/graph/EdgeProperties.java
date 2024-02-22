package com.example.liteflowParse.core.graph;

import lombok.Data;

@Data
public class EdgeProperties {
    // 定义节点和边的属性，根据实际情况添加字段
    String id;
    String tag;
    Integer linkType = 0;// 0:普通路径，1：特殊路径
    String switchType;// swicth:普通路径，default：默认路径
    String ifType;// true:true路径，false：false路径

}
