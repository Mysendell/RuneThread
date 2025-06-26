package io.github.runethread.util;

import io.github.runethread.customblocks.craftingtable.altar.RunicAltarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class Barrier {
    private final BlockPos centerPos;
    private final Vec3 center;
    private final double radius;
    private final RunicAltarEntity altarEntity;
    private int ticks;
    private final double EPS = 1.5;

    public Barrier(BlockPos centerPos, double radius, RunicAltarEntity altarEntity, int ticks) {
        this.centerPos = centerPos;
        this.center = Vec3.atCenterOf(centerPos);
        this.radius = radius;
        this.altarEntity = altarEntity;
        this.ticks = ticks;
    }

    public boolean isInside(BlockPos pos) {
        return Math.abs(pos.getX() - centerPos.getX()) <= radius &&
                Math.abs(pos.getY() - centerPos.getY()) <= radius &&
                Math.abs(pos.getZ() - centerPos.getZ()) <= radius;
    }
    public boolean isCrossed(Vec3 prev, Vec3 current) {
        boolean wasInside = !BarrierManager.isInsideAnyBarrier(BlockPos.containing(prev)).isEmpty();
        boolean isNowInside = !BarrierManager.isInsideAnyBarrier(BlockPos.containing(current)).isEmpty();
        return wasInside != isNowInside;
    }
    public Vec3 clampToCubeBarrier(Vec3 prev, Vec3 attempted) {
        // Compute cube bounds
        double minX = center.x - radius, maxX = center.x + radius;
        double minY = center.y - radius, maxY = center.y + radius;
        double minZ = center.z - radius, maxZ = center.z + radius;

        Vec3 dir = attempted.subtract(prev);
        double tHit = Double.POSITIVE_INFINITY;
        // For each axis, check intersection with the plane the entity crosses
        // and keep the smallest t in (0,1]
        tHit = checkPlane(prev.x, dir.x, minX, tHit);
        tHit = checkPlane(prev.x, dir.x, maxX, tHit);
        tHit = checkPlane(prev.y, dir.y, minY, tHit);
        tHit = checkPlane(prev.y, dir.y, maxY, tHit);
        tHit = checkPlane(prev.z, dir.z, minZ, tHit);
        tHit = checkPlane(prev.z, dir.z, maxZ, tHit);

        if(attempted.y > maxY-1) {
            return null;
        }


        if(tHit > 1.0 || tHit < 0.0) {
            // No intersection found, return previous position
            return prev;
        }

        // Move to just before the barrier (tiny epsilon back along dir)
        double tClamped = Math.max(0, tHit - EPS);

        return prev.add(dir.scale(tClamped));
    }
    private double clampOutside(double value, double center, double radius) {
        double min = center - radius - EPS;
        double max = center + radius + EPS;
        if (value < min) return min;
        if (value > max) return max;
        // If inside, push to nearest edge
        double distToMin = Math.abs(value - min);
        double distToMax = Math.abs(value - max);
        return (distToMin < distToMax) ? min : max;
    }
    private double checkPlane(double p0, double d, double plane, double currentBest) {
        if (d == 0) return currentBest;
        // Compute t where p0 + d*t = plane  ⇒  t = (plane - p0)/d
        double t = (plane - p0) / d;
        // We only care about crossings in (0, 1]
        if (t > 0 && t <= 1) {
            // But only if this plane is the one being crossed:
            // i.e. p0 was on one side, attempted is on the other
            boolean fromBelow = p0 < plane;
            boolean toAbove  = (p0 + d) > plane;
            // startedInside tells us which side is “blocked”:
            // if startedInside, we’re exiting (so we hit whichever plane we go past);
            // if not startedInside, we’re entering, same logic applies.
            if (fromBelow && toAbove || (!fromBelow && !toAbove)) {
                // keep the minimum t
                return Math.min(t, currentBest);
            }
        }
        return currentBest;
    }

    public BlockPos getCenter() {
        return centerPos;
    }
    public double getRadius() {
        return radius;
    }
    public RunicAltarEntity getAltarEntity() {
        return altarEntity;
    }
    public int getTicks() {
        return ticks;
    }
    public void countdown() {
        ticks--;
    }
}
