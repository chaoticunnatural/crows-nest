package dev.wren.crowsnest.internal;

import dev.wren.crowsnest.internal.registries.TypeFormatterRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

import java.text.DecimalFormat;

import static dev.wren.crowsnest.internal.registries.TypeFormatterRegistry.Format.of;

public class FormatUtility {

    public static TypeFormatterRegistry.Format[] formatVec3(Vec3 vec) {
        return new TypeFormatterRegistry.Format[]{
                of("X: ", ChatFormatting.WHITE),
                of(formatNumber(vec.x), ChatFormatting.RED),
                SEP,
                of("Y: ", ChatFormatting.WHITE),
                of(formatNumber(vec.y), ChatFormatting.GREEN),
                SEP,
                of("Z: ", ChatFormatting.WHITE),
                of(formatNumber(vec.z), ChatFormatting.BLUE),
                NEWLINE,
                of("Length: ", ChatFormatting.WHITE),
                of(formatNumber(vec.length()), ChatFormatting.YELLOW)
        };
    }

    public static TypeFormatterRegistry.Format[] formatVec3(Vector3dc vector3dc) {
        return formatVec3(new Vec3(vector3dc.x(), vector3dc.y(), vector3dc.z()));
    }

    public static TypeFormatterRegistry.Format[] formatQuaternion(Quaterniondc qdc) {
        return new TypeFormatterRegistry.Format[]{
                of("X: ", ChatFormatting.WHITE),
                of(formatNumber(qdc.x()), ChatFormatting.RED),
                SEP,
                of("Y: ", ChatFormatting.WHITE),
                of(formatNumber(qdc.y()), ChatFormatting.GREEN),
                SEP,
                of("Z: ", ChatFormatting.WHITE),
                of(formatNumber(qdc.z()), ChatFormatting.BLUE),
                SEP,
                of("W: ", ChatFormatting.WHITE),
                of(formatNumber(qdc.w()), ChatFormatting.YELLOW),
                NEWLINE,
                of("Angle: ", ChatFormatting.WHITE),
                of(formatNumber(qdc.angle()), ChatFormatting.GOLD)
        };
    }

    public static TypeFormatterRegistry.Format[] formatXYZ(Vec3 vec) {
        return formatXYZ(vec.x, vec.y, vec.z);
    }

    public static TypeFormatterRegistry.Format[] formatXYZ(Vector3dc vec) {
        return formatXYZ(vec.x(), vec.y(), vec.z());
    }

    public static TypeFormatterRegistry.Format[] formatXYZW(Quaterniondc qdc) {
        return formatXYZW(qdc.x(), qdc.y(), qdc.z(), qdc.w());
    }

    public static TypeFormatterRegistry.Format[] formatXYZ(double x, double y, double z) {
        return new TypeFormatterRegistry.Format[]{
                of("X: ", ChatFormatting.WHITE),
                of(formatNumber(x), ChatFormatting.RED),
                SEP,
                of("Y: ", ChatFormatting.WHITE),
                of(formatNumber(y), ChatFormatting.GREEN),
                SEP,
                of("Z: ", ChatFormatting.WHITE),
                of(formatNumber(z), ChatFormatting.BLUE)
        };
    }

    public static TypeFormatterRegistry.Format[] formatXYZW(double x, double y, double z, double w) {
        return new TypeFormatterRegistry.Format[]{
                of("X: ", ChatFormatting.WHITE),
                of(formatNumber(x), ChatFormatting.RED),
                SEP,
                of("Y: ", ChatFormatting.WHITE),
                of(formatNumber(y), ChatFormatting.GREEN),
                SEP,
                of("Z: ", ChatFormatting.WHITE),
                of(formatNumber(z), ChatFormatting.BLUE),
                SEP,
                of("W: ", ChatFormatting.WHITE),
                of(formatNumber(w), ChatFormatting.YELLOW)
        };
    }

    public static TypeFormatterRegistry.Format[] formatXYZPosition(double x, double y, double z) {
        return new TypeFormatterRegistry.Format[]{
                of("(", ChatFormatting.WHITE),
                of(formatNumber(x), ChatFormatting.RED),
                SEP,
                of(formatNumber(y), ChatFormatting.GREEN),
                SEP,
                of(formatNumber(z), ChatFormatting.BLUE),
                of(")", ChatFormatting.WHITE)
        };
    }

    public static TypeFormatterRegistry.Format[] formatXZ(double x, double z) {
        return new TypeFormatterRegistry.Format[]{
                of("X: ", ChatFormatting.WHITE),
                of(formatNumber(x), ChatFormatting.RED),
                SEP,
                of("Z: ", ChatFormatting.WHITE),
                of(formatNumber(z), ChatFormatting.BLUE)
        };
    }

    public static TypeFormatterRegistry.Format[] formatXZPosition(double x, double z) {
        return new TypeFormatterRegistry.Format[]{
                of("(", ChatFormatting.WHITE),
                of(formatNumber(x), ChatFormatting.RED),
                SEP,
                of(formatNumber(z), ChatFormatting.BLUE),
                of(")", ChatFormatting.WHITE)
        };
    }

    public static TypeFormatterRegistry.Format NEWLINE = of("\n", ChatFormatting.WHITE);

    public static TypeFormatterRegistry.Format SEP = of(", ", ChatFormatting.WHITE);

    private static final DecimalFormat NORMAL_FORMAT = new DecimalFormat("#,##0.###");

    private static final DecimalFormat SMALL_FORMAT = new DecimalFormat("0.#####");

    private static final DecimalFormat SCIENTIFIC_FORMAT = new DecimalFormat("0.###E0");

    public static String formatNumber(double value) {

        double abs = Math.abs(value);

        if (abs == 0) return "0";

        if (abs >= 1_000_000_000) {
            return SCIENTIFIC_FORMAT.format(value);
        }

        if (abs >= 0.001) {
            return NORMAL_FORMAT.format(value);
        }

        return SMALL_FORMAT.format(value);
    }

}
