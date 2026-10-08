package dirt.thecrown.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @ModifyExpressionValue(
            method = "sendPairingData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;isRemoved()Z"
            )
    )
    //basically suppress the isRemoved check so that the warning doesn't appear in the console
    private boolean modifyIsRemoved(boolean original) {
        //return false to suppress the warning, return true to allow it to appear in the console
        return false;
    }
}
