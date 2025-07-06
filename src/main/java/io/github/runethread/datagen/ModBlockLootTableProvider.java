package io.github.runethread.datagen;

import io.github.runethread.customblocks.CustomBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(CustomBlocks.ANIMATOR_BLOCK.get());
        dropSelf(CustomBlocks.ARCANE_TABLE_BLOCK.get());
        dropSelf(CustomBlocks.MARBLE_BLOCK.get());
        dropSelf(CustomBlocks.TSAVORITE_ORE_BLOCK.get());
        dropSelf(CustomBlocks.RUNIC_ALTAR_BLOCK.get());
        dropSelf(CustomBlocks.MARBLE_BRICK_BLOCK.get());
        dropSelf(CustomBlocks.MARBLE_BRICK_STAIR_BLOCK.get());
    }
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CustomBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
