package dirt.thecrown.item;

import dirt.thecrown.TheCrown;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class ExcaliburFireball extends LargeFireball {

    public ExcaliburFireball(Level level, Player player, Vec3 direction, int explosionPower) {
        super(level, player, direction, explosionPower);
    }

    @Override
    protected void onHitEntity(final @NonNull EntityHitResult hitResult) {
        Entity victim = hitResult.getEntity();

        //if it hits a wind charge, return
        if (victim instanceof WindCharge) {
            TheCrown.LOGGER.info("Excalibur fireball hit a wind charge, returning without damaging or exploding it.");
            return;
        }


        if (this.level() instanceof ServerLevel serverLevel) {
            Entity owner = this.getOwner();
            DamageSource damageSource = this.damageSources().fireball(this, owner);
            victim.hurtServer(serverLevel, damageSource, 6.0F);
            EnchantmentHelper.doPostAttackEffects(serverLevel, victim, damageSource);
        }
    }

    //prevent the fireball from exploding when it hits a wind charge
    @Override
    protected void onHit(final @NonNull HitResult hitResult) {
        Entity victim = hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) hitResult).getEntity() : null;

        //if it hits a wind charge, return
        if (victim instanceof WindCharge) {
            TheCrown.LOGGER.info("Excalibur fireball hit a wind charge (OVERRIDE), returning without exploding.");
            return;
        }

        super.onHit(hitResult);
    }


}
