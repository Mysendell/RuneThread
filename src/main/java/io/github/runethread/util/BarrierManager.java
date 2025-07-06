package io.github.runethread.util;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BarrierManager {
    private static final List<Barrier> barriers = Collections.synchronizedList(new ArrayList<>());

    public static void addBarrier(Barrier barrier) {
        barriers.add(barrier);
    }

    public static List<Barrier> getBarriersAt(BlockPos pos) {
        synchronized (barriers) {
            return barriers.stream().filter(b -> b.isInside(pos)).toList();
        }
    }

    public static List<Barrier> isInsideAnyBarrier(BlockPos pos) {
        synchronized (barriers) {
            return barriers.stream().filter(b -> b.isInside(pos)).toList();
        }
    }

    public static Barrier isCrossingAnyBarrier(Vec3 now, Vec3 future) {
        synchronized (barriers) {
            return barriers.stream().filter(b -> b.isCrossed(now, future)).findFirst().orElse(null);
        }
    }

    public static List<Barrier> getActiveBarriers() {
        return barriers;
    }

    public static void clearBarriers() {
        barriers.clear();
    }

    public static void removeBarrier(Barrier barrier) {
        barriers.remove(barrier);
    }

    public static List<Barrier> getBarrierFromBlockEntity(RunicAltarEntity entity) {
        synchronized (barriers) {
        return barriers.stream()
                .filter(b -> b.getAltarEntity() == entity).toList();
        }
    }
}
