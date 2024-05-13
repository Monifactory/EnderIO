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

import java.util.function.Function;

/**
 * Sided Fixed IO Config
 * Used when a block only has a non changeable IOMode, but different sides have different Modes
 */
public final class SidedFixedIOConfig implements IIOConfig {
    private final Function<Direction, IOMode> mode;

    public SidedFixedIOConfig(Function<Direction, IOMode> mode) {
        this.mode = mode;
    }

    @Override
    public IOMode getIOMode(Direction side) {
        return mode.apply(side);
    }

    @Override
    public void setIOMode(Direction side, IOMode mode) {}

    @Override
    public void cycleIOMode(Direction side) {}

    @Override
    public boolean supportsIOMode(Direction side, IOMode mode) {
        return mode == this.mode.apply(side);
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
