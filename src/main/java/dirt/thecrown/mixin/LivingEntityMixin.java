//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dirt.thecrown.TheCrown;
import dirt.thecrown.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Debug(
        export = true
)
@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends Entity implements Attackable, WaypointTransmitter {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = {"Lnet/minecraft/world/entity/LivingEntity;onAttributeUpdated(Lnet/minecraft/core/Holder;)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/waypoints/ServerWaypointManager;untrackWaypoint(Lnet/minecraft/world/waypoints/WaypointTransmitter;)V"
            )},
            cancellable = true
    )
    private void waypointUpdateCancel(CallbackInfo callbackInfo) {
        if ((Object)this instanceof ServerPlayer player) {
            if (ModItems.isWearingCrown(player)) {
                callbackInfo.cancel();
            }

        }
    }

    @Inject(
            method = {"Lnet/minecraft/world/entity/LivingEntity;isTransmittingWaypoint()Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void forceTransmitWaypoint(CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof ServerPlayer player) {
            if (ModItems.isWearingCrown(player)) {
                cir.setReturnValue(true);
            }

        }
    }

    @Inject(
            method = {"checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void preventTotems(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (ModItems.isWearingCrown(self) && self.getItemBySlot(EquipmentSlot.HEAD).get(DataComponents.DEATH_PROTECTION) == null) {
            TheCrown.LOGGER.info("person is wearing CRACKED crown.. totem prevented!");
            cir.setReturnValue(false);
        }

    }

    @Inject(
            method = {"dropAllDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment(Lnet/minecraft/server/level/ServerLevel;)V",
                    shift = Shift.AFTER
            )},
            cancellable = true
    )
    private void preventXpDrop(ServerLevel level, DamageSource source, CallbackInfo ci) {
        Entity self = level.getEntity(this.getId());
        if (self instanceof ServerPlayer plr) {
            if (ModItems.isWearingCrown(plr) || plr.getKillCredit() != null && ModItems.isWearingCrown(plr.getKillCredit())) {
                ci.cancel();
            }
        }

    }


    @Inject(
            method = {"checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/criterion/UsedTotemTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;)V",
                    shift = Shift.AFTER
            )}
    )
    private void logTotemUse(DamageSource killingDamage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof ServerPlayer plr) {
            Entity offender = killingDamage.getEntity();

            String offenderName = offender != null ? offender.getPlainTextName() : "Natural causes";
            String offenderType = offender != null ? offender.getType().toShortString() : "NATURAL";

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();

            // Format the date and time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");

            TheCrown.LOGGER.info("TOTEM POP: {}'s totem was popped by {}. ENTITY TYPE: {}. KILL CREDIT: {}. TIME: {}", plr.getPlainTextName(), offenderName, offenderType, self.getKillCredit() != null ? self.getKillCredit().getPlainTextName() : "None", now.format(formatter));
        }
    }

    @ModifyExpressionValue(
            method = {"checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
            )}
    )
    private ItemStack getCrownItemFromEntity(ItemStack original, DamageSource killingDamage) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof ServerPlayer plr) {
            if (ModItems.isWearingCrown(plr)) {
                TheCrown.LOGGER.info("{} cracked their crown aka their crowntotem popped!!", plr.getPlainTextName());
                ItemStack safeCopy = plr.getItemBySlot(EquipmentSlot.HEAD).copy();
                plr.setItemSlot(EquipmentSlot.HEAD, ModItems.CRACKED_CROWN.getDefaultInstance());
                return safeCopy;
            }
        }

        return original;
    }


}
