//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import dirt.thecrown.TheCrown;
import dirt.thecrown.dataattachment.ModAttachments;
import dirt.thecrown.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(
        export = true
)
@Mixin({ServerPlayer.class})
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Unique
    private void privateDestroyVanishingCursedItems(ServerPlayer victim) {
        for(int i = 0; i < victim.getInventory().getContainerSize(); ++i) {
            ItemStack itemStack = victim.getInventory().getItem(i);
            if (!itemStack.isEmpty() && EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                victim.getInventory().removeItemNoUpdate(i);
            }
        }

    }

    @Inject(
            method = {"Lnet/minecraft/server/level/ServerPlayer;updatePlayerAttributes()V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;isCrouching()Z"
            )},
            cancellable = true
    )
    private void cancelIsCrouchingCheck(CallbackInfo callbackInfo) {
        if (ModItems.isWearingCrown(this)) {
            callbackInfo.cancel();
        }

    }

    @ModifyExpressionValue(
            method = {"restoreFrom"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"
            )}
    )
    private boolean orHasCrown(boolean original, ServerPlayer oldPlayer) {
        LivingEntity killCredit = oldPlayer.getKillCredit();
        boolean hasCrown = ModItems.isWearingCrown(oldPlayer) || (killCredit != null && ModItems.isWearingCrown(killCredit)) || oldPlayer.getAttachedOrSet(ModAttachments.MUST_RESTORE_ITEMS_ATTACHMENT, false);
        if (hasCrown) {
            TheCrown.LOGGER.info("Restoring player: they have the crown or were killed by someone the crown");
            this.privateDestroyVanishingCursedItems(oldPlayer);
        } else {
            TheCrown.LOGGER.info("Player will not be restored. Kill Credit: {},\n Old Player: {},\n New Player: {}", killCredit, oldPlayer, this);
        }

        return original || hasCrown;
    }
}
