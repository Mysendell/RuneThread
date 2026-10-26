package io.github.runethread.customblocks.etcher;

import io.github.runethread.customTags.RuneTags;
import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.customblocks.craftingtable.ICraftingEntity;
import io.github.runethread.customitems.runes.CompositeRuneItem;
import io.github.runethread.datacomponents.CompositeRuneData;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.gui.menus.RuneEtcherMenu;
import io.github.runethread.util.ChatUtils;
import io.github.runethread.util.exceptions.CompositeRuneException;
import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
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
    public void scheduleCraftingUpdate(@NotNull ItemStack stack, boolean taken, Inventory playerInv, IItemHandler itemHandler) {
        this.setChanged();
        if(!taken && itemHandler == center && (center.getStackInSlot(0).getItem() instanceof CompositeRuneItem) && stack.has(DataComponentRegistry.COMPOSITE_RUNE)) { // Just added a composite rune with data
            unformComposite(stack, playerInv);
        }
    }

    private void unformComposite(@NotNull ItemStack stack, Inventory playerInv) {
        CompositeRuneData compositeRuneData = stack.get(DataComponentRegistry.COMPOSITE_RUNE);
        ItemStack itemStack = circle.getStackInSlot(0);
        playerInv.add(circle.extractItem(0, itemStack.getCount(), false));
        circle.insertItem(0, compositeRuneData.mainRune(), false);

        Iterator<ItemStack> modifierItems = compositeRuneData.modifiers().values().iterator();
        for(int i=1; i < circleSize; i++){
            itemStack = circle.getStackInSlot(i);
            playerInv.add(circle.extractItem(i, itemStack.getCount(), false));
            if(modifierItems.hasNext()) {
                circle.insertItem(i, modifierItems.next(), false);
            }
        }
        stack.remove(DataComponentRegistry.COMPOSITE_RUNE);
        stack.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        stack.set(DataComponents.MAX_STACK_SIZE, stack.getItem().getDefaultMaxStackSize());
    }

    @Override
    public void doCraft(@NotNull ItemStack stack, Player player) {
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
        ItemStack runeItem = null;
        for(int i=0; i < circleSize; i++){
            ItemStack itemStack = circle.getStackInSlot(i);
            if(itemStack.is(RuneTags.Items.FUNCTION_RUNE_TAG)){
                if(runeItem != null) throw new CompositeRuneException("Only one function rune allowed");
                runeItem = itemStack;
            }
            else if(!itemStack.isEmpty()){
                modifiers.put(BuiltInRegistries.ITEM.getKey(itemStack.getItem()), itemStack);
            }
        }
        if(runeItem == null) {
            if(!modifiers.isEmpty()) throw new CompositeRuneException("Must have at least one function rune");
            return;
        }

        CompositeRuneData compositeRuneData = new CompositeRuneData(runeItem, modifiers);
        if(compositeRune.getCount() > 1){
            ItemStack newCompositeRune = compositeRune.split(compositeRune.getCount() - 1);
            center.setStackInSlot(0, newCompositeRune);
        }
        compositeRune.set(DataComponentRegistry.COMPOSITE_RUNE, compositeRuneData);
        compositeRune.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        compositeRune.set(DataComponents.MAX_STACK_SIZE, 1);

        for(int i=0; i < circleSize; i++){
            circle.extractItem(i, 1, false);
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CenterRune", Tag.TAG_COMPOUND))
            center.deserializeNBT(registries, tag.getCompound("CenterRune"));
        if (tag.contains("CircleRunes", Tag.TAG_COMPOUND))
            circle.deserializeNBT(registries, tag.getCompound("CircleRunes"));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("CenterRune", center.serializeNBT(registries));
        tag.put("CircleRunes", circle.serializeNBT(registries));

    }
}
