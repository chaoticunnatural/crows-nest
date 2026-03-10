package dev.wren.crowsnest.internal.pipeline;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.StringReader;

public class PipelineArgument implements ArgumentType<String> {

    public static PipelineArgument pipeline() {
        return new PipelineArgument();
    }

    @Override
    public String parse(StringReader reader) {
        int start = reader.getCursor();
        while (reader.canRead()) reader.skip();
        return reader.getString().substring(start, reader.getCursor());
    }
}