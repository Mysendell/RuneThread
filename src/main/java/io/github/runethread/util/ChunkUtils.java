package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

public class ChunkUtils {
    public static void forceLoadChunk(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        level.setChunkForced(chunkPos.x, chunkPos.z, true);
    }
    public static void removeForceLoadChunk(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        level.setChunkForced(chunkPos.x, chunkPos.z, false);
    }
}
