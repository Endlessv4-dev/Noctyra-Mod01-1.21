package dev.endless.v4;

import dev.endless.v4.data.provider.NoctyraBlockLootTableProvider;
import dev.endless.v4.data.provider.NoctyraBlockTagProvider;
import dev.endless.v4.data.provider.lang.NoctyraEnglishLanguageProvider;
import dev.endless.v4.data.provider.lang.NoctyraHungarianLanguageProvider;
import dev.endless.v4.data.provider.NoctyraModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class NoctyraDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(NoctyraModelProvider::new);
		pack.addProvider(NoctyraEnglishLanguageProvider::new);
		pack.addProvider(NoctyraHungarianLanguageProvider::new);
		pack.addProvider(NoctyraBlockLootTableProvider::new);
		pack.addProvider(NoctyraBlockTagProvider::new);
	}
}
