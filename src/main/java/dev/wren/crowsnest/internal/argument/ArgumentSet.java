package dev.wren.crowsnest.internal.argument;

import java.util.HashMap;
import java.util.Map;

public class ArgumentSet {

    private final Map<String, Object> values = new HashMap<>();

    public <T> void put(String key, T value) {
        values.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        Object val = values.get(key);
        if (val == null) {
            throw new IllegalStateException("Argument " + key + " not found");
        }
        return type.cast(val);
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public Map<String, Object> getAll() {
        return values;
    }
}