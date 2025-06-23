package io.github.runethread.customblocks.craftingtable.altar;

import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customitems.CustomItems;
import io.github.runethread.customitems.runes.LocationRuneItem;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.datacomponents.LocationData;
import io.github.runethread.gui.menus.RusticAltarMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class RunicAltarEntity extends BlockEntity implements MenuProvider {
    public enum RitualState implements StringRepresentable {
        IDLE,
        NEUTRAL,
        SUCCESS,
        FAIL;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    private final ItemStackHandler mainRune = new ItemStackHandler(1);
    private ItemStackHandler destinationRune = new ItemStackHandler(1);
    private ItemStackHandler targetRune = new ItemStackHandler(0);
    private ItemStackHandler power = new ItemStackHandler(1);
    private int energy = 0;
    private RitualState ritualState = RitualState.IDLE;
    private String playerName;

    public RunicAltarEntity(BlockPos pos, BlockState blockState) {
        super(CustomBlockEntities.RUNIC_ALTAR.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.runic_altar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RusticAltarMenu(containerId, playerInventory, this);
    }

    public void onScheduledTick() {
        if(level.players().isEmpty())
            return;
        if(ritualState != RitualState.NEUTRAL) {
            update(RitualState.IDLE);
            return;
        }

        ItemStack mainRuneStack = mainRune.getStackInSlot(0);
        ItemStack powerStack = power.getStackInSlot(0);
        ItemStack destinationRuneStack = destinationRune.getStackInSlot(0);

        if(!powerStack.isEmpty()) {
            int energyPer = new int[]{10, 50, 100, 200, 500}[powerStack.get(DataComponentRegistry.POWER_DATA.get()).power() - 1];
            int amount = powerStack.getCount();
            energy += amount * energyPer;
            powerStack.shrink(amount);
        }

        if (mainRuneStack.isEmpty() || destinationRuneStack.isEmpty()) {
            sendErrorMessage(playerName, "Ritual failed: Main rune or destination rune is missing!");
            updateRitual(RitualState.FAIL);
            return;
        }

        LocationRuneItem destinationRuneItem = (LocationRuneItem) destinationRuneStack.getItem();
        MainRuneItem mainRuneItem = (MainRuneItem) mainRuneStack.getItem();
        int cost = mainRuneItem.getCost();

        if (energy < cost) {
            sendErrorMessage(playerName, "Ritual failed: Not enough energy!");
            updateRitual(RitualState.FAIL);
            return;
        }

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
                sendErrorMessage(playerName, "Ritual failed: Target entity not found!");
                updateRitual(RitualState.FAIL);
                return;
            }
            locationX = entity.blockPosition().getX();
            locationY = entity.blockPosition().getY();
            locationZ = entity.blockPosition().getZ();
        }
        else{
            sendErrorMessage(playerName, "Ritual failed: Destination rune is not set!");
            updateRitual(RitualState.FAIL);
            return;
        }

        int range = destinationRuneItem.getMaxRange();
        double distance = Math.sqrt(
                Math.pow(worldPosition.getX() - locationX, 2) +
                Math.pow(worldPosition.getY() - locationY, 2) +
                Math.pow(worldPosition.getZ() - locationZ, 2)
        );

        float scalingCost = mainRuneItem.getScalingCost();
        int maxScale;

        if(mainRuneItem.equals(CustomItems.PORTAL_RUNE.get())){
            maxScale = (int) Math.floor(distance / range);
            maxScale = Math.max(maxScale, 1);
        }
        else if (distance > range) {
            sendErrorMessage(playerName, "Ritual failed: Target is out of range!");
            updateRitual(RitualState.FAIL);
            return;
        }
        else {
            maxScale = (int) Math.floor( 1 + (Math.log((double)energy / cost) / Math.log(scalingCost)));
        }

        int energyCost = (int) (cost * Math.pow(scalingCost, maxScale - 1));
        if (energy < energyCost) {
            sendErrorMessage(playerName, "Ritual failed: Not enough energy for scaling!");
            updateRitual(RitualState.FAIL);
            return;
        }
        energy -= energyCost;

        if(Math.random() < 0.4)
            mainRuneStack.shrink(1);
        if(Math.random() < 0.3)
            destinationRuneStack.shrink(1);

        updateRitual(RitualState.SUCCESS);

        Object[] additionalData = null;
        if(mainRuneItem.equals(CustomItems.PROTECTION_RUNE.get()) && entity != null){
            additionalData = new Object[]{entity};
        }

        mainRuneItem.getRuneFunction().perform(
                (ServerLevel) level,
                level.getServer().getPlayerList().getPlayerByName(playerName),
                mainRuneItem,
                maxScale,
                locationX,
                locationY,
                locationZ,
                additionalData
        );
    }

    public void startRitual(String playerName) {
        if (ritualState == RitualState.NEUTRAL) {
            return;
        }
        updateRitual(RitualState.NEUTRAL);
        this.playerName = playerName;
    }

    private void sendErrorMessage(String playerName, String message) {
        sendMessage(playerName, Component.literal(message).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
    }

    private void sendMessage(String playerName, Component messageComponent) {
        ServerPlayer player = level.getServer().getPlayerList().getPlayerByName(playerName);
        if (player != null) {
            player.sendSystemMessage(messageComponent);
        }
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
}
