//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.event;

import dirt.thecrown.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ActionbarManager {
    private static Component message = null;
    private static int remaining = 0;

    public ActionbarManager() {
    }

    public static void queue(MinecraftServer server, Component msg, int ticks) {
        message = msg;
        remaining = ticks;
    }

    public static void tick(MinecraftServer server) {
        if (remaining > 0 && message != null) {
            for(ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.connection.send(new ClientboundSetActionBarTextPacket(message));
                ModItems.giveCrownHelmetEffects(player);
            }

            --remaining;
        } else {
            for(ServerPlayer player : server.getPlayerList().getPlayers()) {
                ModItems.giveCrownHelmetEffects(player);
            }
        }

    }
}
