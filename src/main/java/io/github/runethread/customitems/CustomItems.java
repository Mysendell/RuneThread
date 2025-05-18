package io.github.runethread.customitems;

import io.github.runethread.RuneThread;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CustomItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RuneThread.MODID);
    public static final DeferredItem<Item> HAMPTER_ITEM = ITEMS.registerSimpleItem("hampter",
            new Item.Properties().food(new FoodProperties.Builder().alwaysEdible()
                    .nutrition(5).saturationModifier(2f).build()));
    public static final DeferredItem<Item> DOUGH_ITEM = ITEMS.registerSimpleItem("dough",
            new Item.Properties().food(new FoodProperties.Builder().alwaysEdible()
                    .nutrition(1).saturationModifier(0.5f).build()));

    public static void CreativeTabItems(CreativeModeTab.Output output){
        output.accept(new ItemStack(HAMPTER_ITEM.get()));
        output.accept(new ItemStack(DOUGH_ITEM.get()));
    }
}
