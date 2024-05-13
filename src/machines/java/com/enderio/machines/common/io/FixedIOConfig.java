package com.enderio.machines.common.io;

import com.enderio.api.capability.SideConfig;
import com.enderio.api.io.IIOConfig;
import com.enderio.api.io.IOMode;
import com.enderio.base.common.init.EIOCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

/**
 * Fixed IO Config.
 * Used when a block only has a single mode for all sides (or even wants to disable external IO altogether).
 */
public final class FixedIOConfig implements IIOConfig {
    private final IOMode mode;

    public FixedIOConfig(IOMode mode) {
        this.mode = mode;
    }

    @Override
    public IOMode getIOMode(Direction side) {
        return mode;
    }

    @Override
    public void setIOMode(Direction side, IOMode mode) {}

    @Override
    public void cycleIOMode(Direction side) {}

    @Override
    public boolean supportsIOMode(Direction side, IOMode mode) {
        return mode == this.mode;
    }

    @Override
    public boolean renderOverlay() {
        // Don't render the overlay as all sides act the same.
        return false;
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // Not enabled.
    }

    @Override
    public Capability<SideConfig> getCapabilityType() {
        return EIOCapabilities.SIDE_CONFIG;
    }

    @Override
    public LazyOptional<SideConfig> getCapability(@Nullable Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public void invalidateSide(@Nullable Direction side) {}

    @Override
    public void invalidateCaps() {}
}
