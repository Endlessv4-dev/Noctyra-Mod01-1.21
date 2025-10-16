package dev.endless.v4;

import dev.endless.v4.command.gamemode.GameModeCommand;
import dev.endless.v4.command.spawn.SetSpawnCommand;
import dev.endless.v4.command.spawn.SpawnCommand;
import dev.endless.v4.command.team.TeamCommand;
import dev.endless.v4.command.team.TeamDataManager;
import dev.endless.v4.command.team.TeamManager;
import dev.endless.v4.command.util.NoctyraTickSchedulerUtil;
import dev.endless.v4.init.*;
import dev.endless.v4.init.worldgen.BiomeModifiactionInit;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Noctyra implements ModInitializer {
	public static final String MOD_ID = "noctyra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final List<String> teams = new ArrayList<>();
	public static final Map<UUID, String> playerInTeam = new HashMap<>();
	public static final Map<UUID, String> teamOwner = new HashMap<>();
	public static final List<String> TEAM_COMMANDS = new ArrayList<>();

	public static TeamDataManager teamDataManager;

	@Override
	public void onInitialize() {
		LOGGER.info("Loading...");

		teamDataManager = new TeamDataManager();
		teamDataManager.loadTeams();

		NoctyraTickSchedulerUtil.init();

		registerInit();
		registerIntoVanillaInventory();
		registerCommands();
		registerTeamMessage();
		registerHelpCommands();
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
		TeamCommand.register();
	}

	private static void registerHelpCommands() {
		TEAM_COMMANDS.add("toplist");
		TEAM_COMMANDS.add("balance");
		TEAM_COMMANDS.add("deposit §e(amount)");
		TEAM_COMMANDS.add("home");
		TEAM_COMMANDS.add("create §e(team)");
		TEAM_COMMANDS.add("join §e(team)");
		TEAM_COMMANDS.add("setowner §e(player)");
		TEAM_COMMANDS.add("info §e(team/player)");
		TEAM_COMMANDS.add("chat §e<message>");
		TEAM_COMMANDS.add("allychat §e<message>");
		TEAM_COMMANDS.add("[ally/neutral] §e(team)");
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

	private static void registerTeamMessage() {
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
			TeamManager tm = TeamManager.getInstance();

			if (tm.hasTeam(sender)) {
				String team = tm.getTeam(sender);
				Formatting color = tm.getTeamColor(team);

				MutableText customMsg = Text.literal("[")
						.formatted(Formatting.DARK_GRAY)
						.append(Text.literal(team).formatted(color))
						.append(Text.literal("] ").formatted(Formatting.DARK_GRAY))
						.append(Text.literal(sender.getName().getString() + ": ").formatted(Formatting.GRAY))
						.append(message.getContent().copy());

				// Send to all players manually
				for (ServerPlayerEntity player : sender.getServer().getPlayerManager().getPlayerList()) {
					player.sendMessage(customMsg);
				}

				// Returning false stops the original broadcast
				return false;
			}

			return true; // non-team players -> normal chat
		});
	}
}