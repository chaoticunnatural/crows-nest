package dev.wren.crowsnest.internal.operation;

import dev.wren.crowsnest.internal.argument.ArgumentSet;

public record OperationNode(OperationDefinition<?, ?> operation, ArgumentSet arguments) { }