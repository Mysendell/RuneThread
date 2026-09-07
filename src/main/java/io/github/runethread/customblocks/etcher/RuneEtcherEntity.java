package io.github.runethread.customblocks.etcher;

import io.github.runethread.customTags.RuneTags;
import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customblocks.craftingtable.ICraftingEntity;
import io.github.runethread.customitems.runes.CompositeRuneItem;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.CompositeRuneData;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.gui.menus.RuneEtcherMenu;
import io.github.runethread.util.ChatUtils;
import io.github.runethread.util.exceptions.CompositeRuneException;
import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RuneEtcherEntity extends BlockEntity implements MenuProvider, ICraftingEntity {
    final static int circleSize = 8;
    private final ItemStackHandler center = new ItemStackHandler(1);
    private final ItemStackHandler circle = new ItemStackHandler(circleSize); // Todo not saving

    public RuneEtcherEntity(BlockPos pos, BlockState blockState) {
        super(CustomBlockEntities.RUNE_ETCHER.get(), pos, blockState);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.rune_etcher");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new RuneEtcherMenu(containerId, playerInventory, this);
    }

    protected Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input) {
        return Optional.empty();
    }

    public void onRemove() {
    }

    public ItemStackHandler getCenter() {
        return center;
    }

    public ItemStackHandler getCircle() {
        return circle;
    }

    @Override
    public ItemStackHandler getOutput() {
        return center;
    }

    @Override
    public ItemStackHandler getInput() {
        return circle;
    }

    @Override
    public void scheduleCraftingUpdate() {
        this.setChanged();
    }

    @Override
    public void doCraft(Level level, @NotNull ItemStack stack, Player player) {
        if (level.isClientSide) return;
        ServerLevel serverLevel = (ServerLevel) level;
        try {
            if(stack.getItem() instanceof CompositeRuneItem) {
                formComposite(stack);
            }
        } catch (CompositeRuneException e) {
            ChatUtils.sendErrorMessagePlayer(player.getName().getString(), e.getLocalizedMessage(), serverLevel);
        }
    }

    private void formComposite(ItemStack compositeRune) throws CompositeRuneException{
        ModifierMap modifiers = new ModifierMap();
        MainRuneItem runeItem = null;
        int smallest = compositeRune.getCount();
        for(int i=0; i < circleSize; i++){
            ItemStack itemStack = circle.getStackInSlot(i);
            if(itemStack.is(RuneTags.Items.FUNCTION_RUNE_TAG)){
                if(runeItem != null) throw new CompositeRuneException("Only one function rune allowed");
                runeItem = (MainRuneItem) itemStack.getItem();
            }
            else if(!itemStack.isEmpty()){
                modifiers.put(BuiltInRegistries.ITEM.getKey(itemStack.getItem()), itemStack);
                smallest = Math.min(itemStack.getCount(), smallest);
            }
        }
        if(runeItem == null || !modifiers.isEmpty()) throw new CompositeRuneException("Must have at least one function rune");

        CompositeRuneData compositeRuneData = new CompositeRuneData(runeItem, modifiers);
        /*if(compositeRune.getCount() > 1){
            ItemStack newCompositeRune = compositeRune.split(compositeRune.getCount() - smallest);
            center.setStackInSlot(0, newCompositeRune);
        }*/
        compositeRune.set(DataComponentRegistry.COMPOSITE_RUNE, compositeRuneData);
        compositeRune.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        /*for(int i=0; i < circleSize; i++){
            circle.getStackInSlot(i).shrink(smallest);
        }*/
    }

}
