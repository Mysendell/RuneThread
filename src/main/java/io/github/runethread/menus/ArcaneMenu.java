    package io.github.runethread.menus;

    import io.github.runethread.customblocks.craftingtable.ArcaneTableEntity;
    import net.minecraft.core.BlockPos;
    import net.minecraft.network.FriendlyByteBuf;
    import net.minecraft.resources.ResourceLocation;
    import net.minecraft.world.entity.player.Inventory;
    import net.minecraft.world.entity.player.Player;
    import net.minecraft.world.inventory.AbstractContainerMenu;
    import net.minecraft.world.inventory.ContainerLevelAccess;
    import net.minecraft.world.inventory.MenuType;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.block.entity.BlockEntity;
    import net.neoforged.neoforge.items.IItemHandler;
    import net.neoforged.neoforge.items.ItemStackHandler;
    import net.neoforged.neoforge.items.SlotItemHandler;
    import org.jetbrains.annotations.Nullable;

    public class ArcaneMenu extends AbstractContainerMenu {
        private final BlockEntity blockEntity;
        private final IItemHandler inventory;
        private final ContainerLevelAccess access;

        public ArcaneMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
            this(id, playerInv,
                    (ArcaneTableEntity)playerInv.player.level().getBlockEntity(buf.readBlockPos()),
                    ContainerLevelAccess.create(playerInv.player.level(), buf.readBlockPos()));
        }

        public ArcaneMenu(int id, Inventory playerInv, ArcaneTableEntity blockEntity, ContainerLevelAccess access) {
            super(CustomMenus.ARCANE_MENU.get(), id);
            this.blockEntity = blockEntity;
            this.inventory = blockEntity.getInventory();
            this.access = access;
            this.addSlot(new SlotItemHandler(inventory, 0, 30, 17));
            this.addSlot(new SlotItemHandler(inventory, 1, 124, 35));
        }

        @Override
        public ItemStack quickMoveStack(Player player, int index) {
            return null;
        }

        @Override
        public boolean stillValid(Player player) {
            return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
        }

        public ResourceLocation getBackgroundTexture() {
            return ResourceLocation.parse("runethread:textures/gui/arcane_table.png");

        }
    }
