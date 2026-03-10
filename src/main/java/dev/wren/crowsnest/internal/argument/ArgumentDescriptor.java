package dev.wren.crowsnest.internal.argument;

import java.util.List;
import java.util.function.Supplier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.core.impl.shadow.c;

import java.util.function.Function;

public class ArgumentDescriptor {

    private final String name;
    private final Class<?> type;
    private final ArgumentType<?> brigadierType;
    private final Function<CommandContext<CommandSourceStack>, Object> extractor;

    public ArgumentDescriptor(String name, Class<?> type, ArgumentType<?> brigadierType, Function<CommandContext<CommandSourceStack>, Object> extractor) {
        this.name = name;
        this.type = type;
        this.brigadierType = brigadierType;
        this.extractor = extractor;
    }

    public String name() {
        return name;
    }

    public Class<?> type() {
        return type;
    }

    public ArgumentType<?> brigadierType() {
        return brigadierType;
    }

    public Object extract(CommandContext<CommandSourceStack> ctx) {
        return extractor.apply(ctx);
    }

    public static ArgumentDescriptor intArg(String name) {
        return new ArgumentDescriptor(name, Integer.class, IntegerArgumentType.integer(), ctx -> IntegerArgumentType.getInteger(ctx, name));
    }

    public static ArgumentDescriptor floatArg(String name) {
        return new ArgumentDescriptor(name, Float.class, FloatArgumentType.floatArg(), ctx -> FloatArgumentType.getFloat(ctx, name));
    }

    public static ArgumentDescriptor doubleArg(String name) {
        return new ArgumentDescriptor(name, Double.class, DoubleArgumentType.doubleArg(), ctx -> DoubleArgumentType.getDouble(ctx, name));
    }

    public static ArgumentDescriptor vec3Arg(String name) {
        return new ArgumentDescriptor(name, Vec3.class, Vec3Argument.vec3(), ctx -> Vec3Argument.getVec3(ctx, name));
    }

    public static ArgumentDescriptor blockPosArg(String name) {
        return new ArgumentDescriptor(name, BlockPos.class, BlockPosArgument.blockPos(), ctx -> BlockPosArgument.getBlockPos(ctx, name));
    }

    public static ArgumentDescriptor axisArg(String name) {
        return new ArgumentDescriptor(name, Direction.Axis.class, AxisArgum)
    }

    public static <T> ArgumentDescriptor of(String name, Class<T> type, ArgumentType<T> brigadierType) {
        return new ArgumentDescriptor(name, type, brigadierType, ctx -> ctx.getArgument(name, type));
    }


    public static <T> Function<String, ArgumentDescriptor> of(Class<T> type, ArgumentType<T> brigadierType) {
        return name -> new ArgumentDescriptor(name, type, brigadierType, ctx -> ctx.getArgument(name, type));
    }
}