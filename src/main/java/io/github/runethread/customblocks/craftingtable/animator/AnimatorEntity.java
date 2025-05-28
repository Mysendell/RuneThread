package io.github.runethread.customblocks.craftingtable.animator;

import io.github.runethread.customblocks.craftingtable.PhilosphalFurnaceEntity;
import io.github.runethread.gui.menus.AnimatorMenu;
import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.smelting.animator.AnimatorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static io.github.runethread.customblocks.CustomBlockEntities.ANIMATOR;
import static io.github.runethread.recipes.CustomRecipes.*;

public class AnimatorEntity extends PhilosphalFurnaceEntity<Animator, AnimatorRecipe> {
    public AnimatorEntity(BlockPos pos, BlockState blockState) {
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
        return CustomRecipes.ANIMATOR_RECIPE.get();
    }

    @Override
    protected Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input) {
        return level.getServer().getRecipeManager()
                .getRecipeFor(CustomRecipes.ANIMATOR_RECIPE.get(), input, level);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AnimatorEntity entity) {
        entity.serverTick(level);
    }
}
