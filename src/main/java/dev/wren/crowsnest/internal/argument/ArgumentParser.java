package dev.wren.crowsnest.internal.argument;

import com.mojang.brigadier.StringReader;

import java.util.List;


public class ArgumentParser {

    public static ArgumentSet parseArguments(StringReader reader, List<ArgumentDescriptor> descriptors) {
        ArgumentSet set = new ArgumentSet();

        for (ArgumentDescriptor desc : descriptors) {
            skipWhitespace(reader);

            if (!reader.canRead()) {
                if (!desc.optional()) {
                    throw new IllegalStateException("Missing required argument: " + desc.name());
                }
                continue; // optional, skip
            }

            Object value = parseValue(reader, desc.type());
            set.put(desc.name(), value);
        }

        return set;
    }


    private static Object parseValue(StringReader reader, Class<?> type) {
        skipWhitespace(reader);

        int start = reader.getCursor();
        while (reader.canRead() && !Character.isWhitespace(reader.peek())) {
            reader.skip();
        }

        String token = reader.getString().substring(start, reader.getCursor());

        return convertToken(token, type);
    }

    private static Object convertToken(String token, Class<?> type) {
        if (type == String.class) return token;
        if (type == int.class || type == Integer.class) return Integer.parseInt(token);
        if (type == double.class || type == Double.class) return Double.parseDouble(token);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(token);

        throw new IllegalArgumentException("Unsupported argument type: " + type.getSimpleName());
    }

    private static void skipWhitespace(StringReader reader) {
        while (reader.canRead() && Character.isWhitespace(reader.peek())) reader.skip();
    }
}