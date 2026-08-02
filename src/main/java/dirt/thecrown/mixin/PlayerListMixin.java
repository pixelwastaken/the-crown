//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.mixin;

import dirt.thecrown.TheCrown;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerList.class})
public abstract class PlayerListMixin {
    public PlayerListMixin() {
    }

    @Inject(
            method = {"respawn"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V"
            )}
    )
    private void injectRespawn(CallbackInfoReturnable<ServerPlayer> cir) {
        TheCrown.LOGGER.info("respawn ran");
    }
}
