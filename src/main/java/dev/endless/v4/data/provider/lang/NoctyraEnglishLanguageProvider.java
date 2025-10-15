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
    }
}
