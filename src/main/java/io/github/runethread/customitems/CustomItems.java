package io.github.runethread.customitems;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customeffects.CustomEffects;
import io.github.runethread.customentities.customEntities;
import io.github.runethread.customitems.runes.*;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import io.github.runethread.datacomponents.StructureData;
import io.github.runethread.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;
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
            (properties) -> new CollapseRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, -1,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        ListTag listTag = new ListTag();
                        ItemStack finalStack = stack.copy();
                        finalStack.setCount(1);
                        stack.shrink(1);
                        BlockPos origin = ((RunicAltarEntity.DestinationRuneData) additionalData.get("origin")).getBlockPos();
                        MainRuneItem item = (MainRuneItem) stack.getItem();
                        AreaUtil.IterateAreaBox(level, destination.getBlockPos(), finalScale * item.getScale(), (args) -> {
                            BlockPos pos = (BlockPos) args[0];
                            listTag.add(StructureUtil.serializeBlock(pos, destination.getBlockPos(), level));
                            level.removeBlock(pos, false);
                        });
                        CompoundTag radius = new CompoundTag(); // TODO save structure using structureData not saving every block
                        radius.putDouble("radius", finalScale * item.getScale());
                        listTag.addLast(radius);
                        finalStack.set(DataComponentRegistry.STRUCTURE_DATA, new StructureData(listTag));
                        finalStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
                        InventoryUtil.dropStack(origin, level, finalStack);
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));

    public static final DeferredItem<Item> CREATION_RUNE = ITEMS.registerItem("creation_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 1, 1.5f, 0.3, // TODO make cost same amount as max power rune
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        if (!additionalData.containsKey("Collapse Rune")) {
                            ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Creation rune requires a Collapse Rune to be used!", level);
                            return RunicAltarEntity.RitualState.ALMOST;
                        }
                        ItemStack collapseStack = (ItemStack) additionalData.get("Collapse Rune");
                        if (!collapseStack.has(DataComponentRegistry.STRUCTURE_DATA)) { // TODO change to item data Don't duplicate structures
                            ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Creation rune requires a Collapse Rune with a valid structure data!", level);
                            return RunicAltarEntity.RitualState.ALMOST;
                        }
                        ItemStack finalStack = collapseStack.copy();
                        finalStack.setCount(1);
                        BlockPos origin = ((RunicAltarEntity.DestinationRuneData) additionalData.get("origin")).getBlockPos();
                        InventoryUtil.dropStack(origin, level, finalStack);
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));

    public static final DeferredItem<Item> FIRE_RUNE = ITEMS.registerItem("fire_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 1.5, 1.55f, 0.3,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        double radius = finalScale * ((MainRuneItem) stack.getItem()).getScale();
                        BlockPos center = destination.getBlockPos();
                        center = center.offset(0, 1, 0);
                        AreaUtil.IterateAreaSphere(level, center, radius, (args) -> {
                            BlockPos pos = (BlockPos) args[0];
                            if (BaseFireBlock.canBePlacedAt(level, pos, Direction.DOWN)) {
                                BlockState blockstate = BaseFireBlock.getState(level, pos);
                                level.setBlockAndUpdate(pos, blockstate);
                            }
                        });
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));

    public static final DeferredItem<Item> GATHER_RUNE = ITEMS.registerItem("gather_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, 0.3,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        if(reference == null){
                            ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "You need a reference rune!", level);
                            return RunicAltarEntity.RitualState.ALMOST;
                        }

                        AABB area = new AABB(destination.getBlockPos());
                        area = area.inflate(finalScale * ((MainRuneItem) stack.getItem()).getScale()); // TODO add some visual indication of area
                        NonNullList<ItemStack> items = NonNullList.create();
                        for (Entity entity : level.getEntities(null, area)) {
                            if (entity instanceof ItemEntity itemEntity) {
                                ItemStack itemStack = itemEntity.getItem();
                                items.add(itemStack);
                                entity.discard();
                            }
                        }
                        BlockPos destinationBlock = reference.getBlockPos();
                        InventoryUtil.addItemStackHandlerToBlock(level, destinationBlock, new ItemStackHandler(items));
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));

    public static final DeferredItem<Item> LIGHTNING_RUNE = ITEMS.registerItem("lightning_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 4, 1.7f, 0.3,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        double radiusScale, strengthScale;
                        radiusScale = strengthScale = finalScale;
                        MainRuneItem item = (MainRuneItem) stack.getItem();
                        if (additionalData.containsKey("Scale Rune: MODIFIER")) {
                            ItemStack scaleRune = (ItemStack) additionalData.get("Scale Rune: MODIFIER");
                            double scale = scaleRune.get(DataComponentRegistry.SCALE_DATA).scale();
                            if (scale < 0)
                                scale = 1 / scale;
                            scale = Math.abs(scale);
                            radiusScale *= scale;
                            strengthScale *= 1 / scale;
                        }

                        int radius = (int) Math.round(radiusScale / item.getScale());
                        double strength = strengthScale % item.getScale();
                        strength = strength == 0.0 ? 4.0 : strength;
                        int finalStrength = (int) Math.pow(2, strength - 1);
                        float damage = (float) (strengthScale / (item.getScale() * 2.0 * finalStrength));
                        AreaUtil.IterateAreaCircle(level, destination.getBlockPos(), radius, (args) -> {
                            BlockPos pos = (BlockPos) args[0];
                            for (int i = 0; i < finalStrength; i++)
                                EntityType.LIGHTNING_BOLT.spawn(level, pos, EntitySpawnReason.TRIGGERED).setDamage(damage);
                        });
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));

    public static final DeferredItem<Item> TRANSMUTATION_RUNE = ITEMS.registerItem("transmutation_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, 0.3,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        // TODO function to be determined
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));

    public static final DeferredItem<Item> DESTRUCTION_RUNE = ITEMS.registerItem("destruction_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 2, 1.5f, 0.3,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        MainRuneItem item = (MainRuneItem) stack.getItem();
                        ExplosionUtil.accurateExplosion(level, destination.getBlockPos(), finalScale * item.getScale(),
                                false, true, true, item.getScale() * finalScale * 1.5f);
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));

    public static final DeferredItem<Item> PORTAL_RUNE = ITEMS.registerItem("portal_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 15, 1, 1.5f, 0.3,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        TeleportUtil.TeleportResult result = TeleportUtil.teleportManager(destination, reference, level, 10, 10, player);
                        return switch (result) {
                            case FAILED_NO_SAFE_LOCATION -> {
                                ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Teleport failed: No safe location found!", level);
                                yield RunicAltarEntity.RitualState.ALMOST;
                            }
                            case FAILED_NO_ENTITY -> {
                                ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Teleport failed: No entity is provided reference rune", level);
                                yield RunicAltarEntity.RitualState.ALMOST;
                            }
                            case SUCCESS -> RunicAltarEntity.RitualState.SUCCESS;
                        };
                    }));
    public static final DeferredItem<Item> PROTECTION_RUNE = ITEMS.registerItem("protection_rune",
            (properties) -> new MainRuneItem(properties.stacksTo(16).fireResistant(), 10, 10, 1.5f, 0.3,
                    (level, player, stack, finalScale, destination, reference, additionalData) -> {
                        MainRuneItem item = (MainRuneItem) stack.getItem();
                        LivingEntity entity = destination.entity();
                        int duration = (int) Math.round(20 * finalScale * item.getScale());
                        if (entity != null) {
                            entity.addEffect(new MobEffectInstance(CustomEffects.IMMUNITY, duration));
                        } else {
                            BlockPos origin = ((RunicAltarEntity.DestinationRuneData) additionalData.get("origin")).getBlockPos();
                            RunicAltarEntity runicAltarEntity = (RunicAltarEntity) level.getBlockEntity(origin);
                            Barrier barrier = new Barrier(
                                    new BlockPos(destination.locationX(), destination.locationY(), destination.locationZ()),
                                    item.getScale(),
                                    runicAltarEntity,
                                    duration
                            );
                            BarrierManager.addBarrier(barrier);
                            runicAltarEntity.addBarrier(barrier);
                        }
                        return RunicAltarEntity.RitualState.SUCCESS;
                    }));
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
