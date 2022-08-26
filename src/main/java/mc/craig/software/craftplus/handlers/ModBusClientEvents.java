package mc.craig.software.craftplus.handlers;

import mc.craig.software.craftplus.client.layers.OwlShoulderLayer;
import mc.craig.software.craftplus.client.layers.PlayerGliderLayer;
import mc.craig.software.craftplus.client.models.Models;
import mc.craig.software.craftplus.client.renderers.RenderAdvancedArrow;
import mc.craig.software.craftplus.client.renderers.RenderOwl;
import mc.craig.software.craftplus.client.renderers.RenderStalker;
import mc.craig.software.craftplus.common.ModEntities;
import mc.craig.software.craftplus.common.ModItems;
import mc.craig.software.craftplus.common.ModMenus;
import mc.craig.software.craftplus.common.items.ParagliderItem;
import mc.craig.software.craftplus.common.items.TierArmorItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusClientEvents {

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.STALKER.get(), RenderStalker::new);
        event.registerEntityRenderer(ModEntities.ADVANCED_ARROW.get(), RenderAdvancedArrow::new);
        event.registerEntityRenderer(ModEntities.OWL.get(), RenderOwl::new);
    }

    @SubscribeEvent
    public static void regModels(EntityRenderersEvent.RegisterLayerDefinitions definitions) {
        Models.init(definitions);
    }

    @SubscribeEvent
    public static void renderLayers(EntityRenderersEvent.AddLayers addLayers) {
        addLayers.getSkins().forEach(skin -> {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> renderer = addLayers.getSkin(skin);
            renderer.addLayer(new PlayerGliderLayer(renderer));
            renderer.addLayer(new OwlShoulderLayer(renderer, addLayers.getEntityModels()));
        });
    }

    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        ModBusClientEvents.itemPredicates();
        ModMenus.registerScreens();
    }

    @SubscribeEvent
    public static void onColors(RegisterColorHandlersEvent.Item item) {
        for (RegistryObject<Item> entry : ModItems.ITEMS.getEntries()) {
            if (entry.get() instanceof TierArmorItem dyeableArmorItem) {
                item.register((itemStack, p_92673_) -> dyeableArmorItem.getTier().getArmorAction().getColor(), entry.get());
            }
        }
    }

    private static void itemPredicates() {
        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            if (entry.getValue() instanceof ParagliderItem paragliderItem) {
                ItemProperties.register(paragliderItem, new ResourceLocation("copper_mod"), (stack, p_call_2_, livingEntity, something) -> ParagliderItem.hasCopperMod(stack) ? 1 : 0);
            }
        }
    }

}
