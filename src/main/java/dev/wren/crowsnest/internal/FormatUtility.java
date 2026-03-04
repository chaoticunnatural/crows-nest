package dev.wren.crowsnest.internal;

import dev.wren.crowsnest.registries.TypeFormatterRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.text.DecimalFormat;

public class FormatUtility {

    public static MutableComponent asCommandOutput(String name, Object result) {
        MutableComponent prefix = Component.literal("Found " + result.getClass().getSimpleName() + " for " + name + "\n");
        return prefix.append(TypeFormatterRegistry.format(result));
    }

    private static final DecimalFormat NORMAL_FORMAT =
            new DecimalFormat("#,##0.###");

    private static final DecimalFormat SMALL_FORMAT =
            new DecimalFormat("0.#####");

    private static final DecimalFormat SCIENTIFIC_FORMAT =
            new DecimalFormat("0.###E0");

    public static MutableComponent formatNumber(double value) {

        double abs = Math.abs(value);

        if (abs == 0) return Component.literal("0");

        if (abs >= 1_000_000_000) {
            return Component.literal(SCIENTIFIC_FORMAT.format(value));
        }

        if (abs >= 0.001) {
            return Component.literal(NORMAL_FORMAT.format(value));
        }

        return Component.literal(SMALL_FORMAT.format(value));
    }

    public static final MutableComponent SEP = Component.literal(", ").withStyle(ChatFormatting.WHITE);
    public static final MutableComponent NEWLINE = Component.literal("\n");

    public static MutableComponent literalWithStyle(String text, ChatFormatting... style) {
        return Component.literal(text).withStyle(style);
    }

    public static MutableComponent formatXYZ(double xd, double yd, double zd) {
        String x = formatNumber(xd).getString();
        String y = formatNumber(yd).getString();
        String z = formatNumber(zd).getString();
        return formatXYZ(x, y, z);
    }

    public static MutableComponent formatXYZ(String x, String y, String z) {
        return Component.literal("(")
                .append(literalWithStyle(x, ChatFormatting.RED))
                .append(SEP)
                .append(literalWithStyle(y, ChatFormatting.GREEN))
                .append(SEP)
                .append(literalWithStyle(z, ChatFormatting.BLUE))
                .append(Component.literal(")"));
    }

}
