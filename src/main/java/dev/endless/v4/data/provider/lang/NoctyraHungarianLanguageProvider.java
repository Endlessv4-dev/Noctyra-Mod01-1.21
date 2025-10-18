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

public class NoctyraHungarianLanguageProvider extends FabricLanguageProvider {
    public NoctyraHungarianLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "hu_hu", registryLookup);
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
        translationBuilder.add(ItemInit.EXAMPLE_ITEM, "Példatárgy");
        translationBuilder.add(ItemInit.EXAMPLE_FOOD, "Példa Étel");
        translationBuilder.add(BlockInit.EXAMPLE_BLOCK, "Példablokk");
        addText(translationBuilder, ItemGroupInit.EXAMPLE_TITLE, "Noctyra");
        translationBuilder.add(BlockInit.EXAMPLE_DEEPSLATE_ORE, "Példaérc mélypalában");
        translationBuilder.add(BlockInit.EXAMPLE_END_ORE, "Endpéldaérc");
        translationBuilder.add(BlockInit.EXAMPLE_NETHER_ORE, "Netherpéldaérc");
        translationBuilder.add(BlockInit.EXAMPLE_OVERWORLD_ORE, "Példaérc");
        translationBuilder.add(ItemInit.EXAMPLE_SWORD, "Példakard");
        translationBuilder.add(ItemInit.EXAMPLE_PICKAXE, "Példacsákány");
        translationBuilder.add(ItemInit.EXAMPLE_AXE, "Példafejsze");
        translationBuilder.add(ItemInit.EXAMPLE_SHOVEL, "Példaásó");
        translationBuilder.add(ItemInit.EXAMPLE_HOE, "Példakapa");
        translationBuilder.add(ItemInit.EXAMPLE_HELMET, "Példasisak");
        translationBuilder.add(ItemInit.EXAMPLE_CHESTPLATE, "Példamellvért");
        translationBuilder.add(ItemInit.EXAMPLE_LEGGINGS, "Példa lábszárvédő");
        translationBuilder.add(ItemInit.EXAMPLE_BOOTS, "Példabakancs");

        translationBuilder.add(CommandInit.CODE_INCORRECT, "HELYTELEN | ");
        translationBuilder.add(CommandInit.PLAYER_NOT_FOUND, "Játékos nem található.");

        translationBuilder.add(CommandInit.GAMEMODE_USAGE, "Használat: /gamemode <mód> [játékos]");
        translationBuilder.add(CommandInit.MODE_CREATIVE, "KREATÍV");
        translationBuilder.add(CommandInit.MODE_SURVIVAL, "TÚLÉLŐ");
        translationBuilder.add(CommandInit.MODE_ADVENTURE, "KALAND");
        translationBuilder.add(CommandInit.MODE_SPECTATOR, "SZEMLÉLŐ");
        translationBuilder.add(CommandInit.GAMEMODE_SELF, "A játékmódod megváltozott a következőre: %s.");
        translationBuilder.add(CommandInit.GAMEMODE_OTHER, "Megváltoztattad %1$s játékmódját a következőre: %2$s.");

        //translationBuilder.add(CommandInit.TELEPORT_SELF, "Teleportálva a spawnra.");
        //translationBuilder.add(CommandInit.TELEPORT_OTHERS, "Teleportáltad %s játékost a spawnra.");
        translationBuilder.add(CommandInit.TELEPORTED, "El lettél teleportálva a spawnra.");
        translationBuilder.add(CommandInit.TELEPORT_CANCELLED, "Megmozdultál, a teleportálás megszakítva.");
        translationBuilder.add(CommandInit.TELEPORTING_TIMED, "Teleportálás %s...");
        //translationBuilder.add(CommandInit.TELEPORT_SUCCESSFUL, "Teleported to spawn.");
        translationBuilder.add(CommandInit.SETSPAWN, "Spawn beállítva a jelenlegi pozíciódra.");
        translationBuilder.add(CommandInit.SPAWN_NOT_SET_YET, "A spawn még nincs beállítva.");

        translationBuilder.add(CommandInit.NOT_IN_TEAM, "Nem vagy benne egy csapatban.");
        translationBuilder.add(CommandInit.OTHER_NOT_IN_TEAM, "Ez a játékos nincs csapatban.");
        translationBuilder.add(CommandInit.NO_ALLIANCES, "Nincsenek szövetségesek.");
        translationBuilder.add(CommandInit.ALLY_CHAT, "[SZÖVETSÉGES]");
        translationBuilder.add(CommandInit.TEAM_CHAT, "[CSAPAT]");
        translationBuilder.add(CommandInit.ALLIANCE_REQUEST_PREFIX, "[SZÖVETSÉGI KÉRELEM]");
        translationBuilder.add(CommandInit.TEAM_DOESNT_EXIST, "Ez a csapat nem létezik.");
        translationBuilder.add(CommandInit.TEAM_NOT_ALLY, "Ez a csapat nem a szövetségesed.");
        translationBuilder.add(CommandInit.TEAM_NOW_NEUTRAL, "A csapatod már nem szövetséges %s csapattal.");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_DENY_ALLIANCE, "Nincs jogosultságod szövetségek elutasításához.");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_ACCEPT_ALLIANCE, "Nincs jogosultságod szövetségek elfogadásához.");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_MAKE_ALLIANCE, "Nincs jogosultságod szövetségeket kötni..");
        translationBuilder.add(CommandInit.NO_PERMISSION_TO_INVITE, "Nincs jogosultságod játékosok meghívásához.");
        translationBuilder.add(CommandInit.NO_ALLIANCE_REQUEST_FOUND, "Nem találtam szövetség kérelmet tőle: %s.");
        translationBuilder.add(CommandInit.ALLIANCE_DENIED, "A szövetségi kérelem el lett utasítva %s által.");
        translationBuilder.add(CommandInit.ALLIANCE_ACCEPTED, "A csapatod mostantól szövetségben áll %s csapattal.");
        translationBuilder.add(CommandInit.ALLIANCE_DENIED_SELF, "Elutasítottad a szövetségi kérelmet, amelyet %s küldött.");
        translationBuilder.add(CommandInit.ALLIANCE, "SZÖVETSÉG");
        translationBuilder.add(CommandInit.CANNOT_ALLY_OWN, "Nem szövetkezhetsz a saját csapatoddal.");
        translationBuilder.add(CommandInit.NO_PLAYER_PERMISSION_ONLINE_ALLIANCE, "Nincs fent senki az említett csapatból, akinek van jogosultsága szövetséget kötni.");
        translationBuilder.add(CommandInit.ALLIANCE_REQUEST_MESSAGE, "%s szövetséget akar kötni.");
        translationBuilder.add(CommandInit.ALLIANCE_REQUEST_MESSAGE_ND, "Írd %1$s az elfogadáshoz, or %2$s az elutasításhoz.");
        translationBuilder.add(CommandInit.RANK_SCOUT_DESC, "Jogosultság játékosok meghívására a csapathoz.");
        translationBuilder.add(CommandInit.RANK_SCOUTMASTER_DESC, "Jogosultság a %s rang adáshoz és jogaihoz.");
        translationBuilder.add(CommandInit.RANK_CONSUL_DESC, "Jogosultság szövetség kötésekhez.");
        translationBuilder.add(CommandInit.RANK_DOESNT_EXIST, "Ez a rang nem létezik.");
        translationBuilder.add(CommandInit.RANK_TITLE, "RANGOK:");
        translationBuilder.add(CommandInit.RANK_TOPLIST_INVALID_PAGE, "Helytelen oldalszám.");
        translationBuilder.add(CommandInit.PAGE_DOESNT_EXIST, "Az oldal %1$s nem létezik! Max oldal: %2$s");
        translationBuilder.add(CommandInit.TEAM_TOPLIST_TITLE, "CSAPAT TOPLISTA");
        translationBuilder.add(CommandInit.TEAM_TOPLIST_PAGE, "Oldal");
        translationBuilder.add(CommandInit.TEAM_TOPLIST_NEXT_PAGE, "Írd %1$s %2$s hogy lásd a kövezkető oldalt.");
        translationBuilder.add(CommandInit.CANT_INVITE_SELF, "Nem hívhatod meg magad.");
        translationBuilder.add(CommandInit.PLAYER_ALREADY_IN_TEAM, "Ez a játékos már csapatban van.");
        translationBuilder.add(CommandInit.INVITE_PLAYER_SUCCESS, "Meghívtad %s játékost a csapatba.");
        translationBuilder.add(CommandInit.INVITE_MESSAGE, "Meglettél hívva a %s csapatba.");
        translationBuilder.add(CommandInit.INVITE_MESSAGE_ND, "Írd %1$s hogy elfogadd vagy %2$s hogy elutasítsd a meghívást.");
        translationBuilder.add(CommandInit.PLAYER_NOT_IN_YOUR_TEAM, "Ez a játékos nem a te csapatodban van.");
    }
}
