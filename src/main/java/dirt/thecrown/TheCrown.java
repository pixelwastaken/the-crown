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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheCrown implements ModInitializer {
	public static final String MOD_ID = "the-crown";
	public static final Logger LOGGER = LoggerFactory.getLogger("the-crown");
	public static boolean hasLuckPerms;

	//DONE add actionbar showing timer for combat log
	//DONE remove the ability to warp during combat log timer
	//DONE make tnt not destroy items
	//DONE add extra logging for totem popping
	//DONE (FIXED) when the person w/ the crown dies in the void, the crown doesn't return to the altar

	//DONE add a /forceremovecrown command (needs to work for people who are on and offline)
	// 1. make the loseCrown method be called when someone joins the game, if it is added to a callback list
	//DONE make the combat log system transfer crown when someone leaves (detection is already in place, we just need implementation)
	// after the player is punished from combat log system, make sure they are no longer in combat
	//DONE make it so that when someone combat logs, if they lose the crown to someone who isn't in the game anymore, then spawn the crown at the altar

	//TODO [OPTIONAL] limit warp number?

	//TODO make the crown broadcastGlobalMessages appear in dmcc

	//TODO make it so you cant put excalibur in shelves/item frames


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
