package dev.wren.crowsnest.registries;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.checkerframework.checker.units.qual.C;
import org.valkyrienskies.core.impl.shadow.Co;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TypeFormatterRegistry {

    private static final Map<Class<?>, Function<Object, MutableComponent>> FORMATTERS = new HashMap<>();

    public static <T> void registerFormatter(Class<T> type, Function<T, MutableComponent> formatter) {
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
