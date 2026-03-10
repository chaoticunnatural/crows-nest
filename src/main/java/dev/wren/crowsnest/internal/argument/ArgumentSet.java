package dev.wren.crowsnest.internal.argument;

import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class ArgumentSet {

    private final Map<String, Object> values = new HashMap<>();

    public <T> void put(String key, T value) {
        values.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(getOrThrow(key));
    }

    public Float getFloat(String key) {
        return (Float) getOrThrow(key);
    }

    public Double getDouble(String key) {
        return (Double) getOrThrow(key);
    }

    public Vec3 getVec3(String key) {
        return (Vec3) getOrThrow(key);
    }

    private Object getOrThrow(String key) {
        Object value = values.get(key);
        if (value == null) throw new IllegalArgumentException("Argument " + key + " not found!");
        return value;
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public Map<String, Object> getAll() {
        return values;
    }

    public static ArgumentSet EMPTY() {
        return new ArgumentSet();
    }
}