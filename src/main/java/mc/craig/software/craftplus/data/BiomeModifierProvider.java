package mc.craig.software.craftplus.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import mc.craig.software.craftplus.MinecraftPlus;
import mc.craig.software.craftplus.common.ModEntities;
import mc.craig.software.craftplus.util.Tags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public record BiomeModifierProvider(DataGenerator dataGenerator) implements DataProvider {

    public static void generate(RegistryOps<JsonElement> ops, BiomeModifier modifier, Path outputFolder, String saveName, CachedOutput cache) {
        final String directory = PackType.SERVER_DATA.getDirectory();
        final ResourceLocation biomeModifiersRegistryID = ForgeRegistries.Keys.BIOME_MODIFIERS.location();

        final String biomeModifierPathString = String.join("/", directory, MinecraftPlus.MODID, biomeModifiersRegistryID.getNamespace(), biomeModifiersRegistryID.getPath(), saveName + ".json");

        BiomeModifier.DIRECT_CODEC.encodeStart(ops, modifier).resultOrPartial(msg -> MinecraftPlus.LOGGER.error("Failed to encode {}: {}", biomeModifierPathString, msg)).ifPresent(json ->
        {
            try {
                final Path biomeModifierPath = outputFolder.resolve(biomeModifierPathString);
                DataProvider.saveStable(cache, json, biomeModifierPath);
            } catch (
                    IOException e) {
                MinecraftPlus.LOGGER.error("Failed to save " + biomeModifierPathString, e);
            }
        });
    }

    @Override
    public void run(@NotNull CachedOutput cachedOutput) {

        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
        final Path outputFolder = this.dataGenerator.getOutputFolder();

        // Biome Modifiers
        BiomeModifier spawnsModifier = new ForgeBiomeModifiers.AddSpawnsBiomeModifier(new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), Tags.OWL_SPAWNS), List.of(new MobSpawnSettings.SpawnerData(ModEntities.OWL.get(), 40, 1, 2)));

        // Generate BiomeModiers
        generate(ops, spawnsModifier, outputFolder, "owl_spawns", cachedOutput);

    }

    @Override
    public @NotNull String getName() {
        return MinecraftPlus.MODID + " Biome Modifiers";
    }
}