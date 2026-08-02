//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.saveddata;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class SavedBedBombData extends SavedData {
    private boolean doBedBombs;
    private static final Codec<SavedBedBombData> CODEC;
    private static final SavedDataType<SavedBedBombData> TYPE;

    public SavedBedBombData() {
    }

    public SavedBedBombData(boolean doBedBombs) {
        this.doBedBombs = doBedBombs;
    }

    public boolean getBedBombsFlag() {
        return this.doBedBombs;
    }

    public static SavedBedBombData getSavedBedBombData(MinecraftServer server) {
        ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);
        return level == null ? new SavedBedBombData() : (SavedBedBombData)level.getDataStorage().computeIfAbsent(TYPE);
    }

    public void setBedBombsFlag(boolean flag) {
        this.doBedBombs = flag;
        this.setDirty();
    }

    static {
        CODEC = Codec.BOOL.xmap(SavedBedBombData::new, SavedBedBombData::getBedBombsFlag);
        TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("the-crown", "do_bed_bombs"), SavedBedBombData::new, CODEC, (DataFixTypes)null);
    }
}
