package dev.endless.v4.data.provider.lang;

import dev.endless.v4.Noctyra;
import dev.endless.v4.init.BlockInit;
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
    }
}
