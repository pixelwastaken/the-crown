package dirt.thecrown.item;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExcaliburItem extends Item {
    public ExcaliburItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(
                    (source, l, itemStack) -> new WindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z()),
                    serverLevel,
                    stack,
                    player,
                    0.0F,
                    1.5F,
                    0.0F
            );
            Projectile.spawnProjectileFromRotation(
                    (source, l, itemStack) -> new ExcaliburFireball(level, player, player.getLookAngle(), 10),
                    serverLevel,
                    stack,
                    player,
                    -5.0F, //negative brings it up, positive brings it down
                    1.5F,
                    0.0F
            );
        }

        return InteractionResult.SUCCESS;
    }
}
