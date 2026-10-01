//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.mixin;

import dirt.thecrown.TheCrown;
import dirt.thecrown.dataattachment.ModAttachments;
import dirt.thecrown.item.ExcaliburFireball;
import dirt.thecrown.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public abstract class PlayerMixin {
    public PlayerMixin() {
    }

    @Inject(
            method = {"dropEquipment(Lnet/minecraft/server/level/ServerLevel;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void crownDropEquipment(CallbackInfo ci) {
        Player self = (Player)(Object)this;
        if (!self.level().isClientSide()) {
            TheCrown.LOGGER.info("player attempts to drop all items...");
            if (ModItems.isWearingCrown(self)) {
                TheCrown.LOGGER.info("Cancelled! that player is wearing the crown");
                ci.cancel();
                //Else if the player was killed by someone wearing the crown, cancel the drop
                // OR if the player was killed by an Excalibur fireball, cancel the drop
            } else if ((self.getLastDamageSource() != null && self.getLastDamageSource().getDirectEntity() instanceof ExcaliburFireball)
                    || (self.getKillCredit() != null && ModItems.isWearingCrown(self.getKillCredit()))) {
                TheCrown.LOGGER.info("Cancelled! that player got killed by someone wearing the crown");
                self.setAttached(ModAttachments.MUST_RESTORE_ITEMS_ATTACHMENT, true);
                ci.cancel();
            } else if (self.getAttachedOrCreate(ModAttachments.MUST_RESTORE_ITEMS_ATTACHMENT)) {
                ci.cancel();
            }

        }
    }

    @Inject(
            method = "hurtServer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventCrownDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        Player self = (Player)(Object)this;
        if (ModItems.isWearingCrown(self) && !self.level().isClientSide() &&
                source.getDirectEntity() instanceof ExcaliburFireball) {
            TheCrown.LOGGER.info("Cancelled! that player is wearing the crown and cannot be damaged");
            cir.setReturnValue(false);
        }
    }


}
