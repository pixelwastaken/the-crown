//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.event;

import dirt.thecrown.TheCrown;
import dirt.thecrown.dataattachment.ModAttachments;
import dirt.thecrown.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

//blossom.warp.warps
//blossom
//blossom.tpa

import java.time.Instant;

public class ActionbarManager {
    private static Component message = null;
    private static int remaining = 0;

    public ActionbarManager() {
    }

    public static void queue(Component msg, int ticks) {
        message = msg;
        remaining = ticks;
    }

    public static void clearActionbars(MinecraftServer server) {
        for(ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(new ClientboundSetActionBarTextPacket(Component.empty()));
        }
    }
    public static void clearPlayerActionbar(ServerPlayer player) {
        player.connection.send(new ClientboundSetActionBarTextPacket(Component.empty()));
    }

    public static void tick(MinecraftServer server) {
        if (remaining > 0 && message != null) {
            for(ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.connection.send(new ClientboundSetActionBarTextPacket(message));

                ModItems.giveCrownHelmetEffects(player);
            }

            remaining--;
        } else {
            //tick if there is no current message
            for(ServerPlayer player : server.getPlayerList().getPlayers()) {
                //if they are in combat
                long combatTimestamp = player.getAttachedOrSet(ModAttachments.COMBAT_LOG_ATTACHMENT, 0L);
                long combatTimeDelta = Instant.now().getEpochSecond() - combatTimestamp;
                boolean inCombat = combatTimeDelta < 30;
                boolean wasInCombat = player.getAttachedOrSet(ModAttachments.WAS_IN_COMBAT_ATTACHMENT, false);

                if (inCombat) {
                    //do 30 - combat time to make it countdown, instead of up
                    message = Component.literal("You are in combat! Time remaining: %s".formatted(30 - combatTimeDelta)).withStyle(ChatFormatting.RED);
                } else {
                    message = Component.empty();
                }

                //edge case, to reset perms
                //if we were in combat before, but not anymore
                // this would only happen for 1 tick
                if (wasInCombat && !inCombat && TheCrown.hasLuckPerms) {
                    LuckPerms lp = LuckPermsProvider.get();
                    User currentUser = lp.getPlayerAdapter(ServerPlayer.class).getUser(player);

                    //give the user their tpa perms back
                    currentUser.data().add(PermissionNode.builder().permission("blossom").value(true).build());
                    currentUser.data().add(PermissionNode.builder().permission("blossom.warps.warp").value(true).build());
                    currentUser.data().add(PermissionNode.builder().permission("blossom.tpa").value(true).build());

                    lp.getUserManager().saveUser(currentUser);
                }

                player.connection.send(new ClientboundSetActionBarTextPacket(message));

                ModItems.giveCrownHelmetEffects(player);

                player.setAttached(ModAttachments.WAS_IN_COMBAT_ATTACHMENT, inCombat);
            }
        }

    }
}
