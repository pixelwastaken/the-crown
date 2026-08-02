//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.saveddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;

public class SavedCrownPedestalData extends SavedData {
    private BlockPos crownPos;
    private Vec3 scale;
    private static final Codec<SavedCrownPedestalData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(BlockPos.CODEC.fieldOf("crownPos").forGetter(SavedCrownPedestalData::getCrownPos), Vec3.CODEC.fieldOf("scale").forGetter(SavedCrownPedestalData::getScale)).apply(instance, SavedCrownPedestalData::new));
    private static final SavedDataType<SavedCrownPedestalData> TYPE;

    public SavedCrownPedestalData() {
    }

    public SavedCrownPedestalData(BlockPos crownPos, Vec3 scale) {
        this.crownPos = crownPos;
        this.scale = scale;
    }

    public BlockPos getCrownPos() {
        return this.crownPos;
    }

    public Vec3 getScale() {
        return this.scale;
    }

    public static SavedCrownPedestalData getSavedCrownPedestalData(MinecraftServer server) {
        ServerLevel level = server.getLevel(ServerLevel.OVERWORLD);
        return level == null ? new SavedCrownPedestalData() : (SavedCrownPedestalData)level.getDataStorage().computeIfAbsent(TYPE);
    }

    public void setCrownArgs(BlockPos pos, Vec3 scale) {
        this.crownPos = pos;
        this.scale = scale;
        this.setDirty();
    }

    static {
        TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("the-crown", "crown_pedestal"), SavedCrownPedestalData::new, CODEC, (DataFixTypes)null);
    }
}
