package dev.wren.crowsnest.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.wren.crowsnest.internal.reg.TypeBranchRegistry;
import dev.wren.crowsnest.internal.reg.TypeBridgeRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.wren.crowsnest.internal.CommandUtility.shipHandle;

@SuppressWarnings("unchecked")
public class CommandNode<T> {

    private final String name;
    private final Function<LoadedShip, T> resolver;
    private boolean built = false;
    private final boolean directoryBranch;
    private LiteralArgumentBuilder<CommandSourceStack> cached;
    private Function<Object, Object> valueWrapper = Function.identity();

    private final List<CommandNode<?>> children = new ArrayList<>();

    private Class<T> type;


    public CommandNode(String name, Function<LoadedShip, T> resolver, Class<T> type) {
        this.name = name;
        this.resolver = resolver;
        this.directoryBranch = false;
        this.type = type;
    }

    public CommandNode(String name, Class<T> type) {
        this.name = name;
        this.resolver = null;
        this.directoryBranch = true;
        this.type = type;
    }


    public CommandNode<T> typeAdapter(Class<T> type) {
        this.type = type;
        return this;
    }

    public CommandNode<T> doubleAdapter() {
        this.type = (Class<T>) Double.class;
        return this;
    }

    public CommandNode<T> integerAdapter() {
        this.type = (Class<T>) Integer.class;
        return this;
    }

    public CommandNode<T> subCommands(Consumer<CommandNode<T>> consumer) {
        consumer.accept(this);
        return this;
    }


    public static <I, E> CommandNode<E> shipNode(String name, Function<LoadedShip, I> extractor, Class<I> type) {
        TypeBridgeRegistry.TypeBridge<I, E> bridge = (TypeBridgeRegistry.TypeBridge<I, E>) TypeBridgeRegistry.getBridge(type);

        Function<LoadedShip, E> resolver = t -> bridge.convert(extractor.apply(t));

        return new CommandNode<>(name, resolver, bridge.to());
    }

    public static <I, E> CommandNode<E> shipBranch(String name, Function<LoadedShip, I> extractor, Class<I> type, Consumer<CommandNode<E>> subCommands) {
        TypeBridgeRegistry.TypeBridge<I, E> bridge = (TypeBridgeRegistry.TypeBridge<I, E>) TypeBridgeRegistry.getBridge(type);

        Function<LoadedShip, E> resolver = t -> bridge.convert(extractor.apply(t));

        return new CommandNode<>(name, resolver, bridge.to()).subCommands(subCommands);
    }

    public <I, E> CommandNode<E> subNode(String name, Function<T, I> extractor, Class<E> type) {
        TypeBridgeRegistry.TypeBridge<I, E> bridge = (TypeBridgeRegistry.TypeBridge<I, E>) TypeBridgeRegistry.getBridge(type);

        Function<T, E> resolver = t -> bridge.convert(extractor.apply(t));

        return new CommandNode<>(name, ship -> resolver.apply(this.resolver.apply(ship)), type);
    }

    public <R> CommandNode<R> dirBranchNode(String name, Class<R> type, Consumer<CommandNode<R>> consumer) {
        CommandNode<R> childBranch = new CommandNode<>(name, type).subCommands(consumer);

        children.add(childBranch);

        return childBranch;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        if (built) return cached;

        LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal(name);

        TypeBranchRegistry.applyIfPresent(type, this);


        if (!directoryBranch) {
            node.executes(ctx -> shipHandle(ctx, resolver, name));
        } else {
            node.executes(ctx -> {
                ctx.getSource().sendFailure(Component.literal("No value specified!"));
                return 0;
            });
        }

        for (CommandNode<?> child : children) {
            node.then(child.build());
        }

        cached = node;
        built = true;

        return node;
    }
}