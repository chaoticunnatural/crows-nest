package dev.wren.crowsnest.internal.pipeline;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.concurrent.CompletableFuture;

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

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
        String input = builder.getInput();
        int cursor = builder.getStart();

        String partialInput = input.substring(0, cursor);
        String[] tokens = partialInput.split("\\s+");

        Class<?> currentType = LoadedShip.class;
        int index = 0;

        while (index < tokens.length - 1) {

            String opName = tokens[index];

            OperationDefinition<?, ?, ?> op = OperationRegistry.getOperation(currentType, opName);

            if (op == null) {
                break;
            }

            int argCount = op.getArgumentDescriptors().size();
            index += 1 + argCount;

            currentType = TypeBridgeRegistry.getBridge(op.returnType()).to();
        }

        int lastSpace = partialInput.lastIndexOf(' ');
        int offset = lastSpace + 1;

        SuggestionsBuilder newBuilder = builder.createOffset(offset);

        return OperationRegistry.listSuggestions(currentType, ctx, newBuilder);
    }
}