package dev.wren.crowsnest.registries;

import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.joml.Quaterniond;

import dev.wren.crowsnest.internal.registries.TypeFormatterRegistry;
import org.valkyrienskies.core.impl.bodies.properties.BodyKinematicsImpl;
import org.valkyrienskies.core.impl.game.ChunkClaimImpl;

import static dev.wren.crowsnest.internal.FormatUtility.*;

public class TypeFormatters {

    public static void register() {
        TypeFormatterRegistry.registerFormatter(AABB.class, ((aabb, builder) ->
                builder.format("Min: ", ChatFormatting.WHITE)
                        .format(formatXYZPosition(aabb.minX, aabb.minY, aabb.minZ))
                        .format(NEWLINE)
                        .format("Max: ", ChatFormatting.WHITE)
                        .format(formatXYZPosition(aabb.maxX, aabb.maxY, aabb.maxZ))
                        .format(NEWLINE)
                        .format("Size: ", ChatFormatting.WHITE)
                        .format(formatXYZ(aabb.getXsize(), aabb.getYsize(), aabb.getZsize()))
                        .format(NEWLINE)
                        .format("Volume: ")
                        .format(formatNumber(aabb.getXsize() * aabb.getYsize() * aabb.getZsize()), ChatFormatting.YELLOW)
                        .build()
        ));

        TypeFormatterRegistry.registerFormatter(Vec3.class, ((vec3, builder) ->
                builder.format(formatVec3(vec3)).build()
        ));

        TypeFormatterRegistry.registerFormatter(Quaterniond.class, (qdc, builder) ->
                builder.format(formatQuaternion(qdc)).build()
        );

        TypeFormatterRegistry.registerFormatter(ChunkClaimImpl.class, (cc, builder) ->
                builder.format("Start: ", ChatFormatting.WHITE)
                        .format(formatXZPosition(cc.getXStart(), cc.getZStart()))
                        .format(NEWLINE)
                        .format("Middle: ", ChatFormatting.WHITE)
                        .format(formatXZPosition(cc.getXMiddle(), cc.getZMiddle()))
                        .format(NEWLINE)
                        .format("End: ", ChatFormatting.WHITE)
                        .format(formatXZPosition(cc.getXEnd(), cc.getXEnd()))
                        .format(NEWLINE)
                        .format("Index: ", ChatFormatting.WHITE)
                        .format(formatXZPosition(cc.getXIndex(), cc.getZIndex()))
                        .build()
        );

        TypeFormatterRegistry.registerFormatter(BodyKinematicsImpl.class, (bk, builder) ->
                builder.format("Position: ", ChatFormatting.WHITE)
                        .format(formatXYZ(bk.getPosition()))
                        .format(NEWLINE)
                        .format("Rotation: ", ChatFormatting.WHITE)
                        .format(formatQuaternion(bk.getRotation()))
                        .format(NEWLINE)
                        .format("Velocity: ", ChatFormatting.WHITE)
                        .format(formatVec3(bk.getVelocity()))
                        .format(NEWLINE)
                        .format("Angular Velocity: ", ChatFormatting.WHITE)
                        .format(formatVec3(bk.getAngularVelocity()))
                        .format(NEWLINE)
                        .format("Scaling: ",  ChatFormatting.WHITE)
                        .format(formatXYZ(bk.getScaling()))
                        .build()
        );
    }
}
