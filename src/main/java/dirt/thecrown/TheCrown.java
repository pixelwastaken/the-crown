//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown;


import dirt.thecrown.dataattachment.ModAttachments;
import dirt.thecrown.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheCrown implements ModInitializer {
	public static final String MOD_ID = "the-crown";
	public static final Logger LOGGER = LoggerFactory.getLogger("the-crown");
	public static boolean hasLuckPerms;

	public TheCrown() {
	}

	public void onInitialize() {
		ModItems.initialize();
		ModAttachments.initalize();
		LOGGER.info("Hello Fabric world, from the crown :)");
		FabricLoader fabric = FabricLoader.getInstance();
		hasLuckPerms = fabric.isModLoaded("luckperms");
		if (!hasLuckPerms && fabric.getEnvironmentType().equals(EnvType.SERVER)) {
			LOGGER.warn("LuckPerms not installed, The Crown server-sided suffixes will be disabled.");
		}

	}
}
