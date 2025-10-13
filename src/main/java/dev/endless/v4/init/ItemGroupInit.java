package dev.endless.v4.init;

import dev.endless.v4.Noctyra;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.Optional;

public class ItemGroupInit {
    public static final Text EXAMPLE_TITLE = Text.translatable("itemGroup." + Noctyra.MOD_ID + ".example_group");

    public static final ItemGroup EXAMPLE_GROUP = register("example_group", FabricItemGroup.builder()
            .displayName(EXAMPLE_TITLE)
            .icon(ItemInit.EXAMPLE_ITEM::getDefaultStack)
            .entries((displayContext, entries) -> Registries.ITEM.getIds()
                    .stream()
                    .filter(key -> key.getNamespace().equals(Noctyra.MOD_ID))
                    .map(Registries.ITEM::getOrEmpty)
                    .map(Optional::orElseThrow)
                    .filter(item -> !ItemInit.BLACKLIST.contains(item))
                    .forEach(entries::add))
            .build());

    public static <T extends ItemGroup> T register(String name, T itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, Noctyra.id(name), itemGroup);
    }

    public static void load() {}
}
