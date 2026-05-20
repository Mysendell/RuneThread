package io.github.runethread.customblocks;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.altar.RunicAltar;
import io.github.runethread.customblocks.craftingtable.animator.Animator;
import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableBlock;
import io.github.runethread.customblocks.etcher.RuneEtcher;
import io.github.runethread.customblocks.structure.CollapseStructureBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

import static io.github.runethread.customitems.CustomItems.ITEMS;

public class CustomBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RuneThread.MODID);

    public static final DeferredBlock<Block> ARCANE_TABLE_BLOCK = register("arcane_table",
            properties -> new ArcaneTableBlock(properties.sound(SoundType.METAL).noOcclusion()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1.0f)));

    public static final DeferredBlock<Block> ANIMATOR_BLOCK = register("animator",
            properties -> new Animator(properties.mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(1f).sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> MARBLE_BLOCK = register("marble",
            properties -> new Block(properties.mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1f, 6.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> TSAVORITE_ORE_BLOCK = register("tsavorite_ore",
            properties -> new Block(properties.mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1f, 6.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> COLLAPSE_STRUCTURE = register("collapse_structure",
            properties -> new CollapseStructureBlock(properties.mapColor(MapColor.COLOR_LIGHT_GRAY)));

    public static final DeferredBlock<Block> RUNIC_ALTAR_BLOCK = register("runic_altar",
            properties -> new RunicAltar(properties
                    .mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1f, 6.0f).sound(SoundType.STONE)
                    .noOcclusion()));
    public static final DeferredBlock<Block> RUNE_ETCHER = register("rune_etcher",
            properties -> new RuneEtcher(properties
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(1f, 6.0f)
                    .sound(SoundType.TUFF_BRICKS)));


    public static final DeferredBlock<Block> MARBLE_BRICK_BLOCK = register("marble_bricks",
            properties -> new Block(properties
                    .mapColor(MapColor.COLOR_GRAY)));

    public static final DeferredBlock<StairBlock> MARBLE_BRICK_STAIR_BLOCK = register("marble_brick_stairs", properties -> new StairBlock(
                    MARBLE_BRICK_BLOCK.get().defaultBlockState(),
                    properties.mapColor(MapColor.COLOR_GRAY).sound(SoundType.STONE)));

    public static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, ? extends T> blockFunc){
        DeferredBlock<T> block = BLOCKS.registerBlock(name, blockFunc);
        ITEMS.registerSimpleBlockItem(name, block);
        return block;
    }

    public static void CreativeTabBlocks(CreativeModeTab.Output output) {
        output.accept(ARCANE_TABLE_BLOCK);
        output.accept(ANIMATOR_BLOCK);
        output.accept(MARBLE_BLOCK);
        output.accept(TSAVORITE_ORE_BLOCK);
        output.accept(RUNIC_ALTAR_BLOCK);
        output.accept(MARBLE_BRICK_BLOCK.get());
        output.accept(MARBLE_BRICK_STAIR_BLOCK);
        output.accept(RUNE_ETCHER);
    }
}
