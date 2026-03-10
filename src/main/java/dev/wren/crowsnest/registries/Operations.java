package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.argument.ArgumentDescriptor;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.bodies.properties.BodyKinematics;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.core.api.ships.properties.ChunkClaim;
import org.valkyrienskies.mod.mixin.feature.commands.MixinArgumentTypeInfos;

import java.util.EnumSet;
import java.util.Map;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class Operations {

    public static void register() {
        LOGGER.info("Registering operations...");

        OperationRegistry.forType(LoadedShip.class, opBuilder -> {
            opBuilder.noArg(Long.class, LoadedShip::getId, "id");
            opBuilder.noArg(String.class, LoadedShip::getSlug, "slug");
            opBuilder.noArg(AABBic.class, LoadedShip::getShipAABB, "shipAABB");
            opBuilder.noArg(AABBdc.class, LoadedShip::getWorldAABB, "worldAABB");
            opBuilder.noArg(BodyKinematics.class, LoadedShip::getKinematics, "kinematics");
            opBuilder.noArg(String.class, LoadedShip::getChunkClaimDimension, "chunkClaimDimension");
            opBuilder.noArg(ChunkClaim.class, LoadedShip::getChunkClaim, "chunkClaim");
        });

        OperationRegistry.forType(AABB.class, opBuilder -> {
            opBuilder.noArg(Double.class, AABB::getSize, "size");
            opBuilder.noArg(Double.class, AABB::getXsize, "xSize");
            opBuilder.noArg(Double.class, AABB::getYsize, "ySize");
            opBuilder.noArg(Double.class, AABB::getZsize, "zSize");
            opBuilder.noArg(Vec3.class, AABB::getCenter, "center");
        });

        OperationRegistry.forType(Vec3.class, opBuilder -> {
            opBuilder.noArg(Double.class, Vec3::x, "X");
            opBuilder.noArg(Double.class, Vec3::y, "Y");
            opBuilder.noArg(Double.class, Vec3::z, "Z");
            opBuilder.noArg(Double.class, Vec3::length, "length");
            opBuilder.noArg(Double.class, Vec3::lengthSqr, "lengthSqr");
            opBuilder.noArg(Double.class, Vec3::horizontalDistance, "horizontalDistance");
            opBuilder.noArg(Double.class, Vec3::horizontalDistanceSqr, "horizontalDistanceSqr");
            opBuilder.noArg(Vec3.class, Vec3::normalize, "normalize");
            opBuilder.noArg(Vec3.class, Vec3::reverse, "reverse");

            opBuilder.sameTypeOp(Vec3::add, "add", ArgumentDescriptor::vec3Arg);
            opBuilder.sameTypeOp(Vec3::subtract, "subtract", ArgumentDescriptor::vec3Arg);
            opBuilder.sameTypeOp(Vec3::multiply, "multiply", ArgumentDescriptor::vec3Arg);
            opBuilder.sameTypeOp(Vec3::cross, "cross", ArgumentDescriptor::vec3Arg);
            opBuilder.sameTypeOp(Vec3::vectorTo, "vectorTo", ArgumentDescriptor::vec3Arg);

            opBuilder.modifierOp(Double.class, Vec3::scale, "scale", ArgumentDescriptor::doubleArg);
            opBuilder.modifierOp(Float.class, Vec3::xRot, "xRot", ArgumentDescriptor::floatArg);
            opBuilder.modifierOp(Float.class, Vec3::yRot, "yRot", ArgumentDescriptor::floatArg);
            opBuilder.modifierOp(Float.class, Vec3::zRot, "zRot", ArgumentDescriptor::floatArg);

            opBuilder.transformOp(Double.class, Vec3::distanceTo, "distanceTo", ArgumentDescriptor::vec3Arg);
            opBuilder.transformOp(Double.class, Vec3::distanceToSqr, "distanceToSqr", ArgumentDescriptor::vec3Arg);
            opBuilder.transformOp(Double.class, Vec3::dot, "dot", ArgumentDescriptor::vec3Arg);
        });
    }
}
