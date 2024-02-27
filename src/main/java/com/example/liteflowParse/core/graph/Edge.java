package com.example.liteflowParse.core.graph;

import lombok.Data;

import java.util.Objects;

@Data
public class Edge {

    String sourceNodeId;
    String targetNodeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        if (!Objects.equals(sourceNodeId, edge.sourceNodeId)) return false;
        return Objects.equals(targetNodeId, edge.targetNodeId);
    }

    @Override
    public int hashCode() {
        int result = sourceNodeId != null ? sourceNodeId.hashCode() : 0;
        result = 31 * result + (targetNodeId != null ? targetNodeId.hashCode() : 0);
        return result;
    }
}
