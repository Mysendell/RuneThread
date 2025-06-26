package io.github.runethread.customblocks.craftingtable.altar;

import com.mojang.serialization.MapCodec;
import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customitems.CustomItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RunicAltar extends BaseEntityBlock {
    private static final MapCodec<RunicAltar> CODEC = simpleCodec(RunicAltar::new);
    public static final BooleanProperty STRUCTURED = BooleanProperty.create("structured");
    public static final EnumProperty<RunicAltarEntity.RitualState> RITUAL_STATE = EnumProperty.create("ritual_state", RunicAltarEntity.RitualState.class);
    private boolean gemInHand = false;
    private String playerName = "";

    public RunicAltar(Properties p_49224_) {
        super(p_49224_);
        this.registerDefaultState(this.stateDefinition.any().setValue(STRUCTURED, false));
        System.out.println(RITUAL_STATE.getPossibleValues());
        this.registerDefaultState(this.stateDefinition.any().setValue(RITUAL_STATE, RunicAltarEntity.RitualState.IDLE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STRUCTURED);
        builder.add(RITUAL_STATE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new RunicAltarEntity(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof RunicAltarEntity runicAltarEntity) {
            if (!level.isClientSide) {
                (player).openMenu(new SimpleMenuProvider(runicAltarEntity, Component.literal("Runic Altar")), pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
        if (hand.name().equals("OFF_HAND"))
            return InteractionResult.PASS;

        InteractionHand mainHand = InteractionHand.MAIN_HAND;
        InteractionHand offHand = InteractionHand.OFF_HAND;
        ItemStack mainHandStack = player.getItemInHand(mainHand);
        ItemStack offHandStack = player.getItemInHand(offHand);

        gemInHand = mainHandStack.is(CustomItems.POWER_GEM.get()) || offHandStack.is(CustomItems.POWER_GEM.get());

        if (!this.gemInHand)
            return InteractionResult.TRY_WITH_EMPTY_HAND;

        playerName = player.getName().getString();

        if (level.getBlockEntity(pos) instanceof RunicAltarEntity runicAltarEntity) {
            if (!level.isClientSide)
                runicAltarEntity.startRitual(playerName);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        RunicAltarEntity runicAltarEntity = (RunicAltarEntity) level.getBlockEntity(pos);
        runicAltarEntity.onScheduledTick();
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(
                type,
                CustomBlockEntities.RUNIC_ALTAR.get(),
                RunicAltarEntity::serverTick
        );
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RunicAltarEntity runicAltarEntity) {
                ItemStack mainRune = runicAltarEntity.getMainRune().getStackInSlot(0);
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), mainRune);

                ItemStackHandler powerRune = runicAltarEntity.getPower();
                for (int i = 0; i < powerRune.getSlots(); i++) {
                    ItemStack stack = powerRune.getStackInSlot(i);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }

                ItemStackHandler destinationRune = runicAltarEntity.getDestinationRune();
                for (int i = 0; i < destinationRune.getSlots(); i++) {
                    ItemStack stack = destinationRune.getStackInSlot(i);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }

                ItemStackHandler targetRune = runicAltarEntity.getTargetRune();
                for (int i = 0; i < targetRune.getSlots(); i++) {
                    ItemStack stack = targetRune.getStackInSlot(i);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }

                runicAltarEntity.removeBarriers();

                level.removeBlockEntity(pos);
                super.onRemove(state, level, pos, newState, isMoving);
            }
        }
    }
}
