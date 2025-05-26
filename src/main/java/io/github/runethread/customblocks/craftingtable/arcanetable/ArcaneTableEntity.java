package io.github.runethread.customblocks.craftingtable.arcanetable;

import io.github.runethread.customblocks.craftingtable.CraftingEntity;
import io.github.runethread.gui.menus.ArcaneMenu;
import io.github.runethread.recipes.RecipeInput;
import io.github.runethread.recipes.arcanetable.ArcaneRecipeShaped;
import io.github.runethread.recipes.CustomRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static io.github.runethread.customblocks.CustomBlockEntities.ARCANE_TABLE;

public class ArcaneTableEntity extends CraftingEntity<RecipeInput, ArcaneRecipeShaped> {

    public ArcaneTableEntity(BlockPos pos, BlockState state) {
        super(ARCANE_TABLE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.arcane_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ArcaneMenu(containerId, playerInventory, this);
    }

    protected Optional<RecipeHolder<ArcaneRecipeShaped>> getRecipeOpt(Level level, RecipeInput input) {
        return level.getServer().getRecipeManager()
                .getRecipeFor(CustomRecipes.ARCANE_SHAPED.get(), input, level);
    }

    protected RecipeInput getRecipeInput(List<ItemStack> items) {
        return new RecipeInput(items);
    }

}
