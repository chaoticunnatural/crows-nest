package dev.wren.crowsnest.registries;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.joml.Matrix4dc;
import org.joml.Matrix4f;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.bodies.properties.BodyKinematics;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.core.api.ships.properties.ChunkClaim;

import dev.wren.crowsnest.internal.operation.ArgumentSet;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import org.valkyrienskies.core.impl.bodies.properties.BodyKinematicsImpl;
import org.valkyrienskies.core.impl.game.ChunkClaimImpl;

public class Operations {

    public static void register() {
        registerVS();
        registerVec3();

        OperationRegistry.forType(AABB.class, opBuilder ->
            opBuilder

            .noArg(Double.class, AABB::getSize, "size")
            .noArg(Double.class, AABB::getXsize, "xSize")
            .noArg(Double.class, AABB::getYsize, "ySize")
            .noArg(Double.class, AABB::getZsize, "zSize")
            .noArg(Vec3.class, AABB::getCenter, "center")
        );
    }

    public static void registerVec3() {
        OperationRegistry.forType(Vec3.class, opBuilder ->
                opBuilder

                        .noArg(Double.class, Vec3::x, "x")
                        .noArg(Double.class, Vec3::y, "y")
                        .noArg(Double.class, Vec3::z, "z")
                        .noArg(Double.class, Vec3::length, "length")
                        .noArg(Double.class, Vec3::lengthSqr, "lengthSqr")
                        .noArg(Double.class, Vec3::horizontalDistance, "horizontalDistance")
                        .noArg(Double.class, Vec3::horizontalDistanceSqr, "horizontalDistanceSqr")
                        .noArg(Vec3.class, Vec3::normalize, "normalize")
                        .noArg(Vec3.class, Vec3::reverse, "reverse")

                        .operation(Vec3.class, ((v, a) -> v.add(a.getVec3("vec3"))), "add", ArgumentSet.vec3("vec3"))
                        .operation(Vec3.class, ((v, a) -> v.subtract(a.getVec3("vec3"))), "subtract", ArgumentSet.vec3("vec3"))
                        .operation(Vec3.class, ((v, a) -> v.multiply(a.getVec3("vec3"))), "multiply", ArgumentSet.vec3("vec3"))
                        .operation(Vec3.class, ((v, a) -> v.cross(a.getVec3("vec3"))), "cross", ArgumentSet.vec3("vec3"))
                        .operation(Vec3.class, ((v, a) -> v.vectorTo(a.getVec3("vec3"))), "vectorTo", ArgumentSet.vec3("vec3"))

                        .operation(Vec3.class, (v, a) -> v.scale(a.getDouble("scalar")), "scale", ArgumentSet.d("scalar"))
                        .operation(Vec3.class, (v, a) -> v.xRot(a.getFloat("pitch")), "xRot", ArgumentSet.f("pitch"))
                        .operation(Vec3.class, (v, a) -> v.yRot(a.getFloat("yaw")), "yRot", ArgumentSet.f("yaw"))
                        .operation(Vec3.class, (v, a) -> v.zRot(a.getFloat("roll")), "zRot", ArgumentSet.f("roll"))

                        .operation(Double.class, (v, a) -> v.distanceTo(a.getVec3("vec3")), "distanceTo", ArgumentSet.vec3("vec3"))
                        .operation(Double.class, (v, a) -> v.distanceToSqr(a.getVec3("vec3")), "distanceToSqr", ArgumentSet.vec3("vec3"))
                        .operation(Double.class, (v, a) -> v.dot(a.getVec3("vec3")), "dot", ArgumentSet.vec3("vec3"))
                        .operation(Double.class, (v, a) -> v.get(a.getAxis("axis")), "getForAxis", ArgumentSet.axis("axis"))
        );
    }

    public static void registerVS() {
        OperationRegistry.forType(LoadedShip.class, opBuilder ->
            opBuilder

            .noArg(Long.class, LoadedShip::getId, "id")
            .noArg(String.class, LoadedShip::getSlug, "slug")
            .noArg(AABBic.class, LoadedShip::getShipAABB, "shipAABB")
            .noArg(AABBdc.class, LoadedShip::getWorldAABB, "worldAABB")
            .noArg(BodyKinematics.class, LoadedShip::getKinematics, "kinematics")
            .noArg(String.class, LoadedShip::getChunkClaimDimension, "chunkClaimDimension")
            .noArg(ChunkClaim.class, LoadedShip::getChunkClaim, "chunkClaim")
        );

        OperationRegistry.forType(BodyKinematics.class, opBuilder ->
            opBuilder

            .noArg(Vector3dc.class, BodyKinematics::getVelocity, "velocity")
            .noArg(Vector3dc.class, BodyKinematics::getAngularVelocity, "angularVelocity")
            .noArg(Quaterniondc.class, BodyKinematics::getRotation, "rotation")
            .noArg(Vector3dc.class, BodyKinematics::getPosition, "position")
            .noArg(Vector3dc.class, BodyKinematics::getScaling, "scaling")
            .noArg(Matrix4dc.class, BodyKinematics::getToModel, "worldToShip")
            .noArg(Matrix4dc.class, BodyKinematics::getToWorld, "shipToWorld")
        );

        OperationRegistry.forType(ChunkClaim.class, opBuilder ->
            opBuilder

            .noArg(Integer.class, ChunkClaim::getXIndex, "xIndex")
            .noArg(Integer.class, ChunkClaim::getXStart, "xStart")
            .noArg(Integer.class, ChunkClaim::getXMiddle, "xMiddle")
            .noArg(Integer.class, ChunkClaim::getXEnd, "xEnd")
            .noArg(Integer.class, ChunkClaim::getZIndex, "zIndex")
            .noArg(Integer.class, ChunkClaim::getZStart, "zStart")
            .noArg(Integer.class, ChunkClaim::getZMiddle, "zMiddle")
            .noArg(Integer.class, ChunkClaim::getZEnd, "zEnd")
            .noArg(Integer.class, ChunkClaim::getSize, "size")
        );
    }
}
