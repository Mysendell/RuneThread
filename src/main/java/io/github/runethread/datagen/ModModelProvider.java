package io.github.runethread.datagen;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.CustomBlocks;
import io.github.runethread.customblocks.altar.RunicAltar;
import io.github.runethread.customblocks.craftingtable.animator.Animator;
import io.github.runethread.customitems.CustomItems;
import io.github.runethread.datagen.properties.PowerRune;
import io.github.runethread.datagen.properties.RitualIndicator;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    private final PackOutput output;

    public ModModelProvider(PackOutput output) {
        super(output, RuneThread.MODID);
        this.output = output;
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        /* ITEMS */
        itemModels.generateFlatItem(CustomItems.HAMPTER_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.DOUGH_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.CAKE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.CAKE_GOLEM_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(CustomItems.POWER_GEM.get(), ModelTemplates.FLAT_ITEM);

        Optional<ItemModel.Unbaked> fallbackModelPowerRune = Optional.of(ItemModelUtils.plainModel(itemModels.createFlatItemModel(CustomItems.POWER_RUNE.get(), ModelTemplates.FLAT_ITEM)));


        List<RangeSelectItemModel.Entry> entriesPowerRune = List.of(
                new RangeSelectItemModel.Entry(1, ItemModelUtils.plainModel(itemModels.createFlatItemModel(CustomItems.POWER_RUNE.get(), "_1", ModelTemplates.FLAT_ITEM))),
                new RangeSelectItemModel.Entry(2, ItemModelUtils.plainModel(itemModels.createFlatItemModel(CustomItems.POWER_RUNE.get(), "_2", ModelTemplates.FLAT_ITEM))),
                new RangeSelectItemModel.Entry(3, ItemModelUtils.plainModel(itemModels.createFlatItemModel(CustomItems.POWER_RUNE.get(), "_3", ModelTemplates.FLAT_ITEM))),
                new RangeSelectItemModel.Entry(4, ItemModelUtils.plainModel(itemModels.createFlatItemModel(CustomItems.POWER_RUNE.get(), "_4", ModelTemplates.FLAT_ITEM))),
                new RangeSelectItemModel.Entry(5, ItemModelUtils.plainModel(itemModels.createFlatItemModel(CustomItems.POWER_RUNE.get(), "_5", ModelTemplates.FLAT_ITEM)))
        );

        itemModels.itemModelOutput.register(CustomItems.POWER_RUNE.get(),
                new ClientItem(
                        new RangeSelectItemModel.Unbaked(
                                new PowerRune(),
                                1.0f,
                                entriesPowerRune,
                                fallbackModelPowerRune),
                        new ClientItem.Properties(true)));

        ResourceLocation almostModel = ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "item/ritual_indicator_almost");
        ResourceLocation failModel = ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "item/ritual_indicator_fail");
        ResourceLocation successModel = ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "item/ritual_indicator_success");
        ResourceLocation neutralModel = ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "item/ritual_indicator_neutral");

        Optional<ItemModel.Unbaked> neutralItemModelOpt = Optional.of(ItemModelUtils.plainModel(neutralModel));

        List<RangeSelectItemModel.Entry> entriesIndicator = List.of(
                new RangeSelectItemModel.Entry(0, neutralItemModelOpt.get()),
                new RangeSelectItemModel.Entry(1, ItemModelUtils.plainModel(successModel)),
                new RangeSelectItemModel.Entry(2, ItemModelUtils.plainModel(failModel)),
                new RangeSelectItemModel.Entry(3, ItemModelUtils.plainModel(almostModel))
        );

        itemModels.itemModelOutput.register(CustomItems.RITUAL_INDICATOR.get(),
                new ClientItem(
                        new RangeSelectItemModel.Unbaked(
                                new RitualIndicator(),
                                1.0f,
                                entriesIndicator,
                                neutralItemModelOpt
                        ),
                        new ClientItem.Properties(true)
                ));

        itemModels.generateFlatItem(CustomItems.UP_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.DOWN_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.LEFT_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.RIGHT_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.LIVING_RUNE.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(CustomItems.ALPHA_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.BETA_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.LAMBDA_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.TAU_RUNE.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(CustomItems.DESTRUCTION_RUNE.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(CustomItems.PORTAL_RUNE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CustomItems.PROTECTION_RUNE.get(), ModelTemplates.FLAT_ITEM);

        /* BLOCKS */
        blockModels.createTrivialCube(CustomBlocks.MARBLE_BLOCK.get());
        blockModels.createTrivialCube(CustomBlocks.TSAVORITE_ORE_BLOCK.get());
        blockModels.family(CustomBlocks.MARBLE_BRICK_BLOCK.get())
                .stairs(CustomBlocks.MARBLE_BRICK_STAIR_BLOCK.get());

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
                                                case SOUTH ->
                                                        variant = variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
                                                case WEST ->
                                                        variant = variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
                                                case EAST ->
                                                        variant = variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
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
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(CustomBlocks.RUNIC_ALTAR_BLOCK.get())
                        .with(
                                PropertyDispatch.property(RunicAltar.STRUCTURED)
                                        .generate((structured) -> {
                                            ResourceLocation model = ResourceLocation.fromNamespaceAndPath("runethread", "block/runic_altar");
                                            return Variant.variant().with(VariantProperties.MODEL, model);
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
        return CustomItems.ITEMS.getEntries().stream().filter(x -> x.get() != null);
    }
}
