package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.OperationDefinition;
import dev.wren.crowsnest.internal.reg.OperationRegistry;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class Operations {

    public static void register() {
        LOGGER.info("Registering operations...");
        OperationRegistry.registerAll(Vec3.class, Map.ofEntries(
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::add).asEntry("add"),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::subtract).asEntry("subtract"),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::multiply).asEntry("multiply"),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::cross).asEntry("cross"),
                OperationDefinition.sameTypeOp(Vec3.class, Vec3::vectorTo).asEntry("vectorTo"),
                OperationDefinition.modifierOp(Vec3.class, Double.class, Vec3::scale).asEntry("scale"),
                OperationDefinition.transformOp(Vec3.class, Double.class, Vec3::distanceTo).asEntry("distanceTo"),
                OperationDefinition.transformOp(Vec3.class, Double.class, Vec3::distanceToSqr).asEntry("distanceToSqr"),
                OperationDefinition.transformOp(Vec3.class, Double.class, Vec3::dot).asEntry("dot")
        ));
    }

}
