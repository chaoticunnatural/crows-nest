package dev.wren.crowsnest.internal.reg;

import dev.wren.crowsnest.internal.CommandNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.wren.crowsnest.CrowsNest.LOGGER;
import static dev.wren.crowsnest.internal.reg.TypeBridgeRegistry.getBridge;

@SuppressWarnings("unchecked")
public class TypeBranchRegistry {

    private static final Map<Class<?>, Consumer<CommandNode<?>>> ADAPTERS = new HashMap<>();

    public static <T> void registerAdapter(Class<T> type, Consumer<CommandNode<T>> adapter) {
        LOGGER.info("Registering adapter for {}", type.getCanonicalName());

        ADAPTERS.put(type, node -> adapter.accept((CommandNode<T>) node));
    }

    public static void applyIfPresent(Class<?> type, CommandNode<?> nodeBuilder) {
        if (!(TypeBridgeRegistry.contains(type) || ADAPTERS.containsKey(type))) return;

        Consumer<CommandNode<?>> adapter = ADAPTERS.get(type);

        if (adapter != null) {
            adapter.accept(nodeBuilder);
        }
    }


}
