//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dirt.thecrown.item.armor;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class CrackedCrownArmorMaterial {
    public static final ResourceKey<EquipmentAsset> CRACKED_CROWN_ARMOR_KEY;
    public static final ArmorMaterial INSTANCE;

    public CrackedCrownArmorMaterial() {
    }

    static {
        CRACKED_CROWN_ARMOR_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath("the-crown", "cracked_crown"));
        INSTANCE = new ArmorMaterial(CrownArmorMaterial.BASE_DURABILLITY, CrownArmorMaterial.INSTANCE.defense(), CrownArmorMaterial.INSTANCE.enchantmentValue(), CrownArmorMaterial.INSTANCE.equipSound(), CrownArmorMaterial.INSTANCE.toughness(), CrownArmorMaterial.INSTANCE.knockbackResistance(), CrownArmorMaterial.INSTANCE.repairIngredient(), CRACKED_CROWN_ARMOR_KEY);
    }
}
