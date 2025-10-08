package io.github.runethread.customblocks.altar;

import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customblocks.CustomBlocks;
import io.github.runethread.customblocks.structure.StructureCenterEntity;
import io.github.runethread.customitems.CustomItems;
import io.github.runethread.customitems.runes.LocationRuneItem;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.datacomponents.LocationData;
import io.github.runethread.datacomponents.PowerData;
import io.github.runethread.gui.menus.RusticAltarMenu;
import io.github.runethread.gui.menus.TempleAltarMenu;
import io.github.runethread.util.*;
import io.github.runethread.util.exceptions.GenericRuneException;
import io.github.runethread.util.exceptions.InsufficientEnergyException;
import io.github.runethread.util.exceptions.MissingRuneException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunicAltarEntity extends StructureCenterEntity implements MenuProvider {
    public enum RitualState implements StringRepresentable {
        IDLE,
        NEUTRAL,
        SUCCESS,
        ALMOST,
        FAIL;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    private final ItemStackHandler mainRune = new ItemStackHandler(1);
    private final ItemStackHandler destinationRune = new ItemStackHandler(2);
    private final ItemStackHandler targetRune = new ItemStackHandler(18);
    private final ItemStackHandler power = new ItemStackHandler(2);
    private double energy = 0;
    private RitualState ritualState = RitualState.IDLE;
    private String playerName;
    private final List<Barrier> barriers;
    private DestinationRuneData lastDest = null;
    private DestinationRuneData lastRef = null;
    private Double lastRange = null;
    private boolean isTesting;

    static {
        structure = new StructureUtil.StructurePart[]{
                new StructureUtil.StructurePart(
                        new BlockPos(-2, -3, -2), new BlockPos(2, -3, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-1, -2, -1), new BlockPos(1, -2, 1),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(0, -1, 0), new BlockPos(0, -1, 0),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureUtil.StructurePart(
                        new BlockPos(3, -3, 3), new BlockPos(2, -3, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-3, -3, -3), new BlockPos(-2, -3, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(3, -3, -3), new BlockPos(2, -3, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-3, -3, 3), new BlockPos(-2, -3, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureUtil.StructurePart(
                        new BlockPos(2, -2, 2), new BlockPos(2, 0, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-2, -2, -2), new BlockPos(-2, 0, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-2, -2, 2), new BlockPos(-2, 0, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(2, -2, -2), new BlockPos(2, 0, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureUtil.StructurePart(
                        new BlockPos(3, -2, 3), new BlockPos(3, 1, 3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(3, -2, -3), new BlockPos(3, 1, -3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-3, -2, -3), new BlockPos(-3, 1, -3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-3, -2, 3), new BlockPos(-3, 1, 3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureUtil.StructurePart(
                        new BlockPos(3, -3, 1), new BlockPos(3, -3, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-3, -3, 1), new BlockPos(-3, -3, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(1, -3, 3), new BlockPos(-1, -3, 3),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(1, -3, -3), new BlockPos(-1, -3, -3),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),

                new StructureUtil.StructurePart(
                        new BlockPos(2, -2, 1), new BlockPos(2, -2, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-2, -2, 1), new BlockPos(-2, -2, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(1, -2, 2), new BlockPos(-1, -2, 2),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(1, -2, -2), new BlockPos(-1, -2, -2),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),

                new StructureUtil.StructurePart(
                        new BlockPos(1, -1, 1), new BlockPos(1, -1, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(-1, -1, 1), new BlockPos(-1, -1, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(0, -1, 1), new BlockPos(0, -1, 1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureUtil.StructurePart(
                        new BlockPos(0, -1, -1), new BlockPos(0, -1, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
        };
        structureSize = 107;
    }

    public RunicAltarEntity(BlockPos pos, BlockState blockState) {
        super(CustomBlockEntities.RUNIC_ALTAR.get(), pos, blockState);
        barriers = new ArrayList<>(BarrierManager.getBarrierFromBlockEntity(this));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.runic_altar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        if (isStructured)
            return new TempleAltarMenu(containerId, playerInventory, this);
        return new RusticAltarMenu(containerId, playerInventory, this);
    }

    public void onScheduledTick() {
        if (level.players().isEmpty())
            return;
        if (ritualState != RitualState.NEUTRAL && ritualState != RitualState.IDLE) {
            update(RitualState.IDLE);
            return;
        }
        BlockState state = level.getBlockState(worldPosition);
        if (!state.getValue(RunicAltar.STRUCTURED))
            performRustic();
        else
            performTemple();
        playerName = null;
    }

    private void performRustic() {
        Map<String, Object> additionalData = new HashMap<>();

        perform(0, additionalData);
        updateRitual(ritualState);
    }

    private void performTemple() {
        Map<String, Object> additionalData = new HashMap<>();

        for (int i = 0; i < targetRune.getSlots(); i++) {
            ItemStack targetRuneStack = targetRune.getStackInSlot(i);
            if (targetRuneStack.isEmpty()) continue;
            additionalData.put(targetRuneStack.getItemName().getString(), targetRuneStack);
        }

        energy = perform(energy, additionalData);
        updateRitual(ritualState);
    }

    private double perform(double energy, Map<String, Object> additionalData) {
        ItemStack mainRuneStack = mainRune.getStackInSlot(0);
        ItemStack destinationRuneStack = destinationRune.getStackInSlot(0);
        ItemStack referenceRuneStack = destinationRune.getStackInSlot(1);

        energy += addEnergy(power);
        double maxScale;
        double actualScale = 0;
        double energyCost = 0;
        double usableEnergy = energy;
        ServerPlayer player = level.getServer().getPlayerList().getPlayerByName(playerName);

        try {
            if (mainRuneStack.isEmpty())
                throw new MissingRuneException("Ritual failed: Main rune is missing!");

            MainRuneItem mainRuneItem = (MainRuneItem) mainRuneStack.getItem();
            double range;

            DestinationRuneData destination;

            DestinationRuneData reference = null;
            if (!referenceRuneStack.isEmpty())
                reference = getDestinationRuneDataFromItem(referenceRuneStack);

            if (mainRuneItem.equals(CustomItems.PORTAL_RUNE.get()) && additionalData.containsKey("Reverse Rune")) {
                if (lastDest == null || lastRef == null)
                    throw new GenericRuneException("Ritual failed: No previous teleport to reverse!");
                destination = lastRef;
                reference = new DestinationRuneData(lastRef.entity.getOnPos(), lastRef.entity);
                range = lastRange;
            } else {
                if (destinationRuneStack.isEmpty())
                    throw new MissingRuneException("Ritual failed: Destination rune is missing!");
                LocationRuneItem destinationRuneItem = (LocationRuneItem) destinationRuneStack.getItem();
                range = destinationRuneItem.getMaxRange();
                destination = getDestinationRuneDataFromItem(destinationRuneStack);
            }

            double distance = getDistance(reference, destination);

            double scalingCost = mainRuneItem.getScalingCost();
            int cost = mainRuneItem.getCost();
            if(additionalData.containsKey("Scale Rune: POWER")) {
                ItemStack scaleRuneStack = (ItemStack) additionalData.get("Scale Rune: POWER");
                double scale = scaleRuneStack.get(DataComponentRegistry.SCALE_DATA).scale();
                if(scale >= 1)
                    usableEnergy = scale;
                else if (scale > 0)
                    usableEnergy *= scale;
            }

            maxScale = getScale(usableEnergy, cost, scalingCost);
            if (maxScale <= 0) throw new InsufficientEnergyException("Ritual failed: Not enough energy!");
            if(additionalData.containsKey("Scale Rune: SCALE")) {
                ItemStack scaleRuneStack = (ItemStack) additionalData.get("Scale Rune: SCALE");
                double scale = scaleRuneStack.get(DataComponentRegistry.SCALE_DATA).scale();
                if (scale >= 1)
                    actualScale = Math.min(maxScale, scale);
                else if (scale > 0)
                    actualScale = maxScale * scale;
            } else
                actualScale = maxScale;


            energyCost = cost * Math.pow(scalingCost, actualScale - 1);
            if (usableEnergy < energyCost)
                throw new InsufficientEnergyException("Ritual failed: Not enough energy!");

            if(mainRuneItem.equals(CustomItems.PORTAL_RUNE.get()))
                range *= actualScale;
            if (distance > range)
                throw new GenericRuneException("Ritual failed: Target is out of range!");

            update(RitualState.SUCCESS);

            if (isTesting)
                return energy;

            additionalData.put("origin", new DestinationRuneData(worldPosition, null));

            update(mainRuneItem.getRuneFunction().perform(
                    (ServerLevel) level,
                    player,
                    mainRuneStack,
                    actualScale,
                    destination,
                    reference,
                    additionalData
            ));
            lastDest = destination;
            lastRef = reference;
            lastRange = range;

            if (Math.random() < mainRuneItem.getBreakChance())
                mainRuneStack.shrink(1);
            destinationRuneStack.shrink(1);
            referenceRuneStack.shrink(1);

            if (ritualState == RitualState.SUCCESS)
                energy -= energyCost;

            return energy;
        } catch (GenericRuneException e) {
            ChatUtils.sendErrorMessagePlayer(playerName, e.getMessage(), level);
            update(RitualState.FAIL);
            return energy;
        } finally {
            if (isTesting) {
                if (ritualState == RitualState.FAIL)
                    ChatUtils.sendInfoMessagePlayer(playerName, "Ritual invalid!", level);
                else
                    ChatUtils.sendInfoMessagePlayer(playerName, "Ritual valid!", level);
                ChatUtils.sendErrorMessagePlayer(playerName, "!Values are rounded!", level);
                ChatUtils.sendInfoMessagePlayer(playerName, "Scale: " + Math.round(actualScale), level);
                ChatUtils.sendInfoMessagePlayer(playerName, "Energy stored: " + Math.round(energy), level);
                ChatUtils.sendInfoMessagePlayer(playerName, "Energy being used: " + Math.round(usableEnergy), level);
                ChatUtils.sendInfoMessagePlayer(playerName, "Energy cost: " + Math.round(energyCost), level);
                isTesting = false;
            }
        }
    }

    private double getDistance(DestinationRuneData reference, DestinationRuneData destination) {
        if (destination == null)
            throw new MissingRuneException("Ritual failed: Destination rune is not set!");

        DestinationRuneData rangeReference = reference;
        if (reference == null)
            rangeReference = new DestinationRuneData(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), null);
        return Math.sqrt(
                Math.pow(rangeReference.locationX() - destination.locationX(), 2) +
                        Math.pow(rangeReference.locationY() - destination.locationY(), 2) +
                        Math.pow(rangeReference.locationZ() - destination.locationZ(), 2)
        );
    }

    private @Nullable DestinationRuneData getDestinationRuneDataFromItem(ItemStack destinationRuneStack) {
        int locationX;
        int locationY;
        int locationZ;
        LivingEntity entity = null;

        LocationData locationData = destinationRuneStack.get(DataComponentRegistry.LOCATION_DATA.get());
        EntityData entityData = destinationRuneStack.get(DataComponentRegistry.ENTITY_DATA.get());

        if (locationData != null) {
            locationX = locationData.posX();
            locationY = locationData.posY();
            locationZ = locationData.posZ();
        } else if (entityData != null) {
            entity = (LivingEntity) level.getEntity(entityData.UUID());
            if (entity == null) {
                ChatUtils.sendErrorMessagePlayer(playerName, "Ritual failed: Target entity not found!", level);
                updateRitual(RitualState.FAIL);
                return null;
            }
            locationX = entity.blockPosition().getX();
            locationY = entity.blockPosition().getY();
            locationZ = entity.blockPosition().getZ();
        } else
            return null;

        return new DestinationRuneData(locationX, locationY, locationZ, entity);
    }

    private @Nullable DestinationRuneData getDestinationRuneDataFromEntity(LivingEntity entity) {
        int locationX;
        int locationY;
        int locationZ;
        if (entity != null) {
            locationX = entity.blockPosition().getX();
            locationY = entity.blockPosition().getY();
            locationZ = entity.blockPosition().getZ();
        } else {
            ChatUtils.sendErrorMessagePlayer(playerName, "Ritual failed: Target entity not found!", level);
            updateRitual(RitualState.FAIL);
            return null;
        }

        return new DestinationRuneData(locationX, locationY, locationZ, entity);
    }

    public record DestinationRuneData(int locationX, int locationY, int locationZ, LivingEntity entity) {
        public BlockPos getBlockPos() {
            return new BlockPos(locationX, locationY, locationZ);
        }
        public DestinationRuneData(BlockPos pos, LivingEntity entity) {
            this(pos.getX(), pos.getY(), pos.getZ(), entity);
        }
    }

    private static int addEnergy(ItemStackHandler power) {
        int energy = 0;
        for (int i = 0; i < power.getSlots(); i++) {
            ItemStack powerStack = power.getStackInSlot(i);
            energy += addEnergy(powerStack);
        }
        return energy;
    }

    private static int addEnergy(ItemStack powerStack) {
        if (powerStack.isEmpty()) return 0;
        int energyPer = powerStack.getOrDefault(DataComponentRegistry.POWER_DATA.get(), new PowerData(1)).getRealPower();
        int amount = powerStack.getCount();
        powerStack.shrink(amount);
        return amount * energyPer;
    }

    private double getScale(double energy, double cost, double scalingCost) {
        return Math.floor(1 + (Math.log(energy / cost) / Math.log(scalingCost)));
    }

    public void startRitual(String playerName) {
        if (ritualState == RitualState.NEUTRAL) {
            return;
        }
        this.playerName = playerName;
        updateRitual(RitualState.NEUTRAL);
    }

    public void testRitual(String playerName) {
        this.playerName = playerName;
        isTesting = true;
        updateRitual(RitualState.IDLE);
    }

    public void removeBarriers() {
        for (Barrier barrier : barriers) {
            BarrierManager.removeBarrier(barrier);
        }
    }

    public void serverTick() {
        for (Barrier barrier : barriers) {
            if (barrier.getTicks() <= 0) {
                BarrierManager.removeBarrier(barrier);
                continue;
            }
            barrier.countdown();
        }
    }

    protected void updateSelfState(boolean value) {
        if (isStructured != value)
            dropItems();
        isStructured = value;
        BlockState state = level.getBlockState(worldPosition);
        level.setBlock(worldPosition, state.setValue(RunicAltar.STRUCTURED, value), 2);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RunicAltarEntity entity) {
        entity.serverTick();
    }

    private void update(RitualState ritualState) {
        this.ritualState = ritualState;
        updateClient();
    }

    private void updateRitual(RitualState ritualState) {
        level.scheduleTick(worldPosition, getBlockState().getBlock(), 20);
        update(ritualState);
    }

    private void updateClient() {
        setChanged();
        assert level != null;
        try {
            BlockState oldState = level.getBlockState(worldPosition);
            BlockState newState = oldState.setValue(RunicAltar.RITUAL_STATE, ritualState);
            level.setBlock(worldPosition, newState, 3);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void onRemove() {
        dropItems();
        removeBarriers();
        updateStructureState(false);
    }

    public void dropItems() {
        InventoryUtil.dropStackHandler(worldPosition, level, power);
        InventoryUtil.dropStackHandler(worldPosition, level, destinationRune);
        InventoryUtil.dropStackHandler(worldPosition, level, targetRune);
        InventoryUtil.dropStackHandler(worldPosition, level, mainRune);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ritualState = RitualState.valueOf(tag.getString("RitualState"));
        energy = tag.getDouble("Energy");
        if (tag.contains("MainRune", Tag.TAG_COMPOUND))
            mainRune.deserializeNBT(registries, tag.getCompound("MainRune"));
        if (tag.contains("DestinationRune", Tag.TAG_COMPOUND))
            destinationRune.deserializeNBT(registries, tag.getCompound("DestinationRune"));
        if (tag.contains("TargetRune", Tag.TAG_COMPOUND))
            targetRune.deserializeNBT(registries, tag.getCompound("TargetRune"));
        if (tag.contains("PowerData", Tag.TAG_COMPOUND))
            power.deserializeNBT(registries, tag.getCompound("PowerData"));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("RitualState", RitualState.IDLE.name());
        tag.putDouble("Energy", energy);
        tag.put("MainRune", mainRune.serializeNBT(registries));
        tag.put("DestinationRune", destinationRune.serializeNBT(registries));
        tag.put("TargetRune", targetRune.serializeNBT(registries));
        tag.put("PowerData", power.serializeNBT(registries));

    }

    public ItemStackHandler getPower() {
        return power;
    }

    public ItemStackHandler getDestinationRune() {
        return destinationRune;
    }

    public ItemStackHandler getMainRune() {
        return mainRune;
    }

    public ItemStackHandler getTargetRune() {
        return targetRune;
    }

    public void addBarrier(Barrier barrier) {
        barriers.add(barrier);
    }
}
