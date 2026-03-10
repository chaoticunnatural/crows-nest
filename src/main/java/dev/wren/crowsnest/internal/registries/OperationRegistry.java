package dev.wren.crowsnest.internal.registries;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.wren.crowsnest.internal.operation.OperationBuilder;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import net.minecraft.commands.SharedSuggestionProvider;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class OperationRegistry {

    private static final Map<Class<?>, Map<String, OperationDefinition<?, ?>>> operations = new HashMap<>();

    public static <I, R> void register(Class<I> inputType, OperationDefinition<I, R> operationDef) {
        LOGGER.info("Registering operation {} for {}", operationDef.name(), operationDef.inputType().getCanonicalName());

        operations.computeIfAbsent(inputType, k -> new HashMap<>()).put(operationDef.name(), operationDef);
    }

    public static <I> void registerAll(Class<I> inputType, Map<String, OperationDefinition<I, ?>> operationDefs) {
        LOGGER.info("Registering {} operations for {}", operationDefs.size(), inputType.getCanonicalName());

        operations.computeIfAbsent(inputType, k -> new HashMap<>()).putAll(operationDefs);
    }

    public static <T> void forType(Class<T> type, Consumer<OperationBuilder<T>> consumer) {
        OperationBuilder<T> builder = new OperationBuilder<>(type);
        consumer.accept(builder);
        builder.register();
    }

    public static @Nullable OperationDefinition<?, ?> getOperation(Class<?> type, String name) {
        return getOperations(type).stream().filter(opDef -> opDef.name().equals(name)).findFirst().orElse(null);
    }

    public static Collection<String> suggestOperations(Class<?> type, String partial) {
        return getOperations(type).stream()
                .map(OperationDefinition::name)
                .filter(n -> n.startsWith(partial))
                .toList();
    }

    public static Collection<OperationDefinition<?, ?>> getOperations(Class<?> type) {
        List<OperationDefinition<?, ?>> result = new ArrayList<>();

        for (Map.Entry<Class<?>, Map<String, OperationDefinition<?, ?>>> entry : operations.entrySet()) {

            Class<?> registeredType = entry.getKey();

            if (registeredType.isAssignableFrom(type)) {
                result.addAll(entry.getValue().values());
            }
        }

        return result;
    }

    public static <I, S> CompletableFuture<Suggestions> listSuggestions(Class<I> type, CommandContext<S> ctx, SuggestionsBuilder builder) {
        List<String> names = new ArrayList<>();

        for (OperationDefinition<?, ?> op : OperationRegistry.getOperations(type)) {
            names.add(op.name());
        }

        return SharedSuggestionProvider.suggest(names, builder);
    }

}
