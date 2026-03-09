package dev.wren.crowsnest.internal.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

@SuppressWarnings("unchecked")
public class TypeBridgeRegistry {

    private static final Map<Class<?>, TypeBridge<?, ?>> BRIDGES = new HashMap<>();

    public static boolean contains(Class<?> key) {
        return BRIDGES.containsKey(key);
    }

    public static <F, T> void registerBridge(Class<F> from, Class<T> to, Function<F, T> converter) {
        LOGGER.info("Registering bridge from {} to {}", from.getCanonicalName(), to.getCanonicalName());

        BRIDGES.put(from, new TypeBridge<>(to, converter));
    }

    public static <F, T> TypeBridge<F, T> getBridge(Class<F> type) {
        for (Map.Entry<Class<?>, TypeBridge<?, ?>> entry : BRIDGES.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return (TypeBridge<F, T>) entry.getValue();
            }
        }
        return (TypeBridge<F, T>) BRIDGES.getOrDefault(type, TypeBridge.identity(type));
    }

    public static class TypeBridge<F, T> {
        private final Class<T> to;
        private final Function<F, T> converter;

        public TypeBridge(Class<T> to, Function<F, T> converter) {
            this.to = to;
            this.converter = converter;
        }

        public Class<T> to() {
            return to;
        }

        public T convert(F value) {
            return converter.apply(value);
        }

        public T safeConvert(Object value) {
            return converter.apply((F) value);
        }

        public static <V> TypeBridge<V, V> identity(Class<V> type) {
            return new TypeBridge<>(type, Function.identity());
        }

    }

}
