    package io.github.runethread.customblocks.craftingtable.arcanetable;

    import io.github.runethread.customblocks.craftingtable.CraftingEntity;
    import io.github.runethread.gui.menus.ArcaneMenu;
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
    import net.minecraft.world.level.Level;
    import net.minecraft.world.level.block.state.BlockState;
    import org.jetbrains.annotations.Nullable;

    import java.util.Optional;

    import static io.github.runethread.customblocks.CustomBlockEntities.ARCANE_TABLE;

    public class ArcaneTableEntity extends CraftingEntity implements MenuProvider {

        public ArcaneTableEntity(BlockPos pos, BlockState state) {
            super(ARCANE_TABLE.get(), pos, state,3 ,3);
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("container.arcane_table");
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
            return new ArcaneMenu(containerId, playerInventory, this);
        }

        @Override
        protected Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input) {
            Optional<RecipeHolder<CraftingRecipe>> shaped = level.getServer().getRecipeManager()
                    .getRecipeFor(CustomRecipes.RECIPE_SHAPED.get(), input, level);
            if (shaped.isPresent())
                return shaped;

            Optional<RecipeHolder<CraftingRecipe>> shapeless = level.getServer().getRecipeManager()
                    .getRecipeFor(CustomRecipes.RECIPE_SHAPELESS.get(), input, level);
            return shapeless;
        }

        public static void tick(Level level, BlockPos blockPos, BlockState blockState, ArcaneTableEntity entity) {
            entity.tick();
        }
    }