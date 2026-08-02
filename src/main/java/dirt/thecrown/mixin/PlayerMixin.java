//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.mixin;

import dirt.thecrown.TheCrown;
import dirt.thecrown.dataattachment.ModAttachments;
import dirt.thecrown.item.ModItems;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Player.class})
public abstract class PlayerMixin {
    public PlayerMixin() {
    }

    @Inject(
            method = {"Lnet/minecraft/world/entity/player/Player;dropEquipment(Lnet/minecraft/server/level/ServerLevel;)V"},
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
            } else if (self.getKillCredit() != null && ModItems.isWearingCrown(self.getKillCredit())) {
                TheCrown.LOGGER.info("Cancelled! that player got killed by someone wearing the crown");
                self.setAttached(ModAttachments.MUST_RESTORE_ITEMS_ATTACHMENT, true);
                ci.cancel();
            }

        }
    }
}
