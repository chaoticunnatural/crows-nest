package dev.wren.crowsnest.internal.registries;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class TypeFormatterRegistry {

    private static final Map<Class<?>, Function<Object, MutableComponent>> FORMATTERS = new HashMap<>();

    public static <T> void registerFormatter(Class<T> type, Function<T, MutableComponent> formatter) {
        LOGGER.info("Registering formatter for {}", type.getCanonicalName());

        FORMATTERS.put(type, object -> formatter.apply(type.cast(object)));
    }

    public static MutableComponent format(Object object, ChatFormatting... style) {
        if (object == null) return Component.literal("null");

        Class<?> objClass = object.getClass();

        Function<Object, MutableComponent> formatter = FORMATTERS.get(objClass);
        if (formatter != null)
            return formatter.apply(object);

        return Component.literal(object.toString()).withStyle(style);
    }
}
