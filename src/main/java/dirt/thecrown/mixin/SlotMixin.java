package dirt.thecrown.mixin;

import dirt.thecrown.item.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow
    @Final
    public Container container;

    // this will only run for containers that aren't shulker boxes, since shulker boxes use their own slot implementation
    @Inject(
            method="mayPlace",
            at=@At("HEAD"),
            cancellable = true
    )
    public void preventExcaliburPlacement(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(ModItems.EXCALIBUR) && !(container instanceof Inventory)) {
            cir.setReturnValue(false);
        }
    }


}
