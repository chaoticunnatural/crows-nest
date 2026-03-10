package dev.wren.crowsnest.internal.operation;

import dev.wren.crowsnest.internal.argument.ArgumentDescriptor;
import dev.wren.crowsnest.internal.argument.ArgumentSet;
import dev.wren.crowsnest.internal.registries.OperationRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OperationBuilder<I> {

    private final Class<I> type;
    private final Map<String, OperationDefinition<I, ?>> opDefs = new HashMap<>();

    public OperationBuilder(Class<I> type) {
        this.type = type;
    }

    public <R> OperationBuilder<I> operation(Class<R> returnType, BiFunction<I, ArgumentSet, R> operation, String name) {
        opDefs.put(name, new OperationDefinition<>(type, returnType, operation, name));
        return this;
    }

    private <R> OperationBuilder<I> operation(Class<R> resultType, BiFunction<I, ArgumentSet, R> operation, String name, ArgumentDescriptor... args) {
        opDefs.put(name, new OperationDefinition<>(type, resultType, operation, name, List.of(args)));
        return this;
    }

    /**
     * Represents a basic getter method
     * @param returnType the type of value returned
     * @param operation the getter function
     * @param name name of the operation
     * @return this
     * @param <R> the return type
     */
    public <R> OperationBuilder<I> noArg(Class<R> returnType, Function<I, R> operation, String name) {
        return operation(returnType, (input, ignored) -> operation.apply(input), name);
    }

    /**
     * an operation that takes a value and applies it to another value of the same type, returning that same type
     * @param operation the operation
     * @param name name of the operation
     * @return this
     */
    public OperationBuilder<I> sameTypeOp(BiFunction<I, I, I> operation, String name, Function<String, ArgumentDescriptor> argumentDescriptor) {
        return operation(type, (i, argumentSet) -> operation.apply(i, argumentSet.get("value", type)), name, argumentDescriptor.apply("value"));
    }

    /**
     * an operation that modifies the given values by another value
     * @param operation the operation
     * @param name name of the operation
     * @return this
     * @param <A> the argument type
     */
    public <A> OperationBuilder<I> modifierOp(Class<A> argType, BiFunction<I, A, I> operation, String name, Function<String, ArgumentDescriptor> argumentDescriptor) {
        return operation(type, (i, args) -> operation.apply(i, args.get("value", argType)), name, argumentDescriptor.apply("value"));
    }

    /**
     * an operation that transforms the given value using another of the same type
     * @param resultType the type of the resulting value
     * @param operation the operation
     * @param name name of the operation
     * @return this
     * @param <R> the result type
     */
    public <R> OperationBuilder<I> transformOp(Class<R> resultType, BiFunction<I, I, R> operation, String name, Function<String, ArgumentDescriptor> argumentDescriptor) {
        return operation(resultType, (i, args) -> operation.apply(i, args.get("value", type)), name, argumentDescriptor.apply("value"));
    }

    public void register() {
        OperationRegistry.registerAll(type, opDefs);
    }
}