package dev.wren.crowsnest.internal.pipeline;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.wren.crowsnest.internal.argument.ArgumentParser;
import dev.wren.crowsnest.internal.argument.ArgumentSet;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.operation.OperationNode;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.ArrayList;
import java.util.List;


public class Pipeline {

    private final List<OperationNode> nodes;

    public Pipeline() {
        this.nodes = new ArrayList<>();
    }

    public Pipeline(List<OperationNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    public void addOperation(OperationNode node) {
        nodes.add(node);
    }

    public List<OperationNode> getOperations() {
        return nodes;
    }

    public Class<?> getCurrentOutputType(Class<?> rootType) {
        Class<?> type = rootType;
        for (OperationNode node : nodes) {
            OperationDefinition<?, ?, ?> op = node.getOperation();
            if (!op.inputType().isAssignableFrom(type)) {
                throw new IllegalStateException(
                        "Operation " + op.name() + " incompatible with " + type.getSimpleName()
                );
            }
            type = TypeBridgeRegistry.getBridge(op.returnType()).to();
        }
        return type;
    }

    public static Pipeline parse(String input) throws CommandSyntaxException {
        StringReader reader = new StringReader(input);
        Pipeline pipeline = new Pipeline();
        Class<?> currentType = LoadedShip.class;

        skipWhitespace(reader);

        while (reader.canRead()) {
            String opName = reader.readUnquotedString();
            OperationDefinition<?, ?, ?> op = OperationRegistry.getPossibleOperations(currentType, opName);
            if (op == null) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()
                        .createWithContext(reader, opName);
            }

            ArgumentSet args = ArgumentParser.parseArguments(reader, op.getArgumentDescriptors());
            pipeline.addOperation(new OperationNode(op, args));

            currentType = TypeBridgeRegistry.getBridge(op.returnType()).to();
            skipWhitespace(reader);
        }

        return pipeline;
    }


    public static Pipeline parsePartial(String input) {
        StringReader reader = new StringReader(input);
        Pipeline pipeline = new Pipeline();
        Class<?> currentType = LoadedShip.class;

        while (reader.canRead()) {
            skipWhitespace(reader);
            if (!reader.canRead()) break;

            String opName = reader.readUnquotedString();
            OperationDefinition<?, ?, ?> op = OperationRegistry.getOperation(currentType, opName);
            if (op == null) break;

            ArgumentSet args = ArgumentParser.parseArguments(reader, op.getArgumentDescriptors());
            pipeline.addOperation(new OperationNode(op, args));

            currentType = TypeBridgeRegistry.getBridge(op.returnType()).to();
        }

        return pipeline;
    }

    private static void skipWhitespace(StringReader reader) {
        while (reader.canRead() && Character.isWhitespace(reader.peek())) {
            reader.skip();
        }
    }
}