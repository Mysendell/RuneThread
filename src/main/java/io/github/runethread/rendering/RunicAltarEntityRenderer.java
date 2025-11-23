package io.github.runethread.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.runethread.customblocks.altar.RunicAltar;
import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.CustomItems;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.IndicatorData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class RunicAltarEntityRenderer implements BlockEntityRenderer<RunicAltarEntity> {
    public RunicAltarEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RunicAltarEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        assert blockEntity.getLevel() != null;
        BlockState blockState = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        RunicAltarEntity.RitualState ritualState = null;
        try {
            ritualState = blockState.getValue(RunicAltar.RITUAL_STATE);
        } catch (Exception e) {
            System.err.println("Failed to get ritual state from block state: " + e.getMessage());
            return;
        }

        if(ritualState != RunicAltarEntity.RitualState.IDLE) {
            Item item = CustomItems.RITUAL_INDICATOR.get();
            ItemStack stack = new ItemStack(item);
            IndicatorData indicatorData;
            indicatorData = switch (ritualState) {
                case NEUTRAL -> new IndicatorData("NEUTRAL");
                case SUCCESS -> new IndicatorData("SUCCESS");
                case FAIL -> new IndicatorData("FAIL");
                case ALMOST -> new IndicatorData("ALMOST");
                default -> throw new IllegalStateException("Unexpected value: " + ritualState);
            };
            stack.set(DataComponentRegistry.RITUAL_STATE.get(), indicatorData);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            poseStack.pushPose();
            poseStack.translate(0.5, 1.439, 0.5);
            poseStack.scale(1.0f, 1.0f, 1.0f);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
