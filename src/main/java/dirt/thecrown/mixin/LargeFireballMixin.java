package dirt.thecrown.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dirt.thecrown.item.ExcaliburFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(LargeFireball.class)
public class LargeFireballMixin {
    //change the expression value of .gamerules().getBoolean(GameRules.RULE_MOBGRIEFING) to true, so that the fireball cannot create fire
    @ModifyExpressionValue(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;"
            )
    )
    private Object modifyMobGriefing(Object original, HitResult hitResult) {

        //if the fireball is an instance of ExcaliburFireball, return false, so that the fireball cannot create fire
        if ((Object) this instanceof ExcaliburFireball) {
            return false;
        }
        return original;
    }

    //change the argument of Level.explode to Level.ExplosionInteraction.NONE, so that the fireball cannot explode blocks
    @ModifyArg(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
            ),
            index = 6
    )
    private Level.ExplosionInteraction modifyExplosionInteraction(Level.ExplosionInteraction explosionInteraction) {
        //if the fireball is an instance of ExcaliburFireball, return false, so that the fireball cannot explode
        if ((Object) this instanceof ExcaliburFireball) {
            return Level.ExplosionInteraction.NONE;
        }
        return explosionInteraction;
    }
}
