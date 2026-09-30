package dirt.thecrown.mixin;

import dirt.thecrown.TheCrown;
import dirt.thecrown.item.ExcaliburFireball;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractWindCharge.class)
public class AbstractWindChargeMixin {


    //stop collision & hits w/ excalibur fireball
    //this is to prevent the fireball from hitting the wind charge and causing it to explode
    @Inject(
            method = "canHitEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    private void canHitEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof ExcaliburFireball) {
            TheCrown.LOGGER.info("Wind charge hit an Excalibur fireball, returning without damaging it.");
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "canCollideWith",
            at = @At("HEAD"),
            cancellable = true
    )
    private void canCollideWith(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        TheCrown.LOGGER.info("Wind charge collided with an Excalibur fireball, returning without damaging it.");
        if (entity instanceof ExcaliburFireball) {
            cir.setReturnValue(false);
        }
    }
}
