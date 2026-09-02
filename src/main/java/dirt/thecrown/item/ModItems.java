//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.item;

import dirt.thecrown.TheCrown;
import dirt.thecrown.item.armor.CrackedCrownArmorMaterial;
import dirt.thecrown.item.armor.CrownArmorMaterial;

import java.util.HashMap;
import java.util.Map;
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
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.ArmorType;

public class ModItems {
    public static final Item CROWN_CHUNK = register("crown_chunk", Item::new, new Item.Properties());

    public static final AttributeModifier CROWN_BLOCKINT_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("the-crown", "crown_block_int_range"), 2.0F, Operation.ADD_VALUE);
    public static final AttributeModifier CROWN_HEART_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath("the-crown", "crown_hearts"), 20.0F, Operation.ADD_VALUE);
    public static final Item CROWN = register("crown", Item::new, (new Item.Properties()).humanoidArmor(CrownArmorMaterial.INSTANCE, ArmorType.HELMET).rarity(Rarity.EPIC).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING).fireResistant());
    public static final Item CRACKED_CROWN = register("cracked_crown", Item::new, (new Item.Properties()).humanoidArmor(CrackedCrownArmorMaterial.INSTANCE, ArmorType.HELMET).rarity(Rarity.EPIC).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).fireResistant());

    public static final Item EXCALIBUR = register(
            "excalibur",
            Item::new,
            (new Item.Properties())
                    .rarity(Rarity.EPIC)
                    .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                    .fireResistant()
                    .sword(ToolMaterial.NETHERITE, 3, -2.4F)
    );

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

    public static void giveExcaliburEffects(ServerPlayer player) {
        if (player.getMainHandItem().is(EXCALIBUR)) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 20, 1, false, false, true));
        }
    }

    public static void excaliburDefaultItemComponents(DataComponentMap.Builder builder, HolderLookup.Provider provider, Item item) {
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        HolderLookup.RegistryLookup<Enchantment> enchLookup = provider.lookupOrThrow(Enchantments.BINDING_CURSE.registryKey());

        Map<ResourceKey<Enchantment>, Integer> enchantmentMap = new HashMap<>(Map.of(
                Enchantments.VANISHING_CURSE, 1,
                Enchantments.SHARPNESS, 5,
                Enchantments.BANE_OF_ARTHROPODS, 5,
                Enchantments.SMITE, 5,
                Enchantments.LOOTING, 5,
                Enchantments.FIRE_ASPECT, 3,
                Enchantments.SWEEPING_EDGE, 3,
                Enchantments.BREACH, 1
        ));


        enchantmentMap.forEach((enchantment, level) -> {
            Holder<Enchantment> enchHolder = enchLookup.getOrThrow(enchantment);
            enchantments.set(enchHolder, level);
        });

        builder.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable()).build();

        TheCrown.LOGGER.info("Loaded excalibur item enchantments.");
    }

    public static void crownDefaultItemComponents(DataComponentMap.Builder builder, HolderLookup.Provider provider, Item item) {
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        HolderLookup.RegistryLookup<Enchantment> enchLookup = provider.lookupOrThrow(Enchantments.BINDING_CURSE.registryKey());

        Map<ResourceKey<Enchantment>, Integer> enchantmentMap = new HashMap<>(Map.of(
                Enchantments.BINDING_CURSE, 1,
                Enchantments.VANISHING_CURSE, 1,
                Enchantments.PROTECTION, 4,
                Enchantments.AQUA_AFFINITY, 1,
                Enchantments.RESPIRATION, 3
        ));

        enchantmentMap.forEach((enchantment, level) -> {
            Holder<Enchantment> enchHolder = enchLookup.getOrThrow(enchantment);
            enchantments.set(enchHolder, level);
        });

        builder.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable()).build();
        ItemAttributeModifiers attMods = builder.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        attMods = attMods.withModifierAdded(Attributes.MAX_HEALTH, CROWN_HEART_MODIFIER, EquipmentSlotGroup.HEAD).withModifierAdded(Attributes.BLOCK_INTERACTION_RANGE, CROWN_BLOCKINT_MODIFIER, EquipmentSlotGroup.HEAD);
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, attMods);

        TheCrown.LOGGER.info("Loaded crown item enchantments.");
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((tab) -> tab.accept(CROWN));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((tab) -> tab.accept(CRACKED_CROWN));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((tab) -> tab.accept(EXCALIBUR));
    }

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("the-crown", name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}
