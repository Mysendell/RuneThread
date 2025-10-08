package io.github.runethread.datagen;

import io.github.runethread.RuneThread;
import io.github.runethread.customTags.RuneTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.runethread.customitems.CustomItems.*;

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
                .add(ALPHA_RUNE.get())
                .add(BETA_RUNE.get())
                .add(LAMBDA_RUNE.get())
                .add(TAU_RUNE.get());

        tag(RuneTags.Items.FUNCTION_RUNE_TAG)
                .add(GATHER_RUNE.get())
                .add(CREATION_RUNE.get())
                .add(TRANSMUTATION_RUNE.get())
                .add(COLLAPSE_RUNE.get())
                .add(PROTECTION_RUNE.get())
                .add(PORTAL_RUNE.get());

        tag(RuneTags.Items.POWER_TAG)
                .add(POWER_RUNE.get());

        tag(RuneTags.Items.MODIFIER_RUNE_TAG)
                .add(UP_RUNE.get())
                .add(DOWN_RUNE.get())
                .add(LEFT_RUNE.get())
                .add(RIGHT_RUNE.get())
                .add(LEFT_RUNE.get())
                .add(LIVING_RUNE.get())
                .add(CIRCLE_RUNE.get())
                .add(SQUARE_RUNE.get())
                .add(NULL_RUNE.get())
                .add(REVERSE_RUNE.get())
                .add(SCALE_RUNE.get());

        tag(RuneTags.Items.NATURE_RUNE_TAG)
                .add(LIGHTNING_RUNE.get())
                .add(FIRE_RUNE.get())
                .add(DESTRUCTION_RUNE.get());

        tag(RuneTags.Items.RUNE_TAG)
                .add(POWER_RUNE.get())
                .add(ALPHA_RUNE.get())
                .add(BETA_RUNE.get())
                .add(LAMBDA_RUNE.get())
                .add(TAU_RUNE.get())
                .add(PROTECTION_RUNE.get())
                .add(UP_RUNE.get())
                .add(DOWN_RUNE.get())
                .add(LEFT_RUNE.get())
                .add(RIGHT_RUNE.get())
                .add(LEFT_RUNE.get())
                .add(LIVING_RUNE.get())
                .add(DESTRUCTION_RUNE.get())
                .add(PORTAL_RUNE.get())
                .add(CIRCLE_RUNE.get())
                .add(SQUARE_RUNE.get())
                .add(NULL_RUNE.get())
                .add(REVERSE_RUNE.get())
                .add(SCALE_RUNE.get())
                .add(LIGHTNING_RUNE.get())
                .add(FIRE_RUNE.get())
                .add(GATHER_RUNE.get())
                .add(CREATION_RUNE.get())
                .add(TRANSMUTATION_RUNE.get())
                .add(COLLAPSE_RUNE.get());

    }
}
