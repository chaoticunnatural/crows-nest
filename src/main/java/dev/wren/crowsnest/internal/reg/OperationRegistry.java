package dev.wren.crowsnest.internal.reg;

import dev.wren.crowsnest.internal.OperationDefinition;

import java.util.HashMap;
import java.util.Map;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class OperationRegistry {

    private static final Map<Class<?>, Map<String, OperationDefinition<?, ?, ?>>> operations = new HashMap<>();

    public static <I, A, R> void register(String name, OperationDefinition<I, A, R> operationDef) {
        LOGGER.info("Registering operation {} for {}", name, operationDef.inputType().getCanonicalName());

        operations.computeIfAbsent(operationDef.inputType(), k -> new HashMap<>()).put(name, operationDef);
    }

    public static <I> void registerAll(Class<I> inputType, Map<String, OperationDefinition<I, ?, ?>> operationDefs) {
        LOGGER.info("Registering {} operations for {}", operationDefs.size(), inputType.getCanonicalName());

        operations.computeIfAbsent(inputType, i -> new HashMap<>()).putAll(operationDefs);
    }

    public static Map<String, OperationDefinition<?, ?, ?>> getOperations(Class<?> type) {
        return operations.getOrDefault(type, Map.of());
    }

}
