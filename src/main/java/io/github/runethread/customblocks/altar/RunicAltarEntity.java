package io.github.runethread.customblocks.altar;

import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customblocks.structure.StructureCenterEntity;
import io.github.runethread.customitems.runes.LocationRuneItem;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import io.github.runethread.datacomponents.ScaleData;
import io.github.runethread.gui.menus.RusticAltarMenu;
import io.github.runethread.gui.menus.TempleAltarMenu;
import io.github.runethread.util.ChatUtils;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.InventoryUtil;
import io.github.runethread.util.exceptions.GenericRuneException;
import io.github.runethread.util.exceptions.InsufficientEnergyException;
import io.github.runethread.util.exceptions.MissingLevelException;
import io.github.runethread.util.exceptions.MissingRuneException;
import io.github.runethread.util.runeutils.Barrier;
import io.github.runethread.util.runeutils.BarrierManager;
import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
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
import java.util.List;

import static io.github.runethread.util.runeutils.ModifierRune.SCALE_RUNE;

public class RunicAltarEntity extends StructureCenterEntity implements MenuProvider {
    public enum RitualState implements StringRepresentable {
        IDLE,
        NEUTRAL,
        SUCCESS,
        ALMOST,
        FAIL;

        @Override
        public @NotNull String getSerializedName() {
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
    private boolean isTesting;

    static{
        structureName = "temple";
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
        if (ritualState != RitualState.NEUTRAL && ritualState != RitualState.IDLE) {
            update(RitualState.IDLE);
            return;
        }
        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        if (!state.getValue(RunicAltar.STRUCTURED))
            performRustic();
        else
            performTemple();
        playerName = null;
    }

    private void performRustic() {
        ModifierMap additionalData = new ModifierMap();

        perform(0, additionalData);
        updateRitual(ritualState);
    }

    private void performTemple() {
        ModifierMap additionalData = new ModifierMap();

        for (int i = 0; i < targetRune.getSlots(); i++) {
            ItemStack targetRuneStack = targetRune.getStackInSlot(i);
            if (targetRuneStack.isEmpty()) continue;
            additionalData.put(BuiltInRegistries.ITEM.getKey(targetRuneStack.getItem()), targetRuneStack);
        }

        energy = perform(energy, additionalData);
        updateRitual(ritualState);
    }

    private double perform(double energy, ModifierMap additionalData) {
        ItemStack mainRuneStack = mainRune.getStackInSlot(0);
        ItemStack destinationRuneStack = destinationRune.getStackInSlot(0);
        ItemStack referenceRuneStack = destinationRune.getStackInSlot(1);

        energy += addEnergy(power);
        double maxScale;
        double actualScale = 0;
        double energyCost = 0;
        double usableEnergy = energy;

        ServerLevel level = (ServerLevel) this.level;
        assert level != null;
        ServerPlayer player = level.getServer().getPlayerList().getPlayerByName(playerName);

        try {
            if (mainRuneStack.isEmpty())
                throw new MissingRuneException("Ritual failed: Main rune is missing!");
            if (destinationRuneStack.isEmpty())
                throw new MissingRuneException("Ritual failed: Destination rune is missing!");

            MainRuneItem mainRuneItem = (MainRuneItem) mainRuneStack.getItem();
            double range;

            ILocation reference = null;
            if (!referenceRuneStack.isEmpty())
                reference = getLocationFromItem(referenceRuneStack);
            else if (mainRuneItem.requiresReference(mainRuneStack))
                throw new MissingRuneException("Ritual failed: Reference rune is missing!");

            LocationRuneItem destinationRuneItem = (LocationRuneItem) destinationRuneStack.getItem();
            range = destinationRuneItem.getMaxRange();
            ILocation destination = getLocationFromItem(destinationRuneStack);

            double distance = getDistance(reference, destination);

            double scalingCost = mainRuneItem.getScalingCost(mainRuneStack);
            int cost = mainRuneItem.getCost(mainRuneStack);
            /*if (additionalData.containsKey("Scale Rune: POWER")) { //TODO reimplement this in the power section
                ItemStack scaleRuneStack = additionalData.get("Scale Rune: POWER");
                double scale = scaleRuneStack.getOrDefault(DataComponentRegistry.SCALE_DATA, new ScaleData(0, ScaleData.Mode.SCALE)).scale();
                if (scale >= 1)
                    usableEnergy = scale;
                else if (scale > 0)
                    usableEnergy *= scale;
            }*/

            maxScale = getScale(usableEnergy, cost, scalingCost);
            if (maxScale <= 0) throw new InsufficientEnergyException("Ritual failed: Not enough energy!");
            if (additionalData.hasModifier(SCALE_RUNE)) {
                ItemStack scaleRuneStack = additionalData.get(SCALE_RUNE.getName());
                double scale = scaleRuneStack.getOrDefault(DataComponentRegistry.SCALE_DATA, new ScaleData(0, ScaleData.Mode.SCALE)).scale();
                if (scale >= 1)
                    actualScale = Math.min(maxScale, scale);
                else if (scale > 0)
                    actualScale = maxScale * scale;
            } else
                actualScale = maxScale;


            energyCost = cost * Math.pow(scalingCost, actualScale - 1);
            if (usableEnergy < energyCost)
                throw new InsufficientEnergyException("Ritual failed: Not enough energy!");

            if (!mainRuneItem.insideRange(actualScale, range, distance))
                throw new GenericRuneException("Ritual failed: Target is out of range!");

            update(RitualState.SUCCESS);

            if (isTesting)
                return energy;

            update(mainRuneItem.perform(
                    level,
                    player,
                    mainRuneStack,
                    actualScale,
                    destination,
                    reference,
                    worldPosition,
                    additionalData
            ));

            if (Math.random() < mainRuneItem.getBreakChance(mainRuneStack))
                mainRuneStack.shrink(1);
            destinationRuneStack.shrink(1);
            referenceRuneStack.shrink(1);

            if (ritualState == RitualState.SUCCESS)
                energy -= energyCost;

            return energy;
        } catch (GenericRuneException | MissingLevelException e) {
            ChatUtils.sendErrorMessagePlayer(playerName, e.getMessage(), level);
            update(RitualState.FAIL);
            return energy;
        }
        finally {
            if (isTesting) {
                if (ritualState == RitualState.FAIL)
                    ChatUtils.sendInfoMessagePlayer(playerName, "Ritual invalid!", level);
                else
                    ChatUtils.sendInfoMessagePlayer(playerName, "Ritual valid!", level);
                ChatUtils.sendErrorMessagePlayer(playerName, "!Values are rounded!", level); // TODO unround 'em
                ChatUtils.sendInfoMessagePlayer(playerName, "Scale: " + Math.round(actualScale), level);
                ChatUtils.sendInfoMessagePlayer(playerName, "Energy stored: " + Math.round(energy), level);
                ChatUtils.sendInfoMessagePlayer(playerName, "Energy being used: " + Math.round(usableEnergy), level);
                ChatUtils.sendInfoMessagePlayer(playerName, "Energy cost: " + Math.round(energyCost), level);
                isTesting = false;
            }
        }
    }

    private double getDistance(ILocation reference, ILocation destination) {
        if (destination == null)
            throw new MissingRuneException("Ritual failed: Destination rune is not set!");

        try {
            BlockPos destPos = destination.getLocation((ServerLevel) level);
            BlockPos rangePos;
            if (reference != null)
                rangePos = reference.getLocation((ServerLevel) level);
            else
                rangePos = worldPosition;

            return Math.sqrt(
                    Math.pow(rangePos.getX() - destPos.getX(), 2) +
                            Math.pow(rangePos.getY() - destPos.getY(), 2) +
                            Math.pow(rangePos.getZ() - destPos.getZ(), 2)
            );
        } catch (Exception e) {
            throw new GenericRuneException("Ritual failed: Could not find reference!");
        }
    }

    private @Nullable ILocation getLocationFromItem(@NotNull ItemStack destinationRuneStack) {

        ILocation locationData = destinationRuneStack.get(DataComponentRegistry.LOCATION_DATA.get());
        if (locationData == null)
            locationData = destinationRuneStack.get(DataComponentRegistry.ENTITY_DATA.get());

        return locationData;
    }

    private static double addEnergy(@NotNull ItemStackHandler power) {
        double energy = 0;
        for (int i = 0; i < power.getSlots(); i++) {
            ItemStack powerStack = power.getStackInSlot(i);
            energy += addEnergy(powerStack);
        }
        return energy;
    }

    private static double addEnergy(@NotNull ItemStack powerStack) {
        if (powerStack.isEmpty()) return 0;
        double energyPer = powerStack.getOrDefault(DataComponentRegistry.POWER_DATA.get(), new PowerData(1)).getRealPower();
        int amount = powerStack.getCount();
        powerStack.shrink(amount);
        return amount * energyPer;
    }

    private double getScale(double energy, double cost, double scalingCost) {
        return Math.floor(1 + (Math.log(energy / cost) / Math.log(scalingCost)));
    }

    public void startRitual(String playerName) {
        if (ritualState != RitualState.IDLE)
            return;

        this.playerName = playerName;
        updateRitual(RitualState.NEUTRAL);
    }

    public void testRitual(String playerName) {
        this.playerName = playerName;
        isTesting = true;
        updateRitual(RitualState.IDLE);
    }

    public void serverTick() {
        for (Barrier barrier : barriers) {
            barrier.countdown();
        }
    }

    protected void updateSelfState(boolean value) {
        try {
            if (isStructured != value)
                dropItems();
            isStructured = value;
            assert level != null;
            BlockState state = level.getBlockState(worldPosition);
            level.setBlock(worldPosition, state.setValue(RunicAltar.STRUCTURED, value), 2);
        } catch (Exception e) {
            System.err.println("Error updating self state: " + e.getMessage());
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, @NotNull RunicAltarEntity entity) {
        entity.serverTick();
    }

    private void update(RitualState ritualState) {
        this.ritualState = ritualState;
        updateClient();
    }

    private void updateRitual(RitualState ritualState) {
        assert level != null;
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
            e.printStackTrace();
        }
    }

    public void onRemove() {
        dropItems();
        BarrierManager.removeBarriers(barriers);
        isValidStructure();
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
