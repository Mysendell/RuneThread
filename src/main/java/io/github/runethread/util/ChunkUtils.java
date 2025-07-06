package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

public class ChunkUtils {
    public static boolean forceLoadChunk(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        if(level.getChunkSource().hasChunk(chunkPos.x, chunkPos.z)) {
            return false;
        }
        level.setChunkForced(chunkPos.x, chunkPos.z, true);
        return true;
    }
    public static void removeForceLoadChunk(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        level.setChunkForced(chunkPos.x, chunkPos.z, false);
    }
}
