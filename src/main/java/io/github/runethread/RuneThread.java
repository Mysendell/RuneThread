package io.github.runethread;

import io.github.runethread.customblocks.CustomBlocks;
import io.github.runethread.customitems.CustomItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

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

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static void registerRecipes(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            event.register(Registries.RECIPE_SERIALIZER,
                    new ResourceLocation("runethread", "arcane_crafting"),
                    () -> ArcaneRecipeSerializer.INSTANCE);
        }
    }
}
