package dirt.thecrown.mixin;

import dirt.thecrown.item.ModItems;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {

    @Inject(
            method="mayPlace",
            at=@At("HEAD"),
            cancellable = true
    )
    public void preventExcaliburPlacement(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(ModItems.EXCALIBUR)) {
            cir.setReturnValue(false);
        }
    }
}
