package mc.craig.software.craftplus.data;

import com.mojang.datafixers.util.Pair;
import mc.craig.software.craftplus.common.ModEntities;
import mc.craig.software.craftplus.common.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(DataGenerator p_124437_) {
        super(p_124437_);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of(Pair.of(ModEntityLoot::new, LootContextParamSets.ENTITY));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        for (Map.Entry<ResourceLocation, LootTable> entry : map.entrySet())
            LootTables.validate(validationtracker, entry.getKey(), entry.getValue());
    }

    public static class ModEntityLoot extends EntityLoot {
        @Override
        protected void addTables() {
            add(ModEntities.OWL.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.OWL_FEATHER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
            add(ModEntities.STALKER.get(), LootTable.lootTable());
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            ArrayList<EntityType<?>> entityTypes = new ArrayList<>();
            for (RegistryObject<EntityType<?>> entry : ModEntities.ENTITY_TYPES.getEntries()) {
                entityTypes.add(entry.get());
            }
            return entityTypes;
        }
    }
}
