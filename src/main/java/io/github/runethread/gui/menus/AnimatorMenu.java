package io.github.runethread.gui.menus;

import io.github.runethread.customblocks.craftingtable.animator.AnimatorCraftingEntity;
import io.github.runethread.gui.AbstractMenu;
import io.github.runethread.gui.slots.CraftingSlot;
import io.github.runethread.gui.slots.OutputSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import static io.github.runethread.gui.CustomMenus.ANIMATOR_MENU;

public class AnimatorMenu extends AbstractMenu<AnimatorCraftingEntity> {
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

        this.input = ((AnimatorCraftingEntity) blockEntity).getInput();
        this.output = ((AnimatorCraftingEntity) blockEntity).getOutput();
        this.fuel = ((AnimatorCraftingEntity) blockEntity).getFuel();
        this.lifeEnergy = ((AnimatorCraftingEntity) blockEntity).getLifeEnergy();

        TE_INVENTORY_SLOT_COUNT = 4;

        addPlayerInventory(playerInv, 8, 84, 18, 18);
        addPlayerHotbar(playerInv, 8, 142, 18);
        addAnimatorTableSlots();

        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ((AnimatorCraftingEntity) blockEntity).getBurnTime();
            }

            @Override
            public void set(int value) {
                burnTime = value;
            }
        });
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ((AnimatorCraftingEntity) blockEntity).getFuelBurnTime();
            }

            @Override
            public void set(int value) {
                fuelBurnTime = value;
            }
        });
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ((AnimatorCraftingEntity) blockEntity).getMaxBurnTime();
            }

            @Override
            public void set(int value) {
                recipeBurnTime = value;
            }
        });
    }

    private void addAnimatorTableSlots() {
        this.addSlot(new OutputSlot(output, 0, 98, 39, blockEntity));
        this.addSlot(new CraftingSlot(input, 0, 17, 21, blockEntity));
        this.addSlot(new SlotItemHandler(fuel, 0, 17, 57));
        this.addSlot(new CraftingSlot(lifeEnergy, 0, 44, 21, blockEntity));
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
