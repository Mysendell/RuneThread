package io.github.runethread.customblocks;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.craftingtable.animator.Animator;
import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.runethread.customitems.CustomItems.ITEMS;

public class CustomBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RuneThread.MODID);


    public static final DeferredBlock<Block> ARCANE_TABLE_BLOCK = BLOCKS.registerBlock("arcane_table",
            properties -> new ArcaneTableBlock(properties.sound(SoundType.METAL).noOcclusion()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1.0f)));
    public static final DeferredItem<BlockItem> ARCANE_TABLE_ITEM = ITEMS.registerSimpleBlockItem("arcane_table", ARCANE_TABLE_BLOCK);

    public static final DeferredBlock<Block> ANIMATOR_BLOCK = BLOCKS.registerBlock("animator",
            properties -> new Animator(properties.mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(1f).sound(SoundType.DEEPSLATE)));
    public static final DeferredItem<BlockItem> ANIMATOR_ITEM = ITEMS.registerSimpleBlockItem("animator", ANIMATOR_BLOCK);

    public static void CreativeTabBlocks(CreativeModeTab.Output output) {
        output.accept(new ItemStack(ARCANE_TABLE_ITEM.get()));
        output.accept(new ItemStack(ANIMATOR_ITEM.get()));
    }
}
