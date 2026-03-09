package dev.wren.crowsnest.internal.operation;


import dev.wren.crowsnest.internal.argument.ArgumentDescriptor;
import dev.wren.crowsnest.internal.argument.ArgumentSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OperationDefinition<I, A, R> {

    private final Class<I> inputType;
    private final Class<A> argType;
    private final Class<R> returnType;
    private final BiFunction<I, A, R> operation;
    private final String name;
    private final List<ArgumentDescriptor> argumentDescriptors;

    public OperationDefinition(Class<I> inputType, Class<A> argType, Class<R> returnType, BiFunction<I, A, R> operation, String name, List<ArgumentDescriptor> argumentDescriptors) {
        this.inputType = inputType;
        this.argType = argType;
        this.returnType = returnType;
        this.operation = operation;
        this.name = name;
        this.argumentDescriptors = argumentDescriptors != null ? argumentDescriptors : Collections.emptyList();
    }

    public OperationDefinition(Class<I> inputType, Class<A> argType, Class<R> returnType, BiFunction<I, A, R> operation, String name) {
        this(inputType, argType, returnType, operation, name, Collections.emptyList());
    }

    public Class<I> inputType() { return inputType; }
    public Class<A> argType() { return argType; }
    public Class<R> returnType() { return returnType; }
    public String name() { return name; }

    @SuppressWarnings("unchecked")
    public R perform(Object input, ArgumentSet args) {
        I typedInput = inputType.cast(input);

        if (argType == Void.class || argumentDescriptors.isEmpty()) {
            return operation.apply(typedInput, null);
        }

        A typedArgs = (A) args;

        return operation.apply(typedInput, typedArgs);
    }

    public boolean hasArg() {
        return argType != Void.class;
    }

    public List<ArgumentDescriptor> getArgumentDescriptors() {
        return argumentDescriptors != null ? argumentDescriptors : Collections.emptyList();
    }

    public Map.Entry<String, OperationDefinition<I, A, R>> asEntry() {
        return Map.entry(this.name, this);
    }

    public static <I, A, R> OperationDefinition<I, A, R> operation(Class<I> inputType, Class<A> argType, Class<R> returnType, BiFunction<I, A, R> operation, String name) {
        return new OperationDefinition<>(inputType, argType, returnType, operation, name);
    }

    public static <I, R> OperationDefinition<I, Void, R> noArg(Class<I> inputType, Class<R> returnType, Function<I, R> operation, String name) {
        return new OperationDefinition<>(inputType, Void.class, returnType, (input, ignored) -> operation.apply(input), name);
    }
    public static <IAR> OperationDefinition<IAR, IAR, IAR> sameTypeOp(Class<IAR> type, BiFunction<IAR, IAR, IAR> operation, String name) {
        return operation(type, type, type, operation, name);
    }

    public static <IR, A> OperationDefinition<IR, A, IR> modifierOp(Class<IR> type, Class<A> argType, BiFunction<IR, A, IR> operation, String name) {
        return operation(type, argType, type, operation, name);
    }

    public static <IA, R> OperationDefinition<IA, IA, R> transformOp(Class<IA> type, Class<R> resultType, BiFunction<IA, IA, R> operation, String name) {
        return operation(type, type, resultType, operation, name);
    }
}