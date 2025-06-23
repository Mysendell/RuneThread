package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class ExplosionUtil {
    /**
     * Creates a "perfect" spherical explosion.
     * @param level The server level
     * @param center Center of the explosion
     * @param radius The radius (can be a float)
     * @param dropBlocks Whether to drop block items (true = drops, false = just deletes)
     * @param damageEntities Whether to apply damage to entities within the radius
     * @param damage Amount of damage to apply to entities
     */
    public static void accurateExplosion(ServerLevel level, BlockPos center, float radius, boolean dropBlocks, boolean destroyBlocks,boolean damageEntities, float damage) {
        int minX = center.getX() - (int)Math.ceil(radius);
        int maxX = center.getX() + (int)Math.ceil(radius);
        int minY = Math.max(level.getMinY(), center.getY() - (int)Math.ceil(radius));
        int maxY = Math.min(level.getMaxY() - 1, center.getY() + (int)Math.ceil(radius));
        int minZ = center.getZ() - (int)Math.ceil(radius);
        int maxZ = center.getZ() + (int)Math.ceil(radius);

        double radiusSq = radius * radius;

        List<BlockPos> loadedChunkList = new ArrayList<>();

        if(destroyBlocks)
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        double dx = x + 0.5 - center.getX();
                        double dy = y + 0.5 - center.getY();
                        double dz = z + 0.5 - center.getZ();
                        if (dx * dx + dy * dy + dz * dz <= radiusSq) {
                            BlockPos pos = new BlockPos(x, y, z);
                            ChunkUtils.forceLoadChunk(level, pos);
                            loadedChunkList.add(pos);
                            BlockState state = level.getBlockState(pos);
                            if (!state.isAir()) {
                                if (dropBlocks) {
                                    level.destroyBlock(pos, true);
                                } else {
                                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }


        if (damageEntities) {
            AABB bounds = new AABB(
                    center.getX() - radius, center.getY() - radius, center.getZ() - radius,
                    center.getX() + radius, center.getY() + radius, center.getZ() + radius
            );
            List<Entity> entities = level.getEntities((Entity) null, bounds, (entity) -> entity.distanceToSqr(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5) <= radiusSq);
            DamageSource explosionSource = level.damageSources().source(DamageTypes.EXPLOSION, null);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    living.hurt(explosionSource, damage);
                }
            }
        }

        level.playSound(null ,center, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS);
        level.sendParticles(ParticleTypes.EXPLOSION, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5, 1, 0, 0, 0, 0.1);
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5, 1, 0, 0, 0, 0.1);
        for(BlockPos pos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, pos);
        }
    }
}
