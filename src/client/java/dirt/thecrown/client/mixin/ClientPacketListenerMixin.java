//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.client.mixin;

import dirt.thecrown.item.ModItems;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPacketListener.class})
public class ClientPacketListenerMixin {
    public ClientPacketListenerMixin() {
    }

    @Inject(
            method = {"Lnet/minecraft/client/multiplayer/ClientPacketListener;findTotem(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/item/ItemStack;"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void findCrown(Player player, CallbackInfoReturnable<ItemStack> cir) {
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.CROWN)) {
            cir.setReturnValue(player.getItemBySlot(EquipmentSlot.HEAD));
        }

    }
}
