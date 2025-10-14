package dev.endless.v4.list;

import dev.endless.v4.Noctyra;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class TagList {
    public static class Blocks {
        public static final TagKey<Block> EXAMPLE_TAG = TagKey.of(RegistryKeys.BLOCK, Noctyra.id("example"));
        public static final TagKey<Block> INCORRECT_FOR_EXAMPLE_TOOL = TagKey.of(RegistryKeys.BLOCK, Noctyra.id("incorrect_for_example_tool"));
    }
}
