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
    }
}
