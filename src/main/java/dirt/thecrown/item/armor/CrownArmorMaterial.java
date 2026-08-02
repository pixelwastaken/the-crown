//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.item.armor;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class CrownArmorMaterial {
    public static int BASE_DURABILLITY;
    public static final ResourceKey<EquipmentAsset> CROWN_ARMOR_KEY;
    public static final ArmorMaterial INSTANCE;

    public CrownArmorMaterial() {
    }

    static {
        BASE_DURABILLITY = ArmorMaterials.NETHERITE.durability();
        CROWN_ARMOR_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath("the-crown", "crown"));
        INSTANCE = new ArmorMaterial(BASE_DURABILLITY, ArmorMaterials.NETHERITE.defense(), ArmorMaterials.NETHERITE.enchantmentValue(), ArmorMaterials.NETHERITE.equipSound(), ArmorMaterials.NETHERITE.toughness(), ArmorMaterials.NETHERITE.knockbackResistance(), ArmorMaterials.NETHERITE.repairIngredient(), CROWN_ARMOR_KEY);
    }
}
