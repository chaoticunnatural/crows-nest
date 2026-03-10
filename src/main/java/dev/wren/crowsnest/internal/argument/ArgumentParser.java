package dev.wren.crowsnest.internal.argument;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.List;


public class ArgumentParser {

    public static ArgumentSet parseArguments(CommandContext<CommandSourceStack> ctx, List<ArgumentDescriptor> descriptors) {
        ArgumentSet set = new ArgumentSet();

        for (ArgumentDescriptor desc : descriptors) {
            Object value = desc.extract(ctx);
            set.put(desc.name(), value);
        }

        return set;
    }
}