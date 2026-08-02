//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.mixin;

import dirt.thecrown.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.waypoints.Waypoint;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({WaypointTransmitter.class})
public interface WaypointTransmitterMixin extends Waypoint {
    @Inject(
            method = {"Lnet/minecraft/world/waypoints/WaypointTransmitter;doesSourceIgnoreReceiver(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/server/level/ServerPlayer;)Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void crownBypassesIgnore(LivingEntity source, ServerPlayer receiver, CallbackInfoReturnable<Boolean> cir) {
        if (source instanceof ServerPlayer player) {
            if (ModItems.isWearingCrown(player)) {
                cir.setReturnValue(false);
            }
        }

    }
}
