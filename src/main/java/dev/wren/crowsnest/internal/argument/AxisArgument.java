package dev.wren.crowsnest.internal.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class AxisArgument implements ArgumentType<Direction.Axis> {

    private static final Collection<String> EXAMPLES = Arrays.asList("X", "Y", "Z");
    public static final SimpleCommandExceptionType INVALID_AXIS = new SimpleCommandExceptionType(Component.literal("Invalid axis!"));

    public static AxisArgument axis() {
        return new AxisArgument();
    }

    public static Direction.Axis getAxis(CommandContext<CommandSourceStack> pContext, String pName) {
        return Direction.Axis.valueOf(pContext.getArgument(pName, String.class));
    }


    @Override
    public Direction.Axis parse(final StringReader reader) throws CommandSyntaxException {
        try {
            return Direction.Axis.valueOf(reader.readString());
        } catch (IllegalArgumentException e) {
            throw INVALID_AXIS.create();
        }
    }


    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> pContext, SuggestionsBuilder pBuilder) {
        if (!(pContext.getSource() instanceof SharedSuggestionProvider)) {
            return Suggestions.empty();
        } else {
            String s = pBuilder.getRemaining();
            Collection<SharedSuggestionProvider.TextCoordinates> collection;
            if (!s.isEmpty() && s.charAt(0) == '^') {
                collection = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
            } else {
                collection = ((SharedSuggestionProvider)pContext.getSource()).getRelevantCoordinates();
            }

            return SharedSuggestionProvider.suggestCoordinates(s, collection, pBuilder, Commands.createValidator(this::parse));
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
