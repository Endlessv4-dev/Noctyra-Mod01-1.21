package dev.endless.v4.data.provider.lang;

import dev.endless.v4.Noctyra;
import dev.endless.v4.init.BlockInit;
import dev.endless.v4.init.CommandInit;
import dev.endless.v4.init.ItemGroupInit;
import dev.endless.v4.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class NoctyraEnglishLanguageProvider extends FabricLanguageProvider {
    public NoctyraEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    private static void addText(@NotNull TranslationBuilder builder, @NotNull Text text, @NotNull String value) {
        if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
            builder.add(translatableTextContent.getKey(), value);
        } else {
            Noctyra.LOGGER.warn("Failed to add translation for text: {}", text.getString());
        }
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemInit.EXAMPLE_ITEM, "Example Item");
        translationBuilder.add(ItemInit.EXAMPLE_FOOD, "Example Food");
        translationBuilder.add(BlockInit.EXAMPLE_BLOCK, "Example Block");
        addText(translationBuilder, ItemGroupInit.EXAMPLE_TITLE, "Noctyra");
        translationBuilder.add(BlockInit.EXAMPLE_DEEPSLATE_ORE, "Deepslate Example Ore");
        translationBuilder.add(BlockInit.EXAMPLE_END_ORE, "End Example Ore");
        translationBuilder.add(BlockInit.EXAMPLE_NETHER_ORE, "Nether Example Ore");
        translationBuilder.add(BlockInit.EXAMPLE_OVERWORLD_ORE, "Example Ore");
        translationBuilder.add(ItemInit.EXAMPLE_SWORD, "Example Sword");
        translationBuilder.add(ItemInit.EXAMPLE_PICKAXE, "Example Pickaxe");
        translationBuilder.add(ItemInit.EXAMPLE_AXE, "Example Axe");
        translationBuilder.add(ItemInit.EXAMPLE_SHOVEL, "Example Shovel");
        translationBuilder.add(ItemInit.EXAMPLE_HOE, "Example Hoe");
        translationBuilder.add(ItemInit.EXAMPLE_HELMET, "Example Helmet");
        translationBuilder.add(ItemInit.EXAMPLE_CHESTPLATE, "Example Chestplate");
        translationBuilder.add(ItemInit.EXAMPLE_LEGGINGS, "Example Leggings");
        translationBuilder.add(ItemInit.EXAMPLE_BOOTS, "Example Boots");

        translationBuilder.add(CommandInit.CODE_INCORRECT, "INCORRECT | ");
        translationBuilder.add(CommandInit.PLAYER_NOT_FOUND, "Player not found.");

        translationBuilder.add(CommandInit.GAMEMODE_USAGE, "Usage: /gamemode <mode> [player]");
        translationBuilder.add(CommandInit.MODE_CREATIVE, "CREATIVE");
        translationBuilder.add(CommandInit.MODE_SURVIVAL, "SURVIVAL");
        translationBuilder.add(CommandInit.MODE_ADVENTURE, "ADVENTURE");
        translationBuilder.add(CommandInit.MODE_SPECTATOR, "SPECTATOR");
        translationBuilder.add(CommandInit.GAMEMODE_SELF, "Your gamemode has been changed to %s.");
        translationBuilder.add(CommandInit.GAMEMODE_OTHER, "Changed the gamemode for %1$s to %2$s.");

        //translationBuilder.add(CommandInit.TELEPORT_SELF, "You have teleported to spawn.");
        //translationBuilder.add(CommandInit.TELEPORT_OTHERS, "You have teleported %s to spawn.");
        translationBuilder.add(CommandInit.TELEPORTED, "You have been teleported to spawn.");
        translationBuilder.add(CommandInit.TELEPORT_CANCELLED, "You moved, teleport cancelled.");
        translationBuilder.add(CommandInit.TELEPORTING_TIMED, "Teleporting in %s...");
        //translationBuilder.add(CommandInit.TELEPORT_SUCCESSFUL, "Teleported to spawn.");
        translationBuilder.add(CommandInit.SETSPAWN, "You have set the spawn at your current location.");
        translationBuilder.add(CommandInit.SPAWN_NOT_SET_YET, "The spawn hasn't been set yet.");

        translationBuilder.add(CommandInit.NOT_IN_TEAM, "You are not in a team.");
        translationBuilder.add(CommandInit.OTHER_NOT_IN_TEAM, "This player is not in a team.");
        translationBuilder.add(CommandInit.NO_ALLIANCES, "You don't have any alliances.");
        translationBuilder.add(CommandInit.ALLY_CHAT, "[ALLYCHAT]");
        translationBuilder.add(CommandInit.TEAM_CHAT, "[TEAMCHAT]");
        translationBuilder.add(CommandInit.ALLIANCE_REQUEST_PREFIX, "[ALLIANCE REQUEST]");
        translationBuilder.add(CommandInit.TEAM_DOESNT_EXIST, "This team doesn't exist.");
        translationBuilder.add(CommandInit.TEAM_NOT_ALLY, "This team is not your ally.");
        translationBuilder.add(CommandInit.TEAM_NOW_NEUTRAL, "Your team is now neutral with %s.");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_DENY_ALLIANCE, "You don't have permission to deny alliances.");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_ACCEPT_ALLIANCE, "You don't have permission to accept alliances.");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_MAKE_ALLIANCE, "You don't have permission to make alliances.");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_INVITE, "You don't have the required permission to invite players to the team.");
        translationBuilder.add(CommandInit.NO_ALLIANCE_REQUEST_FOUND, "No alliance request found from %s.");
        translationBuilder.add(CommandInit.ALLIANCE_DENIED, "Your alliance request for %s has been denied.");
        translationBuilder.add(CommandInit.ALLIANCE_ACCEPTED, "Your team is now allied with %s.");
        translationBuilder.add(CommandInit.ALLIANCE_DENIED_SELF, "You have denied the alliance request from %s.");
        translationBuilder.add(CommandInit.ALLIANCE, "ALLIANCE");
        translationBuilder.add(CommandInit.CANNOT_ALLY_OWN, "You cannot ally with your own team.");
        translationBuilder.add(CommandInit.NO_PLAYER_PERMISSION_ONLINE_ALLIANCE, "No one in that team that has the permission to make an alliances is online.");
        translationBuilder.add(CommandInit.ALLIANCE_REQUEST_MESSAGE, "%s wants to form an alliance.");
        translationBuilder.add(CommandInit.ALLIANCE_REQUEST_MESSAGE_ND, "Type %1$s to accept, or %2$s to deny.");
        translationBuilder.add(CommandInit.RANK_SCOUT_DESC, "Allows you to invite players to the team.");
        translationBuilder.add(CommandInit.RANK_SCOUTMASTER_DESC, "Allows promote a player to §eSCOUT §7and has the permissions of a scout.");
        translationBuilder.add(CommandInit.RANK_CONSUL_DESC, "Allows you to make alliances with other teams.");
        translationBuilder.add(CommandInit.RANK_DOESNT_EXIST, "This rank doesn't exist.");
        translationBuilder.add(CommandInit.RANK_TITLE, "RANKS:");
        translationBuilder.add(CommandInit.RANK_TOPLIST_INVALID_PAGE, "Invalid page number.");
        translationBuilder.add(CommandInit.PAGE_DOESNT_EXIST, "Page %1$s does not exist! Max page: %2$s");
        translationBuilder.add(CommandInit.TEAM_TOPLIST_TITLE, "TEAM TOPLIST");
        translationBuilder.add(CommandInit.TEAM_TOPLIST_PAGE, "Page");
        translationBuilder.add(CommandInit.TEAM_TOPLIST_NEXT_PAGE, "Type %1$s %2$s to see the next page.");
        translationBuilder.add(CommandInit.CANT_INVITE_SELF, "You cannot invite yourself.");
        translationBuilder.add(CommandInit.PLAYER_ALREADY_IN_TEAM, "This player is already in a team.");
        translationBuilder.add(CommandInit.INVITE_PLAYER_SUCCESS, "You have invited %s to the team.");
        translationBuilder.add(CommandInit.INVITE_MESSAGE, "You have been invited to the %s team.");
        translationBuilder.add(CommandInit.INVITE_MESSAGE_ND, "Type %1$s to join or %2$s to dismiss the invitation.");
        translationBuilder.add(CommandInit.PLAYER_NOT_IN_YOUR_TEAM, "This player is not in your team.");
    }
}
