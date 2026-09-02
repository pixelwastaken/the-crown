package dirt.thecrown.mixin;

import dirt.thecrown.item.ModItems;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.slot.SlotCollection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(ShelfBlock.class)
public class ShelfBlockMixin {

    @Inject(
            method="swapSingleItem",
            at=@At("HEAD"),
            cancellable = true
    )
    private static void preventExcaliburPlacement(ItemStack itemStack, Player player, ShelfBlockEntity shelfBlockEntity, int hitSlot, Inventory inventory, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(ModItems.EXCALIBUR))
            cir.setReturnValue(false);
    }

    @Inject(
            method="swapHotbar",
            at=@At("HEAD"),
            cancellable = true
    )
    private void preventHotbarSwap(Level level, BlockPos pos, Inventory inventory, CallbackInfoReturnable<Boolean> cir) {
        //define the hotbar slots as a SlotCollection
        SlotCollection hotbar = inventory.getSlotsFromRange(IntList.of(0,1,2,3,4,5,6,7,8));

        //filter the collection using a predicate to check if an ItemStack is Excalibur,
        // then return that SlotCollection as a Stream of ItemStacks
        Stream<ItemStack> hotbarItems = hotbar.filter(stack -> stack.is(ModItems.EXCALIBUR)).itemCopies();

        //if the stream is not empty, then Excalibur is in the hotbar and we should prevent the swap
        if (hotbarItems.findAny().isPresent())
            cir.setReturnValue(false);
    }
}
