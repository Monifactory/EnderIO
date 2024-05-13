package com.enderio.base.common.capacitor;

import com.enderio.EnderIO;
import com.enderio.api.capacitor.CapacitorModifier;
import com.enderio.api.capacitor.CapacitorData;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public enum DefaultCapacitorData implements CapacitorData, INBTSerializable<Tag> {
    NONE(0),
    BASIC(1.0f),
    DOUBLE_LAYER(2.0f),
    OCTADIC(3.0f);

    private final float base;

    DefaultCapacitorData(float base) {
        this.base = base;
    }

    @Override
    public float base() {
        return base;
    }

    @Override
    public float getModifier(CapacitorModifier modifier) {
        return base();
    }

    @Override
    public Map<CapacitorModifier, Float> modifiers() {
        return Map.of();
    }

    @Override
    public Tag serializeNBT() {
        return FloatTag.valueOf(base);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        EnderIO.LOGGER.warn("Tried to deserialize NBT for a default capacitor datum.");
    }
}
