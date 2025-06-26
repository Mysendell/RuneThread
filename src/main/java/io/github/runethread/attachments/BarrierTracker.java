package io.github.runethread.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class BarrierTracker implements INBTSerializable<CompoundTag> {
    private Vec3 lastPos;

    public BarrierTracker() {
        lastPos = Vec3.ZERO;
    }

    public Vec3 getLastBarrierCheckPos() {
        return lastPos;
    }

    public void setLastBarrierCheckPos(Vec3 pos) {
        this.lastPos = pos;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("lastPosX", lastPos.x);
        nbt.putDouble("lastPosY", lastPos.y);
        nbt.putDouble("lastPosZ", lastPos.z);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        double x = nbt.getDouble("lastPosX");
        double y = nbt.getDouble("lastPosY");
        double z = nbt.getDouble("lastPosZ");
        this.lastPos = new Vec3(x, y, z);
    }
}