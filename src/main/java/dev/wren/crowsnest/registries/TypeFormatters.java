package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.FormatUtility;
import dev.wren.crowsnest.internal.registries.TypeFormatterRegistry;
import kotlin.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static dev.wren.crowsnest.CrowsNest.LOGGER;
import static dev.wren.crowsnest.internal.FormatUtility.*;

public class TypeFormatters {


    public static void register() {
        LOGGER.info("Registering type formatters...");
        TypeFormatterRegistry.registerFormatter(AABB.class, aabb -> {
            Map<String, ChatFormatting[]> parts = new HashMap<>();

            parts.putAll(pieceMap("Min: ", ChatFormatting.WHITE));
            parts.putAll(formatXYZPosition(aabb.minX, aabb.minY, aabb.minZ));
            parts.putAll(Map.ofEntries(NEWLINE, piece("Max: ", ChatFormatting.WHITE)));
            parts.putAll(formatXYZPosition(aabb.maxX, aabb.maxY, aabb.maxZ));
            parts.putAll(Map.ofEntries(NEWLINE, piece("Size: ", ChatFormatting.WHITE)));
            parts.putAll(formatXYZ(aabb.getXsize(), aabb.getYsize(), aabb.getZsize()));
            parts.putAll(Map.ofEntries(NEWLINE, piece("Volume: ", ChatFormatting.WHITE)));
            parts.putAll(pieceMap(formatNumber(aabb.getXsize() * aabb.getYsize() * aabb.getZsize())));

            return makeComponent(parts);
        });

        TypeFormatterRegistry.registerFormatter(Vec3.class, vec3 -> {
            Map<String, ChatFormatting[]> parts = new HashMap<>();
            parts.putAll(formatXYZ(vec3.x, vec3.y, vec3.z));
            parts.putAll(Map.ofEntries(
                    NEWLINE,
                    piece("Length: ", ChatFormatting.WHITE),
                    piece(formatNumber(vec3.length()), ChatFormatting.YELLOW)
            ));

            return makeComponent(parts);
        });
    }

    public static Map<String, ChatFormatting[]> formatXYZ(double x, double y, double z) {
        return Map.ofEntries(
                piece("X: ", ChatFormatting.WHITE),
                piece(FormatUtility.formatNumber(x), ChatFormatting.RED),
                SEP,
                piece("Y: ", ChatFormatting.WHITE),
                piece(FormatUtility.formatNumber(y), ChatFormatting.GREEN),
                SEP,
                piece("Z: ", ChatFormatting.WHITE),
                piece(FormatUtility.formatNumber(z), ChatFormatting.BLUE)
        );
    }

    public static Map<String, ChatFormatting[]> formatXYZPosition(double x, double y, double z) {
        return Map.ofEntries(
                piece("(", ChatFormatting.WHITE),
                piece(FormatUtility.formatNumber(x), ChatFormatting.RED),
                SEP,
                piece(FormatUtility.formatNumber(y), ChatFormatting.GREEN),
                SEP,
                piece(FormatUtility.formatNumber(z), ChatFormatting.BLUE),
                piece(")", ChatFormatting.WHITE)
        );
    }

    public static Map.Entry<String, ChatFormatting[]> NEWLINE = piece("\n", ChatFormatting.RESET);

    public static Map.Entry<String, ChatFormatting[]> SEP = piece(", ", ChatFormatting.WHITE);

    public static Map.Entry<String, ChatFormatting[]> piece(String content, ChatFormatting... styles) {
        return Map.entry(content, styles);
    }

    public static Map<String, ChatFormatting[]> pieceMap(String content, ChatFormatting... styles) {
        return Map.of(content, styles);
    }

    public static Map.Entry<String, ChatFormatting[]> piece(Component content, ChatFormatting... styles) {
        return Map.entry(content.getString(), styles);
    }

    public static Map<String, ChatFormatting[]> pieceMap(Component content, ChatFormatting... styles) {
        return Map.of(content.getString(), styles);
    }

    public static MutableComponent makeComponent(Map<String, ChatFormatting[]> components) {
        MutableComponent initial = Component.literal("");
        for (Map.Entry<String, ChatFormatting[]> entry : components.entrySet()) {
            initial.append(Component.literal(entry.getKey()).withStyle(entry.getValue()));
        }

        return initial;
    }

}
