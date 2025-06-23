package io.github.runethread.datagen;

import io.github.runethread.RuneThread;
import io.github.runethread.customTags.RuneTags;
import io.github.runethread.customitems.CustomItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this(output, lookupProvider, CompletableFuture.completedFuture(blockTagKey -> Optional.empty()));
    }

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, RuneThread.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(RuneTags.Items.LOCATON_RUNE_TAG)
                .add(CustomItems.ALPHA_RUNE.get())
                .add(CustomItems.BETA_RUNE.get())
                .add(CustomItems.LAMBDA_RUNE.get())
                .add(CustomItems.TAU_RUNE.get());

        tag(RuneTags.Items.FUNCTION_RUNE_TAG)
                .add(CustomItems.PROTECTION_RUNE.get())
                .add(CustomItems.PORTAL_RUNE.get());

        tag(RuneTags.Items.POWER_TAG)
                .add(CustomItems.POWER_RUNE.get());

        tag(RuneTags.Items.TARGET_RUNE_TAG)
                .add(CustomItems.UP_RUNE.get())
                .add(CustomItems.DOWN_RUNE.get())
                .add(CustomItems.LEFT_RUNE.get())
                .add(CustomItems.RIGHT_RUNE.get())
                .add(CustomItems.LEFT_RUNE.get())
                .add(CustomItems.LIVING_RUNE.get());

        tag(RuneTags.Items.NATURE_RUNE_TAG)
                .add(CustomItems.DESTRUCTION_RUNE.get());

        tag(RuneTags.Items.RUNE_TAG)
                .add(CustomItems.POWER_RUNE.get())
                .add(CustomItems.ALPHA_RUNE.get())
                .add(CustomItems.BETA_RUNE.get())
                .add(CustomItems.LAMBDA_RUNE.get())
                .add(CustomItems.TAU_RUNE.get())
                .add(CustomItems.PROTECTION_RUNE.get())
                .add(CustomItems.UP_RUNE.get())
                .add(CustomItems.DOWN_RUNE.get())
                .add(CustomItems.LEFT_RUNE.get())
                .add(CustomItems.RIGHT_RUNE.get())
                .add(CustomItems.LEFT_RUNE.get())
                .add(CustomItems.LIVING_RUNE.get())
                .add(CustomItems.DESTRUCTION_RUNE.get())
                .add(CustomItems.PORTAL_RUNE.get());

    }
}
