package dev.endless.v4.init;

import dev.endless.v4.Noctyra;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockInit {
    public static final Block EXAMPLE_BLOCK = registerWithItem("example_block", new Block(AbstractBlock.Settings.create()
            .strength(1.5F, 6.0F)
            .requiresTool()));

    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, Noctyra.id(name), block);
    }

    public static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
        T registered = register(name, block);
        ItemInit.register(name, new BlockItem(registered, settings));
        return registered;
    }

    public static <T extends Block> T registerWithItem(String name, T block) {
        return registerWithItem(name, block, new Item.Settings());
    }

    public static void load() {}
}
