package dev.wren.crowsnest.internal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.function.Consumer;
import java.util.function.Function;

import static dev.wren.crowsnest.internal.FormatUtility.asCommandOutput;

public class CommandUtility {

    public static int shipHandle(CommandContext<CommandSourceStack> context, Function<LoadedShip, ?> func, String name) {
        LoadedShip ship = Utility.getShipAtPos(context.getSource().getUnsidedLevel(), BlockPosArgument.getBlockPos(context, "pos"));

        if (ship == null) {
            context.getSource().sendFailure(Component.literal("No ship found!"));
            return 0;
        }

        Object result = func.apply(ship);

        context.getSource().sendSuccess(() -> asCommandOutput(name, result) , false);

        return Command.SINGLE_SUCCESS;
    }
}
