package dirt.thecrown.mixin;

import dirt.thecrown.item.ModItems;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {

    @Inject(
            method= "setItem(Lnet/minecraft/world/item/ItemStack;Z)V",
            at=@At("HEAD"),
            cancellable = true
    )
    public void preventSetExcalibur(ItemStack itemStack, boolean updateNeighbours, CallbackInfo ci) {
        if (itemStack.is(ModItems.EXCALIBUR))
            ci.cancel();
    }
}
