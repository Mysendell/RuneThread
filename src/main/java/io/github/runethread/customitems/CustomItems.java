package io.github.runethread.customitems;

import io.github.runethread.RuneThread;
import io.github.runethread.customentities.customEntities;
import io.github.runethread.customitems.runes.LocationRuneItem;
import io.github.runethread.customitems.runes.PowerRuneItem;
import io.github.runethread.customitems.runes.ScaleRuneItem;
import io.github.runethread.customitems.runes.functionrunes.*;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
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

    public static final DeferredItem<Item> CAKE = ITEMS.registerSimpleItem("cake",
            new Item.Properties().food(new FoodProperties.Builder().alwaysEdible()
                    .nutrition(1).saturationModifier(1f).build()));

    public static final DeferredItem<SpawnEggItem> CAKE_GOLEM_SPAWN_EGG =
            ITEMS.registerItem("cake_golem_spawn_egg",
                    properties -> new SpawnEggItem(
                            customEntities.CAKE_GOLEM.get(),
                            properties
                    )
            );

    public static final DeferredItem<Item> POWER_GEM = ITEMS.registerSimpleItem("power_gem",
            new Item.Properties().stacksTo(64).fireResistant());
    public static final DeferredItem<Item> RITUAL_INDICATOR = ITEMS.registerSimpleItem("ritual_indicator",
            new Item.Properties().stacksTo(64).fireResistant());

    public static final DeferredItem<Item> POWER_RUNE = ITEMS.registerItem("power_rune",
            (properties) -> new PowerRuneItem(properties.stacksTo(16).fireResistant()));

    public static final DeferredItem<Item> UP_RUNE = ITEMS.registerSimpleItem("up_rune",
            new Item.Properties().stacksTo(16).fireResistant());
    public static final DeferredItem<Item> DOWN_RUNE = ITEMS.registerSimpleItem("down_rune",
            new Item.Properties().stacksTo(16).fireResistant());
    public static final DeferredItem<Item> LEFT_RUNE = ITEMS.registerSimpleItem("left_rune",
            new Item.Properties().stacksTo(16).fireResistant());
    public static final DeferredItem<Item> RIGHT_RUNE = ITEMS.registerSimpleItem("right_rune",
            new Item.Properties().stacksTo(16).fireResistant());
    public static final DeferredItem<Item> LIVING_RUNE = ITEMS.registerSimpleItem("living_rune",
            new Item.Properties().stacksTo(16).fireResistant());

    public static final DeferredItem<Item> ALPHA_RUNE = ITEMS.registerItem("alpha_rune",
            (properties) -> new LocationRuneItem(properties.stacksTo(16).fireResistant(), 16 * 2));
    public static final DeferredItem<Item> BETA_RUNE = ITEMS.registerItem("beta_rune",
            (properties) -> new LocationRuneItem(properties.stacksTo(16).fireResistant(), 16 * 4));
    public static final DeferredItem<Item> LAMBDA_RUNE = ITEMS.registerItem("lambda_rune",
            (properties) -> new LocationRuneItem(properties.stacksTo(16).fireResistant(), 16 * 8));
    public static final DeferredItem<Item> TAU_RUNE = ITEMS.registerItem("tau_rune",
            (properties) -> new LocationRuneItem(properties.stacksTo(16).fireResistant(), 16 * 16));

    public static final DeferredItem<Item> SCALE_RUNE = ITEMS.registerItem("scale_rune",
            (properties) -> new ScaleRuneItem(properties.stacksTo(16).fireResistant()));


    public static final DeferredItem<Item> CIRCLE_RUNE = ITEMS.registerSimpleItem("circle_rune",
            new Item.Properties().stacksTo(16).fireResistant());
    public static final DeferredItem<Item> SQUARE_RUNE = ITEMS.registerSimpleItem("square_rune",
            new Item.Properties().stacksTo(16).fireResistant());
    public static final DeferredItem<Item> NULL_RUNE = ITEMS.registerSimpleItem("null_rune",
            new Item.Properties().stacksTo(16).fireResistant());
    public static final DeferredItem<Item> REVERSE_RUNE = ITEMS.registerSimpleItem("reverse_rune",
            new Item.Properties().stacksTo(16).fireResistant());


    public static final DeferredItem<Item> COLLAPSE_RUNE = ITEMS.registerItem("collapse_rune",
            (properties) -> new CollapseRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, -1));

    public static final DeferredItem<Item> CREATION_RUNE = ITEMS.registerItem("creation_rune",
            (properties) -> new CreationRuneItem(properties.stacksTo(16).fireResistant(), 10, 1, 1.5f, 0.3));

    public static final DeferredItem<Item> FIRE_RUNE = ITEMS.registerItem("fire_rune",
            (properties) -> new FireRuneItem(properties.stacksTo(16).fireResistant(), 10, 1.5, 1.55f, 0.3));

    public static final DeferredItem<Item> GATHER_RUNE = ITEMS.registerItem("gather_rune",
            (properties) -> new GatherRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, 0.3));

    public static final DeferredItem<Item> LIGHTNING_RUNE = ITEMS.registerItem("lightning_rune",
            (properties) -> new LightningRuneItem(properties.stacksTo(16).fireResistant(), 10, 4, 1.7f, 0.3));

    public static final DeferredItem<Item> TRANSMUTATION_RUNE = ITEMS.registerItem("transmutation_rune",
            (properties) -> new TransmutationRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, 0.3));

    public static final DeferredItem<Item> DESTRUCTION_RUNE = ITEMS.registerItem("destruction_rune",
            (properties) -> new DestructionRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, 0.3));

    public static final DeferredItem<Item> PORTAL_RUNE = ITEMS.registerItem("portal_rune",
            (properties) -> new PortalRuneItem(properties.stacksTo(16).fireResistant(), 15, 1, 1.5f, 0.3));

    public static final DeferredItem<Item> PROTECTION_RUNE = ITEMS.registerItem("protection_rune",
            (properties) -> new ProtectionRuneItem(properties.stacksTo(16).fireResistant(), 10, 10, 1.5f, 0.3));

    public static final DeferredItem<Item>[] simpleItems = new DeferredItem[]{
            UP_RUNE, DOWN_RUNE, LEFT_RUNE, RIGHT_RUNE, LIVING_RUNE,
            ALPHA_RUNE, BETA_RUNE, LAMBDA_RUNE, TAU_RUNE,
            CIRCLE_RUNE, SQUARE_RUNE, NULL_RUNE, REVERSE_RUNE,
            CREATION_RUNE, FIRE_RUNE, GATHER_RUNE,
            LIGHTNING_RUNE, TRANSMUTATION_RUNE, DESTRUCTION_RUNE,
            PORTAL_RUNE, PROTECTION_RUNE, SCALE_RUNE, POWER_GEM
    };

    public static void CreativeTabItems(CreativeModeTab.Output output) {
        output.accept(HAMPTER_ITEM.get());
        output.accept(DOUGH_ITEM.get());
        output.accept(CAKE.get());
        output.accept(CAKE_GOLEM_SPAWN_EGG.get());
        output.accept(POWER_GEM.get());
        output.accept(UP_RUNE.get());
        output.accept(DOWN_RUNE.get());
        output.accept(LEFT_RUNE.get());
        output.accept(RIGHT_RUNE.get());
        output.accept(LIVING_RUNE.get());
        output.accept(ALPHA_RUNE.get());
        output.accept(BETA_RUNE.get());
        output.accept(LAMBDA_RUNE.get());
        output.accept(TAU_RUNE.get());
        output.accept(DESTRUCTION_RUNE.get());
        output.accept(PORTAL_RUNE.get());
        output.accept(PROTECTION_RUNE.get());
        output.accept(COLLAPSE_RUNE.get());
        output.accept(FIRE_RUNE.get());
        output.accept(CREATION_RUNE.get());
        output.accept(LIGHTNING_RUNE.get());
        output.accept(TRANSMUTATION_RUNE.get());
        output.accept(GATHER_RUNE.get());
        output.accept(SQUARE_RUNE.get());
        output.accept(CIRCLE_RUNE.get());
        output.accept(NULL_RUNE.get());
        output.accept(SCALE_RUNE);
        output.accept(REVERSE_RUNE.get());

        ItemStack powerRuneStack = new ItemStack(POWER_RUNE.get());
        powerRuneStack.set(DataComponentRegistry.POWER_DATA, new PowerData(1));
        output.accept(powerRuneStack);
        powerRuneStack = new ItemStack(POWER_RUNE.get());
        powerRuneStack.set(DataComponentRegistry.POWER_DATA, new PowerData(2));
        output.accept(powerRuneStack);
        powerRuneStack = new ItemStack(POWER_RUNE.get());
        powerRuneStack.set(DataComponentRegistry.POWER_DATA, new PowerData(3));
        output.accept(powerRuneStack);
        powerRuneStack = new ItemStack(POWER_RUNE.get());
        powerRuneStack.set(DataComponentRegistry.POWER_DATA, new PowerData(4));
        output.accept(powerRuneStack);
        powerRuneStack = new ItemStack(POWER_RUNE.get());
        powerRuneStack.set(DataComponentRegistry.POWER_DATA, new PowerData(5));
        output.accept(powerRuneStack);
        powerRuneStack = new ItemStack(POWER_RUNE.get());
        powerRuneStack.set(DataComponentRegistry.POWER_DATA, new PowerData(6));
        output.accept(powerRuneStack);
    }
}
