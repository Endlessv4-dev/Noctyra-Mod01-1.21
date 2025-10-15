package dev.endless.v4;

import dev.endless.v4.command.gamemode.GameModeCommand;
import dev.endless.v4.command.spawn.SetSpawnCommand;
import dev.endless.v4.command.spawn.SpawnCommand;
import dev.endless.v4.command.util.NoctyraTickSchedulerUtil;
import dev.endless.v4.init.*;
import dev.endless.v4.init.worldgen.BiomeModifiactionInit;
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
		NoctyraTickSchedulerUtil.init();
		registerInit();
		registerIntoVanillaInventory();
		registerCommands();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	private static void registerInit() {
		ItemInit.load();
		BlockInit.load();
		ItemGroupInit.load();
		BiomeModifiactionInit.load();
		ArmorMaterialInit.load();
		CommandInit.load();
	}

	private static void registerCommands() {
		GameModeCommand.register();
		SpawnCommand.register();
		SetSpawnCommand.register();
	}

	private static void registerIntoVanillaInventory() {
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