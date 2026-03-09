package dev.wren.crowsnest.internal.pipeline;

import dev.wren.crowsnest.internal.operation.OperationNode;
import oshi.annotation.concurrent.Immutable;

import java.util.List;

/**
 * Pipeline, but immutable
 */
@Immutable
public class ImmutablePipeline {

    private final List<OperationNode> nodes;

    public ImmutablePipeline(Pipeline parent) {
        this.nodes = parent.getOperations();
    }

    public List<OperationNode> getOperations() {
        return nodes;
    }
}
