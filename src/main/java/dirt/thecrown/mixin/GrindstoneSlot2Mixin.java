package dirt.thecrown.mixin;

import dirt.thecrown.item.ModItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$2")
public class GrindstoneSlot2Mixin {
    @Inject(
            method={"mayPlace(Lnet/minecraft/world/item/ItemStack;)Z"},
            at=@At("HEAD"),
            cancellable = true
    )
    public void preventExcaliburPlacement(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(ModItems.EXCALIBUR)) {
            cir.setReturnValue(false);
        }
    }

}
