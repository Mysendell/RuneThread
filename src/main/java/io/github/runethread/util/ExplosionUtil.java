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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

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
    public static void accurateExplosion(ServerLevel level, BlockPos center, double radius, boolean dropBlocks, boolean destroyBlocks,boolean damageEntities, double damage) {
        if(destroyBlocks)
            AreaUtil.IterateAreaSphere(level, center, radius, (args) -> {
                BlockPos pos = (BlockPos) args[0];
                BlockState state = level.getBlockState(pos);
                if (!state.isAir())
                    level.destroyBlock(pos, dropBlocks);
                level.sendParticles(ParticleTypes.EXPLOSION, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5, 1, 0, 0, 0, 0.1);
                level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5, 1, 0, 0, 0, 0.1);
            });

        if (damageEntities)
            damageEntities(level, center, radius, damage, radius * radius);

        level.playSound(null ,center, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS);

    }

    private static void damageEntities(ServerLevel level, BlockPos center, double radius, double damage, double radiusSq) {
        AABB bounds = new AABB(
                center.getX() - radius, center.getY() - radius, center.getZ() - radius,
                center.getX() + radius, center.getY() + radius, center.getZ() + radius
        );
        List<Entity> entities = level.getEntities((Entity) null, bounds, (entity) -> entity.distanceToSqr(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5) <= radiusSq);
        DamageSource explosionSource = level.damageSources().source(DamageTypes.EXPLOSION, null);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                living.hurtServer(level,  explosionSource, (float) damage);
            }
        }
    }
}
