//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.item;

import dirt.thecrown.TheCrown;
import dirt.thecrown.item.armor.CrackedCrownArmorMaterial;
import dirt.thecrown.item.armor.CrownArmorMaterial;
import java.util.function.Function;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.ArmorType;

public class ModItems {
    public static final Item CROWN_CHUNK = register("crown_chunk", Item::new, new Item.Properties());
    public static final AttributeModifier CROWN_BLOCKINT_MODIFIER;
    public static final AttributeModifier CROWN_HEART_MODIFIER;
    public static final Item CROWN;
    public static final Item CRACKED_CROWN;

    public ModItems() {
    }

    public static boolean isWearingCrown(LivingEntity player) {
        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        return headItem.is(CROWN) || headItem.is(CRACKED_CROWN);
    }

    public static void giveCrownHelmetEffects(ServerPlayer player) {
        if (isWearingCrown(player)) {
            player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 60, 4, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, false, false, true));
        }

    }

    public static void crownDefaultItemComponents(DataComponentMap.Builder builder, HolderLookup.Provider provider, Item item) {
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        HolderLookup.RegistryLookup<Enchantment> enchLookup = provider.lookupOrThrow(Enchantments.BINDING_CURSE.registryKey());
        Holder<Enchantment> curseOfBinding = enchLookup.getOrThrow(Enchantments.BINDING_CURSE);
        Holder<Enchantment> curseOfVanishing = enchLookup.getOrThrow(Enchantments.VANISHING_CURSE);
        Holder<Enchantment> prot = enchLookup.getOrThrow(Enchantments.PROTECTION);
        Holder<Enchantment> aquaaffinity = enchLookup.getOrThrow(Enchantments.AQUA_AFFINITY);
        Holder<Enchantment> respiration = enchLookup.getOrThrow(Enchantments.RESPIRATION);
        enchantments.set(curseOfBinding, 1);
        enchantments.set(curseOfVanishing, 1);
        enchantments.set(prot, 4);
        enchantments.set(aquaaffinity, 1);
        enchantments.set(respiration, 3);
        builder.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable()).build();
        ItemAttributeModifiers attMods = (ItemAttributeModifiers)builder.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        attMods = attMods.withModifierAdded(Attributes.MAX_HEALTH, CROWN_HEART_MODIFIER, EquipmentSlotGroup.HEAD).withModifierAdded(Attributes.BLOCK_INTERACTION_RANGE, CROWN_BLOCKINT_MODIFIER, EquipmentSlotGroup.HEAD);
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, attMods);
        TheCrown.LOGGER.info("Loaded crown item enchantments.");
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((CreativeModeTabEvents.ModifyOutput)(tab) -> tab.accept(CROWN));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((CreativeModeTabEvents.ModifyOutput)(tab) -> tab.accept(CRACKED_CROWN));
    }

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("the-crown", name));
        T item = (T)(itemFactory.apply(settings.setId(itemKey)));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    static {
        CROWN_BLOCKINT_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("the-crown", "crown_block_int_range"), (double)2.0F, Operation.ADD_VALUE);
        CROWN_HEART_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("the-crown", "crown_hearts"), (double)20.0F, Operation.ADD_VALUE);
        CROWN = register("crown", Item::new, (new Item.Properties()).humanoidArmor(CrownArmorMaterial.INSTANCE, ArmorType.HELMET).rarity(Rarity.EPIC).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING).fireResistant());
        CRACKED_CROWN = register("cracked_crown", Item::new, (new Item.Properties()).humanoidArmor(CrackedCrownArmorMaterial.INSTANCE, ArmorType.HELMET).rarity(Rarity.EPIC).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).fireResistant());
    }
}
