//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.client;

import dirt.thecrown.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Interaction;

public class TheCrownClient implements ClientModInitializer {
	public TheCrownClient() {
	}

	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			if (stack.is(ModItems.CROWN_CHUNK)) {
				tooltip.add(Component.translatable("item.the-crown.crown_chunk.info").withStyle(ChatFormatting.GOLD));
				tooltip.add(Component.translatable("item.the-crown.crown_chunk.info2").withStyle(ChatFormatting.GOLD));
			}

		});
		DefaultItemComponentEvents.MODIFY.register((modifyContext) -> modifyContext.modify(ModItems.CROWN, ModItems::crownDefaultItemComponents));
		DefaultItemComponentEvents.MODIFY.register((modifyContext) -> modifyContext.modify(ModItems.CRACKED_CROWN, ModItems::crownDefaultItemComponents));
		DefaultItemComponentEvents.MODIFY.register((modifyContext) -> modifyContext.modify(ModItems.EXCALIBUR, ModItems::excaliburDefaultItemComponents));
		UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> (entity instanceof Interaction ? InteractionResult.SUCCESS : InteractionResult.PASS));
	}
}
