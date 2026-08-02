//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.saveddata;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class SavedRecentKingData extends SavedData {
    private NameAndId recentKing;
    private static final Codec<SavedRecentKingData> CODEC;
    private static final SavedDataType<SavedRecentKingData> TYPE;

    public SavedRecentKingData() {
    }

    public SavedRecentKingData(NameAndId recentKing) {
        this.recentKing = recentKing;
    }

    public NameAndId getRecentKing() {
        return this.recentKing;
    }

    public static SavedRecentKingData getSavedRecentKingData(MinecraftServer server) {
        ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);
        return level == null ? new SavedRecentKingData() : (SavedRecentKingData)level.getDataStorage().computeIfAbsent(TYPE);
    }

    public void setRecentKing(NameAndId recentKing) {
        this.recentKing = recentKing;
        this.setDirty();
    }

    static {
        CODEC = NameAndId.CODEC.xmap(SavedRecentKingData::new, SavedRecentKingData::getRecentKing);
        TYPE = new SavedDataType<>(Identifier.fromNamespaceAndPath("the-crown", "recent_king"), SavedRecentKingData::new, CODEC, (DataFixTypes)null);
    }
}
