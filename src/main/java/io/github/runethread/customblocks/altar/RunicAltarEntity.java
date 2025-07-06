package io.github.runethread.customblocks.altar;

import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customblocks.CustomBlocks;
import io.github.runethread.customblocks.StructureCenterEntity;
import io.github.runethread.customitems.CustomItems;
import io.github.runethread.customitems.runes.LocationRuneItem;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.datacomponents.LocationData;
import io.github.runethread.gui.menus.RusticAltarMenu;
import io.github.runethread.gui.menus.TempleAltarMenu;
import io.github.runethread.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
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
    private ItemStackHandler destinationRune = new ItemStackHandler(2);
    private ItemStackHandler targetRune = new ItemStackHandler(18);
    private ItemStackHandler power = new ItemStackHandler(2);
    private int energy = 0;
    private RitualState ritualState = RitualState.IDLE;
    private String playerName;
    private List<Barrier> barriers;
    static{
        structure = new StructureCheckerUtil.StructurePart[]{
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-2, -3, -2), new BlockPos(2, -3, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-1, -2, -1), new BlockPos(1, -2, 1),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(0, -1, 0), new BlockPos(0, -1, 0),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureCheckerUtil.StructurePart(
                        new BlockPos(3, -3, 3), new BlockPos(2, -3, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-3, -3, -3), new BlockPos(-2, -3, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(3, -3, -3), new BlockPos(2, -3, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-3, -3, 3), new BlockPos(-2, -3, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureCheckerUtil.StructurePart(
                        new BlockPos(2, -2, 2), new BlockPos(2, 0, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-2, -2, -2), new BlockPos(-2, 0, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-2, -2, 2), new BlockPos(-2, 0, 2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(2, -2, -2), new BlockPos(2, 0, -2),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureCheckerUtil.StructurePart(
                        new BlockPos(3, -2, 3), new BlockPos(3, 1, 3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(3, -2, -3), new BlockPos(3, 1, -3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-3, -2, -3), new BlockPos(-3, 1, -3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-3, -2, 3), new BlockPos(-3, 1, 3),
                        CustomBlocks.MARBLE_BRICK_BLOCK
                ),

                new StructureCheckerUtil.StructurePart(
                        new BlockPos(3, -3, 1), new BlockPos(3, -3, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-3, -3, 1), new BlockPos(-3, -3, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(1, -3, 3), new BlockPos(-1, -3, 3),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(1, -3, -3), new BlockPos(-1, -3, -3),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),

                new StructureCheckerUtil.StructurePart(
                        new BlockPos(2, -2, 1), new BlockPos(2, -2, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-2, -2, 1), new BlockPos(-2, -2, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(1, -2, 2), new BlockPos(-1, -2, 2),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(1, -2, -2), new BlockPos(-1, -2, -2),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),

                new StructureCheckerUtil.StructurePart(
                        new BlockPos(1, -1, 1), new BlockPos(1, -1, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(-1, -1, 1), new BlockPos(-1, -1, -1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
                        new BlockPos(0, -1, 1), new BlockPos(0, -1, 1),
                        CustomBlocks.MARBLE_BRICK_STAIR_BLOCK
                ),
                new StructureCheckerUtil.StructurePart(
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
        if(isStructured)
            return new TempleAltarMenu(containerId, playerInventory, this);
        return new RusticAltarMenu(containerId, playerInventory, this);
    }

    public void onScheduledTick() {
        if(level.players().isEmpty())
            return;
        if(ritualState != RitualState.NEUTRAL) {
            update(RitualState.IDLE);
            return;
        }
        BlockState state = level.getBlockState(worldPosition);
        if(!state.getValue(RunicAltar.STRUCTURED))
            performRustic();
        else
            performTemple();
    }

    private void performRustic() {
        ItemStack destinationRuneStack = destinationRune.getStackInSlot(0);
        List<Object> additionalData = new ArrayList<>();

        perform(0, destinationRuneStack, additionalData, null);
    }

    private void performTemple() {
        ItemStack destinationRuneStack = destinationRune.getStackInSlot(0);
        ItemStack referenceRuneStack = destinationRune.getStackInSlot(1);
        List<Object> additionalData = new ArrayList<>();

        for(int i = 0; i < targetRune.getSlots(); i++) {
            ItemStack targetRuneStack = targetRune.getStackInSlot(i);
            if (targetRuneStack.isEmpty()) continue;
            additionalData.add(targetRuneStack);
        }
        DestinationRuneData referenceRuneData = null;
        if(!referenceRuneStack.isEmpty())
            referenceRuneData = getDestinationRuneData(referenceRuneStack);

        Integer result = perform(energy, destinationRuneStack, additionalData, referenceRuneData);
        if (result == null) return;
        if (Math.random() < 0.2) {
            referenceRuneStack.shrink(1);
        }
        energy = result;
    }

    private Integer perform(int energy, ItemStack destinationRuneStack, List<Object> additionalData, DestinationRuneData reference) {
        ItemStack mainRuneStack = mainRune.getStackInSlot(0);
        energy = addEnergy(power, energy);

        if (mainRuneStack.isEmpty() || destinationRuneStack.isEmpty()) {
            ChatUtils.sendErrorMessagePlayer(playerName, "Ritual failed: Main rune or destination rune is missing!", level);
            updateRitual(RitualState.FAIL);
            return null;
        }

        LocationRuneItem destinationRuneItem = (LocationRuneItem) destinationRuneStack.getItem();
        MainRuneItem mainRuneItem = (MainRuneItem) mainRuneStack.getItem();

        DestinationRuneData destinationRuneData = getDestinationRuneData(destinationRuneStack);
        if (destinationRuneData == null) {
            ChatUtils.sendErrorMessagePlayer(playerName, "Ritual failed: Destination rune is not set!", level);
            updateRitual(RitualState.FAIL);
            return null;
        }

        int range = destinationRuneItem.getMaxRange();
        double distance = Math.sqrt(
                Math.pow(worldPosition.getX() - destinationRuneData.locationX(), 2) +
                Math.pow(worldPosition.getY() - destinationRuneData.locationY(), 2) +
                Math.pow(worldPosition.getZ() - destinationRuneData.locationZ(), 2)
        );

        float scalingCost = mainRuneItem.getScalingCost();
        int cost = mainRuneItem.getCost();
        Integer maxScale = getScale(mainRuneItem, distance, range, energy, cost, scalingCost);
        if (maxScale == null) return null;
        int energyCost = (int) (cost * Math.pow(scalingCost, maxScale - 1));
        if (energy < energyCost) {
            ChatUtils.sendErrorMessagePlayer(playerName, "Ritual failed: Not enough energy!", level);
            updateRitual(RitualState.FAIL);
            return null;
        }

        update(RitualState.SUCCESS);

        if(mainRuneItem.equals(CustomItems.PROTECTION_RUNE.get()))
            additionalData.addFirst(this);

        updateRitual(mainRuneItem.getRuneFunction().perform(
                (ServerLevel) level,
                level.getServer().getPlayerList().getPlayerByName(playerName),
                mainRuneItem,
                maxScale,
                destinationRuneData,
                reference,
                additionalData.toArray()
        ));

        if(Math.random() < 0.3)
            mainRuneStack.shrink(1);
        if(Math.random() < 0.2)
            destinationRuneStack.shrink(1);

        if(ritualState == RitualState.SUCCESS)
            energy -= energyCost;

        return energy;
    }

    private @Nullable DestinationRuneData getDestinationRuneData(ItemStack destinationRuneStack) {
        int locationX;
        int locationY;
        int locationZ;
        Entity entity = null;

        LocationData locationData = destinationRuneStack.get(DataComponentRegistry.LOCATION_DATA.get());
        EntityData entityData = destinationRuneStack.get(DataComponentRegistry.ENTITY_DATA.get());

        if (locationData != null) {
            locationX = locationData.posX();
            locationY = locationData.posY();
            locationZ = locationData.posZ();
        } else if (entityData != null) {
            entity = level.getEntity(entityData.UUID());
            if (entity == null) {
                ChatUtils.sendErrorMessagePlayer(playerName, "Ritual failed: Target entity not found!", level);
                updateRitual(RitualState.FAIL);
                return null;
            }
            locationX = entity.blockPosition().getX();
            locationY = entity.blockPosition().getY();
            locationZ = entity.blockPosition().getZ();
        }
        else
            return null;

        return new DestinationRuneData(locationX, locationY, locationZ, entity);
    }

    public record DestinationRuneData(int locationX, int locationY, int locationZ, Entity entity) {
        public Object[] toArray() {
            return new Object[]{locationX, locationY, locationZ, entity};
        }
    }

    private static int addEnergy(ItemStackHandler power, int energy) {
            for(int i=0; i < power.getSlots(); i++) {
                ItemStack powerStack = power.getStackInSlot(i);
                energy += addEnergy(powerStack, energy);
            }
        return energy;
    }

    private static int addEnergy(ItemStack powerStack, int energy) {
        if (powerStack.isEmpty()) return energy;
        int energyPer = new int[]{10, 50, 100, 200, 500}[powerStack.get(DataComponentRegistry.POWER_DATA.get()).power() - 1];
        int amount = powerStack.getCount();
        energy += amount * energyPer;
        powerStack.shrink(amount);
        return energy;
    }

    private @Nullable Integer getScale(MainRuneItem mainRuneItem, double distance, int range, int energy, int cost, float scalingCost) {
        int maxScale;

        if(mainRuneItem.equals(CustomItems.PORTAL_RUNE.get())){
            maxScale = (int) Math.floor(distance / range);
            maxScale = Math.max(maxScale, 1);
        }
        else if (distance > range) {
            ChatUtils.sendErrorMessagePlayer(playerName, "Ritual failed: Target is out of range!", level);
            updateRitual(RitualState.FAIL);
            return null;
        }
        else {
            maxScale = (int) Math.floor( 1 + (Math.log((double) energy / cost) / Math.log(scalingCost)));
        }
        return maxScale;
    }

    public void startRitual(String playerName) {
        if (ritualState == RitualState.NEUTRAL) {
            return;
        }
        updateRitual(RitualState.NEUTRAL);
        this.playerName = playerName;
    }

    public void removeBarriers() {
        for(Barrier barrier : barriers) {
            BarrierManager.removeBarrier(barrier);
        }
    }

    public void serverTick(){
        for(Barrier barrier : barriers) {
            if (barrier.getTicks() <= 0) {
                BarrierManager.removeBarrier(barrier);
                continue;
            }
            barrier.countdown();
        }
    }

    protected void updateSelfState(boolean value){
        if(isStructured != value)
            dropItems();
        isStructured = value;
        BlockState state = level.getBlockState(worldPosition);
        level.setBlock(worldPosition, state.setValue(RunicAltar.STRUCTURED, value), 2);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RunicAltarEntity entity){
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

    public void dropItems(){
        InventoryUtil.dropStackHandler(worldPosition, level, power);
        InventoryUtil.dropStackHandler(worldPosition, level, destinationRune);
        InventoryUtil.dropStackHandler(worldPosition, level, targetRune);
        InventoryUtil.dropStackHandler(worldPosition, level, mainRune);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ritualState = RitualState.valueOf(tag.getString("RitualState"));
        energy = tag.getInt("Energy");
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
        tag.putInt("Energy", energy);
        tag.put("MainRune", mainRune.serializeNBT(registries));
        tag.put("DestinationRune", destinationRune.serializeNBT(registries));
        tag.put("TargetRune", targetRune.serializeNBT(registries));
        tag.put("PowerData", power.serializeNBT(registries));

    }

    public void setRitualState(RitualState ritualState) {
        this.ritualState = ritualState;
        setChanged();
    }

    public RitualState getRitualState() {
        return ritualState;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
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
    public List<Barrier> getBarriers() {
        return barriers;
    }
    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
    }
    public void addBarrier(Barrier barrier) {
        barriers.add(barrier);
    }
    public StructureCheckerUtil.StructurePart[] getStructure() {
        return structure;
    }
    public void setStructure(StructureCheckerUtil.StructurePart[] structure) {
        this.structure = structure;
    }
}
