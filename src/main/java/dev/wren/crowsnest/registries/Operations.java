package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.Map;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class Operations {

    public static void register() {
        LOGGER.info("Registering operations...");
        OperationRegistry.registerAll(LoadedShip.class, Map.ofEntries(
                OperationDefinition.noArg(LoadedShip.class, Long.class, LoadedShip::getId, "id").asEntry(),
                OperationDefinition.noArg(LoadedShip.class, String.class, LoadedShip::getSlug, "slug").asEntry(),
                OperationDefinition.noArg(LoadedShip.class, AABBic.class, LoadedShip::getShipAABB, "shipAABB").asEntry(),
                OperationDefinition.noArg(LoadedShip.class, AABBdc.class, LoadedShip::getWorldAABB, "worldAABB").asEntry(), // todo add other stuff once this works
                OperationDefinition.noArg(LoadedShip.class, Vector3dc.class, LoadedShip::getVelocity, "velocity").asEntry()
        ));

        OperationRegistry.registerAll(AABB.class, Map.ofEntries(
                OperationDefinition.noArg(AABB.class, Double.class, AABB::getXsize, "xSize").asEntry()
        ));

        OperationRegistry.registerAll(Vec3.class, Map.ofEntries( // todo other noarg stuff
                OperationDefinition.noArg(Vec3.class, Double.class, Vec3::x, "x").asEntry(),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::add, "add").asEntry(),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::subtract, "subtract").asEntry(),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::multiply, "multiply").asEntry(),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::cross, "cross").asEntry(),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::vectorTo, "vectorTo").asEntry(),
                OperationDefinition.modifierOp(Vec3.class, Double.class, Vec3::scale, "scale").asEntry(),
                OperationDefinition.transformOp(Vec3.class, Double.class, Vec3::distanceTo, "distanceTo").asEntry(),
                OperationDefinition.transformOp(Vec3.class, Double.class, Vec3::distanceToSqr, "distanceToSqr").asEntry(),
                OperationDefinition.transformOp(Vec3.class, Double.class, Vec3::dot, "dot").asEntry()
        ));
    }
}
