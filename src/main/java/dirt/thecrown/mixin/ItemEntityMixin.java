package dirt.thecrown.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(
            method = "hurtServer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void noExplosionDmg(final ServerLevel level, final DamageSource source, final float damage, CallbackInfoReturnable<Boolean> cir) {
        if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
            //returning false means to ignore the damage taken
            cir.setReturnValue(false);
        }
    }

    //prevent wind charge explosion from moving items
    @Inject(
            method = "ignoreExplosion",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ignoreWindBurstExplosion(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
        //only wind bursts can trigger blocks, so we can use that to check if it's a wind burst explosion
        if (explosion.canTriggerBlocks()) {
            //returning true means to ignore the explosion knockback
            cir.setReturnValue(true);
        }
    }
}
