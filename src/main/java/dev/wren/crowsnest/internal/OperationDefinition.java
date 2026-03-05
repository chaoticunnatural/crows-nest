package dev.wren.crowsnest.internal;


import java.util.Map;
import java.util.function.BiFunction;

public class OperationDefinition<I, A, R> {

    private final Class<I> inputType;
    private final Class<A> argType;
    private final Class<R> returnType;
    private final BiFunction<I, A, R> operation;

    public OperationDefinition(Class<I> inputType, Class<A> argType, Class<R> returnType, BiFunction<I, A, R> operation) {
        this.inputType = inputType;
        this.argType = argType;
        this.returnType = returnType;
        this.operation = operation;
    }

    public Class<I> inputType() { return inputType; }
    public Class<A> argType() { return argType; }
    public Class<R> returnType() { return returnType; }

    public R perform(I input, A arg) {
        return operation.apply(input, arg);
    }

    public Map.Entry<String, OperationDefinition<I, A, R>> asEntry(String name) {
        return Map.entry(name, this);
    }

    public static <I, A, R> OperationDefinition<I, A, R> operation(Class<I> inputType, Class<A> argType, Class<R> returnType, BiFunction<I, A, R> operation) {
        return new OperationDefinition<>(inputType, argType, returnType, operation);
    }

    public static <IAR> OperationDefinition<IAR, IAR, IAR> sameTypeOp(Class<IAR> type, BiFunction<IAR, IAR, IAR> operation) {
        return operation(type, type, type, operation);
    }

    public static <IR, A> OperationDefinition<IR, A, IR> modifierOp(Class<IR> type, Class<A> argType, BiFunction<IR, A, IR> operation) {
        return operation(type, argType, type, operation);
    }

    public static <IA, R> OperationDefinition<IA, IA, R> transformOp(Class<IA> type, Class<R> resultType, BiFunction<IA, IA, R> operation) {
        return operation(type, type, resultType, operation);
    }
}