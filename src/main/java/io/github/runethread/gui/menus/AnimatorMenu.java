package io.github.runethread.gui.menus;

import io.github.runethread.customblocks.craftingtable.animator.AnimatorEntity;
import io.github.runethread.gui.Menu;
import io.github.runethread.recipes.FurnaceOutputSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import static io.github.runethread.gui.CustomMenus.ANIMATOR_MENU;

public class AnimatorMenu extends Menu<AnimatorEntity> {
    private final IItemHandler input;
    private final IItemHandler output;
    private final IItemHandler fuel;
    private final IItemHandler lifeEnergy;

    private int burnTime;
    private int fuelBurnTime;
    private int recipeBurnTime;

    public AnimatorMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public AnimatorMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, ANIMATOR_MENU.get());

        this.input = ((AnimatorEntity) blockEntity).getInput();
        this.output = ((AnimatorEntity) blockEntity).getOutput();
        this.fuel = ((AnimatorEntity) blockEntity).getFuel();
        this.lifeEnergy = ((AnimatorEntity) blockEntity).getLifeEnergy();

        TE_INVENTORY_SLOT_COUNT = 4;

        addPlayerInventory(playerInv, 8, 84, 18, 18);
        addPlayerHotbar(playerInv, 8, 142, 18);
        addAnimatorTableSlots();

        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ((AnimatorEntity) blockEntity).getBurnTime();
            }

            @Override
            public void set(int value) {
                burnTime = value;
            }
        });
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ((AnimatorEntity) blockEntity).getFuelBurnTime();
            }

            @Override
            public void set(int value) {
                fuelBurnTime = value;
            }
        });
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ((AnimatorEntity) blockEntity).getMaxBurnTime();
            }

            @Override
            public void set(int value) {
                recipeBurnTime = value;
            }
        });
    }

    private void addAnimatorTableSlots() {
        this.addSlot(new FurnaceOutputSlot(blockEntity, player, level, 0, 98, 39));
        this.addSlot(new SlotItemHandler(input, 0, 17, 21));
        this.addSlot(new SlotItemHandler(fuel, 0, 17, 57));
        this.addSlot(new SlotItemHandler(lifeEnergy, 0, 44, 21));
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.parse("runethread:textures/gui/animator.png");
    }

    public int getBurnTime() {
        return burnTime;
    }
    public int getFuelBurnTime() {
        return fuelBurnTime;
    }
    public int getRecipeBurnTime() {
        return recipeBurnTime;
    }
}
