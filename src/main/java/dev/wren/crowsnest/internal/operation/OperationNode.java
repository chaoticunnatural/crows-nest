package dev.wren.crowsnest.internal.operation;

import dev.wren.crowsnest.internal.argument.ArgumentSet;

public class OperationNode {

    private final OperationDefinition<?, ?, ?> operation;
    private final ArgumentSet arguments;

    public OperationNode(OperationDefinition<?, ?, ?> operation, ArgumentSet arguments) {
        this.operation = operation;
        this.arguments = arguments;
    }

    public OperationDefinition<?, ?, ?> getOperation() {
        return operation;
    }

    public ArgumentSet getArguments() {
        return arguments;
    }
}