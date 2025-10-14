package dev.endless.v4.init;

import dev.endless.v4.Noctyra;
import dev.endless.v4.list.FoodList;
import dev.endless.v4.list.enums.NoctyraToolMaterials;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<ItemConvertible> BLACKLIST = new ArrayList<>();

    public static final Item EXAMPLE_ITEM = register("example_item", new Item(new Item.Settings()));
    public static final Item EXAMPLE_FOOD = register("example_food", new Item(new Item.Settings().food(FoodList.EXAMPLE_FOOD_COMPONENT).maxCount(16)));

    public static final SwordItem EXAMPLE_SWORD = register("example_sword",
            new SwordItem(NoctyraToolMaterials.EXAMPLE, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(NoctyraToolMaterials.EXAMPLE, 3, -2.4f))));

    public static final PickaxeItem EXAMPLE_PICKAXE = register("example_pickaxe",
            new PickaxeItem(NoctyraToolMaterials.EXAMPLE, new Item.Settings()
                    .attributeModifiers(PickaxeItem.createAttributeModifiers(NoctyraToolMaterials.EXAMPLE, 1, -2.8F))));

    public static final AxeItem EXAMPLE_AXE = register("example_axe",
            new AxeItem(NoctyraToolMaterials.EXAMPLE, new Item.Settings()
                    .attributeModifiers(AxeItem.createAttributeModifiers(NoctyraToolMaterials.EXAMPLE, 5, -3.0F))));

    public static final ShovelItem EXAMPLE_SHOVEL = register("example_shovel",
            new ShovelItem(NoctyraToolMaterials.EXAMPLE, new Item.Settings()
                    .attributeModifiers(ShovelItem.createAttributeModifiers(NoctyraToolMaterials.EXAMPLE, 1.5F, -3.0F))));

    public static final HoeItem EXAMPLE_HOE = register("example_hoe",
            new HoeItem(NoctyraToolMaterials.EXAMPLE, new Item.Settings()
                    .attributeModifiers(HoeItem.createAttributeModifiers(NoctyraToolMaterials.EXAMPLE, 0, -3.0F))));

    public static final ArmorItem EXAMPLE_HELMET = register("example_helmet",
            new ArmorItem(ArmorMaterialInit.EXAMPLE, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(45))));

    public static final ArmorItem EXAMPLE_CHESTPLATE = register("example_chestplate",
            new ArmorItem(ArmorMaterialInit.EXAMPLE, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(45))));

    public static final ArmorItem EXAMPLE_LEGGINGS = register("example_leggings",
            new ArmorItem(ArmorMaterialInit.EXAMPLE, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(45))));

    public static final ArmorItem EXAMPLE_BOOTS = register("example_boots",
            new ArmorItem(ArmorMaterialInit.EXAMPLE, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(45))));

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, Noctyra.id(name) , item);
    }

    public static void load() {}
}
