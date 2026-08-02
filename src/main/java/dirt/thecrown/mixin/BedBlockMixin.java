//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.mixin;

import dirt.thecrown.saveddata.SavedBedBombData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BedBlock.class})
public class BedBlockMixin {
    public BedBlockMixin() {
    }

    @Inject(
            method = {"useWithoutItem"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onUse(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide() && level.getServer() != null) {
            SavedBedBombData savedBedBombData = SavedBedBombData.getSavedBedBombData(level.getServer());
            if (level.dimension().equals(Level.END) && !savedBedBombData.getBedBombsFlag()) {
                player.sendOverlayMessage(Component.literal("Bed bombs are disabled."));
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
                cir.cancel();
            }
        }

    }
}
