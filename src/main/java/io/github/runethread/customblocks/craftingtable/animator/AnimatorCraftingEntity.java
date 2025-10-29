package io.github.runethread.customblocks.craftingtable.animator;

import io.github.runethread.customblocks.craftingtable.PhilosphalFurnaceCraftingEntity;
import io.github.runethread.gui.menus.AnimatorMenu;
import io.github.runethread.recipes.CustomRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static io.github.runethread.customblocks.CustomBlockEntities.ANIMATOR;

public class AnimatorCraftingEntity extends PhilosphalFurnaceCraftingEntity implements MenuProvider {
    public AnimatorCraftingEntity(BlockPos pos, BlockState blockState) {
        super(ANIMATOR.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.animator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AnimatorMenu(containerId, playerInventory, this);
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return CustomRecipes.PHILOSOPHAL.get();
    }

    @Override
    protected Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input) {
        return level.getServer().getRecipeManager()
                .getRecipeFor(CustomRecipes.PHILOSOPHAL.get(), input, level);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AnimatorCraftingEntity entity) {
        entity.serverTick(level);
    }
}
