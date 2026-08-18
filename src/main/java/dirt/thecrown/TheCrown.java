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

	//TODO add actionbar showing timer for combat log
	//TODO remove the ability to warp during combat log timer
	//TODO add a forceremovecrown command (needs to work for people who are on and offline)
	// 1. make the loseCrown method be called when someone joins the game, if it is added to a callback list
	//TODO make the combat log system transfer crown when someone leaves (detection is already in place, we just need implementation)

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
