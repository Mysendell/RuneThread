package io.github.runethread.datagen;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.CustomBlocks;
import io.github.runethread.customblocks.craftingtable.animator.Animator;
import io.github.runethread.customitems.CustomItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, RuneThread.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        /* ITEMS */
        itemModels.generateFlatItem(CustomItems.HAMPTER_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.DOUGH_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.CAKE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.CAKE_GOLEM_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);

        /* BLOCKS */

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(CustomBlocks.ANIMATOR_BLOCK.get())
                        .with(
                                PropertyDispatch.properties(Animator.FACING, Animator.LIT)
                                        .generate((facing, lit) -> {
                                            String litSuffix = lit ? "_lit" : "";
                                            // This will use block/animator.json and block/animator_lit.json as models
                                            ResourceLocation model = ResourceLocation.fromNamespaceAndPath("runethread", "block/animator" + litSuffix);
                                            Variant variant = Variant.variant().with(VariantProperties.MODEL, model);
                                            // Add rotation based on facing
                                            switch (facing) {
                                                case NORTH -> { /* no rotation needed */ }
                                                case SOUTH -> variant = variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
                                                case WEST  -> variant = variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
                                                case EAST  -> variant = variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                                            }
                                            return variant;
                                        })
                        )
        );
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(CustomBlocks.ARCANE_TABLE_BLOCK.get())
                        .with(
                                PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                                        .generate(facing -> {
                                            int y = switch (facing) {
                                                case NORTH -> 0;
                                                case EAST -> 90;
                                                case SOUTH -> 180;
                                                case WEST -> 270;
                                                default -> 0;
                                            };
                                            return Variant.variant()
                                                    .with(VariantProperties.MODEL, ResourceLocation.fromNamespaceAndPath("runethread", "block/arcane_table"))
                                                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[y / 90]);
                                        })
                        )
        );

    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return CustomBlocks.BLOCKS.getEntries().stream()
                .filter(x -> x.get() instanceof Block)
                .map(x -> Holder.direct(x.get()));
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return CustomItems.ITEMS.getEntries().stream().filter(x -> x.get()  != null);
    }
}
