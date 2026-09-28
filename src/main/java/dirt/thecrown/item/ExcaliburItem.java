package dirt.thecrown.item;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExcaliburItem extends Item {
    public ExcaliburItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
        // Add custom logic here
        if (!level.isClientSide()) {
            ServerLevel server = (ServerLevel) level;

            // Where the sword roughly is (server only knows coarse transforms)
            Vec3 basePos = player.getEyePosition()
                    .add(player.getLookAngle().scale(0.6)); // forward from face

            // Spawn a circular ring of flame particles
            int points = 20;
            float radius = 0.4f;

            for (int i = 0; i < points; i++) {
                float angle = (float) (i * (Math.PI * 2 / points));

                double ox = Math.cos(angle) * radius;
                double oz = Math.sin(angle) * radius;

                double x = basePos.x + ox;
                double y = basePos.y - 0.4;
                double z = basePos.z + oz;

                server.sendParticles(
                        ParticleTypes.FLAME,
                        x, y, z,
                        1,   // count
                        0, 0, 0,
                        0.0
                );
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
