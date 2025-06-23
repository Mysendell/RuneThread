package io.github.runethread;

import com.mojang.logging.LogUtils;
import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customblocks.CustomBlocks;
import io.github.runethread.customblocks.craftingtable.altar.RunicAltarEntityRenderer;
import io.github.runethread.customeffects.CustomEffects;
import io.github.runethread.customentities.cakegolem.CakeGolem;
import io.github.runethread.customentities.cakegolem.CakeGolemModel;
import io.github.runethread.customentities.cakegolem.CakeGolemRenderer;
import io.github.runethread.customentities.customEntities;
import io.github.runethread.customitems.CustomItems;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datagen.properties.PowerRune;
import io.github.runethread.gui.CustomMenus;
import io.github.runethread.gui.screens.AnimatorScreen;
import io.github.runethread.gui.screens.ArcaneScreen;
import io.github.runethread.gui.screens.RusticAltarScreen;
import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.util.RuneConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static io.github.runethread.customblocks.CustomBlocks.BLOCKS;
import static io.github.runethread.customitems.CustomItems.HAMPTER_ITEM;
import static io.github.runethread.customitems.CustomItems.ITEMS;

@Mod(RuneThread.MODID)
public class RuneThread {

    public static final String MODID = "runethread";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RUNE_THREAD_TAB =
            CREATIVE_MODE_TABS.register("runethread", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.runethread.runethread_tab"))
                    .icon(() -> new ItemStack(HAMPTER_ITEM.get()))
                    .displayItems((params, output) -> {
                        CustomBlocks.CreativeTabBlocks(output);
                        CustomItems.CreativeTabItems(output);
                    })
                    .build());

    public RuneThread(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, RuneConfig.SPEC);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
        CustomRecipes.RECIPE_TYPES.register(modEventBus);
        CustomRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        BLOCKS.register(modEventBus);
        customEntities.ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        CustomMenus.MENUS.register(modEventBus);
        CustomBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        CustomEffects.EFFECTS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        /*Runs on server start*/
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerRangeProps(RegisterRangeSelectItemModelPropertyEvent event) {
            event.register(
                    ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "power_rune"),  // use your ID
                    PowerRune.MAP_CODEC
            );
        }
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(CustomMenus.ARCANE_MENU.get(), ArcaneScreen::new);
            event.register(CustomMenus.ANIMATOR_MENU.get(), AnimatorScreen::new);
            event.register(CustomMenus.RUSTIC_ALTAR_MENU.get(), RusticAltarScreen::new);
        }
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(customEntities.CAKE_GOLEM.get(), CakeGolemRenderer::new);
        }
        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(CakeGolemModel.LAYER_LOCATION, CakeGolemModel::createBodyLayer);
        }
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(customEntities.CAKE_GOLEM.get(), CakeGolem.createAttributes().build());
        }
        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(
                    CustomBlockEntities.RUNIC_ALTAR.get(),
                    RunicAltarEntityRenderer::new
            );
        }
    }
}