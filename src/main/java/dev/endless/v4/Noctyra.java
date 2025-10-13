package dev.endless.v4;

import dev.endless.v4.init.BlockInit;
import dev.endless.v4.init.ItemGroupInit;
import dev.endless.v4.init.ItemInit;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Noctyra implements ModInitializer {
	public static final String MOD_ID = "noctyra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading...");

		ItemInit.load();
		BlockInit.load();
		ItemGroupInit.load();

		registerIntoVanillaInventory();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void registerIntoVanillaInventory() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
			entries.addAfter(Items.PUMPKIN_PIE, ItemInit.EXAMPLE_FOOD);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
			entries.add(ItemInit.EXAMPLE_ITEM);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
			entries.add(BlockInit.EXAMPLE_BLOCK);
		});
	}
}