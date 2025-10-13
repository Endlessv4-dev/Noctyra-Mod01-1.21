package dev.endless.v4.init;

import dev.endless.v4.Noctyra;
import dev.endless.v4.list.FoodList;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<ItemConvertible> BLACKLIST = new ArrayList<>();

    public static final Item EXAMPLE_ITEM = register("example_item", new Item(new Item.Settings()));
    public static final Item EXAMPLE_FOOD = register("example_food", new Item(new Item.Settings().food(FoodList.EXAMPLE_FOOD_COMPONENT).maxCount(16)));

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, Noctyra.id(name) , item);
    }

    public static void load() {}
}
