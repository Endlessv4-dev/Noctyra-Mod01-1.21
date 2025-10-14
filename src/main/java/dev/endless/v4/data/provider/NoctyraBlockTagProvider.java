package dev.endless.v4.data.provider;

import dev.endless.v4.Noctyra;
import dev.endless.v4.init.BlockInit;
import dev.endless.v4.list.TagList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class NoctyraBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public NoctyraBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockInit.EXAMPLE_BLOCK)
                .add(BlockInit.EXAMPLE_DEEPSLATE_ORE)
                .add(BlockInit.EXAMPLE_END_ORE)
                .add(BlockInit.EXAMPLE_NETHER_ORE)
                .add(BlockInit.EXAMPLE_OVERWORLD_ORE);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BlockInit.EXAMPLE_BLOCK)
                .add(BlockInit.EXAMPLE_DEEPSLATE_ORE)
                .add(BlockInit.EXAMPLE_END_ORE)
                .add(BlockInit.EXAMPLE_NETHER_ORE)
                .add(BlockInit.EXAMPLE_OVERWORLD_ORE);

        getOrCreateTagBuilder(TagList.Blocks.EXAMPLE_TAG)
                .add(BlockInit.EXAMPLE_BLOCK)
                .add(Blocks.BLUE_ORCHID);

        getOrCreateTagBuilder(TagList.Blocks.INCORRECT_FOR_EXAMPLE_TOOL);
    }
}
