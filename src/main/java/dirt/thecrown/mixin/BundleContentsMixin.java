package dirt.thecrown.mixin;

import dirt.thecrown.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleContents.class)
public class BundleContentsMixin {

    @Inject(
            method="canItemBeInBundle",
            at=@At("HEAD"),
            cancellable = true
    )
    private static void preventExcaliburInBundle(final ItemStack itemToAdd, CallbackInfoReturnable<Boolean> cir) {
        if (itemToAdd.is(ModItems.EXCALIBUR)) {
            cir.setReturnValue(false);
        }
    }
}
