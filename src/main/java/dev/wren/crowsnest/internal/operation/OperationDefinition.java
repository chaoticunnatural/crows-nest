package dev.wren.crowsnest.internal.operation;


import dev.wren.crowsnest.internal.argument.ArgumentDescriptor;
import dev.wren.crowsnest.internal.argument.ArgumentSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OperationDefinition<I, R> {

    private final Class<I> inputType;
    private final Class<R> returnType;
    private final BiFunction<I, ArgumentSet, R> operation;
    private final String name;
    private final List<ArgumentDescriptor> argumentDescriptors;

    public OperationDefinition(Class<I> inputType, Class<R> returnType, BiFunction<I, ArgumentSet, R> operation, String name, List<ArgumentDescriptor> argumentDescriptors) {
        this.inputType = inputType;
        this.returnType = returnType;
        this.operation = operation;
        this.name = name;
        this.argumentDescriptors = argumentDescriptors != null ? argumentDescriptors : Collections.emptyList();
    }

    public OperationDefinition(Class<I> inputType, Class<R> returnType, BiFunction<I, ArgumentSet, R> operation, String name) {
        this(inputType, returnType, operation, name, Collections.emptyList());
    }

    public Class<I> inputType() { return inputType; }
    public Class<R> returnType() { return returnType; }
    public String name() { return name; }

    public R perform(Object input, ArgumentSet args) {
        I typedInput = inputType.cast(input);
        return operation.apply(typedInput, args);
    }

    public List<ArgumentDescriptor> getArgumentDescriptors() {
        return argumentDescriptors != null ? argumentDescriptors : Collections.emptyList();
    }
}