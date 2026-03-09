package dev.wren.crowsnest.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.wren.crowsnest.internal.*;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.operation.OperationNode;
import dev.wren.crowsnest.internal.pipeline.ImmutablePipeline;
import dev.wren.crowsnest.internal.pipeline.Pipeline;
import dev.wren.crowsnest.internal.pipeline.PipelineArgument;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static dev.wren.crowsnest.internal.FormatUtility.asCommandOutput;
import static dev.wren.crowsnest.CrowsNest.LOGGER;

/**
 * <a href="https://tenor.com/view/clueless-gif-24395495">:clueless:</a>
 */
public class DynamicShipInfoCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("ship")
            .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .then(Commands.argument("pipeline", PipelineArgument.pipeline())
                    .suggests((ctx, builder) -> {
                        PipelineArgument arg = new PipelineArgument();
                        return arg.listSuggestions(ctx, builder);
                    })
                    .executes(ctx -> {
                        BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                        LoadedShip ship = Utility.getShipAtPos(ctx.getSource().getUnsidedLevel(), pos);

                        String pipelineText = ctx.getArgument("pipeline", String.class);
                        Pipeline pipeline = Pipeline.parse(pipelineText);

                        Object value = ship;

                        for (OperationNode node : pipeline.getOperations()) {
                            OperationDefinition<?, ?, ?> op = node.getOperation();

                            Object typedInput = op.inputType().cast(value);

                            Object result = op.perform(typedInput, node.getArguments());

                            value = TypeBridgeRegistry.getBridge(result.getClass()).safeConvert(result);
                        }

                        Object finalValue = value;
                        ctx.getSource().sendSuccess(() -> asCommandOutput("result", finalValue), true);

                        return 1;
                    })
                ));
    }
}