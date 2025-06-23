package io.github.runethread.customitems;

import io.github.runethread.RuneThread;
import io.github.runethread.customeffects.CustomEffects;
import io.github.runethread.customentities.customEntities;
import io.github.runethread.customitems.runes.LocationRuneItem;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import io.github.runethread.util.ExplosionUtil;
import io.github.runethread.util.TeleportUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
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
    public static final DeferredItem<Item> RITUAL_INDICATOR_NEUTRAL = ITEMS.registerSimpleItem("ritual_indicator_neutral",
            new Item.Properties().stacksTo(64).fireResistant());
    public static final DeferredItem<Item> RITUAL_INDICATOR_SUCCESS = ITEMS.registerSimpleItem("ritual_indicator_success",
            new Item.Properties().stacksTo(64).fireResistant());
    public static final DeferredItem<Item> RITUAL_INDICATOR_FAIL = ITEMS.registerSimpleItem("ritual_indicator_fail",
            new Item.Properties().stacksTo(64).fireResistant());

    public static final DeferredItem<Item> POWER_RUNE = ITEMS.registerSimpleItem("power_rune",
            new Item.Properties().stacksTo(16).fireResistant());

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


    public static final DeferredItem<Item> DESTRUCTION_RUNE = ITEMS.registerItem("destruction_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f,
                    (level, player, item, finalScale, posX, posY, posZ, additionalData) -> {
                        ExplosionUtil.accurateExplosion(level, new BlockPos(posX, posY, posZ), finalScale * item.getScale(),
                                false, true, true, item.getScale() * finalScale * 1.5f);
                    }));

    public static final DeferredItem<Item> PORTAL_RUNE = ITEMS.registerItem("portal_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 15, 1, 1.5f,
                    (level, player, item, finalScale, posX, posY, posZ, additionalData) -> {
                        if(!TeleportUtil.teleportToNearestSafe(player, level, new BlockPos(posX, posY, posZ),
                                20 , 20))
                            player.sendSystemMessage(Component.literal("Teleport failed: No safe location found!").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                    }));
    public static final DeferredItem<Item> PROTECTION_RUNE = ITEMS.registerItem("protection_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 10, 1.5f,
                    (level, player, item, finalScale, posX, posY, posZ, additionalData) -> {
                        LivingEntity entity = (LivingEntity) additionalData[0];
                        if (entity != null) {
                            entity.addEffect(new MobEffectInstance(CustomEffects.IMMUNITY, 20 * finalScale * item.getScale()));
                        }
                    }));

    public static void CreativeTabItems(CreativeModeTab.Output output) {
        output.accept(new ItemStack(HAMPTER_ITEM.get()));
        output.accept(new ItemStack(DOUGH_ITEM.get()));
        output.accept(new ItemStack(CAKE.get()));
        output.accept(new ItemStack(CAKE_GOLEM_SPAWN_EGG.get()));
        output.accept(new ItemStack(POWER_GEM.get()));
        output.accept(new ItemStack(UP_RUNE.get()));
        output.accept(new ItemStack(DOWN_RUNE.get()));
        output.accept(new ItemStack(LEFT_RUNE.get()));
        output.accept(new ItemStack(RIGHT_RUNE.get()));
        output.accept(new ItemStack(LIVING_RUNE.get()));
        output.accept(new ItemStack(ALPHA_RUNE.get()));
        output.accept(new ItemStack(BETA_RUNE.get()));
        output.accept(new ItemStack(LAMBDA_RUNE.get()));
        output.accept(new ItemStack(TAU_RUNE.get()));
        output.accept(new ItemStack(DESTRUCTION_RUNE.get()));
        output.accept(new ItemStack(PORTAL_RUNE.get()));
        output.accept(new ItemStack(PROTECTION_RUNE.get()));

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

    }
}
