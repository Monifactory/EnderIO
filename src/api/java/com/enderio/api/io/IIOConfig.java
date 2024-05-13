package com.enderio.api.io;

import com.enderio.api.UseOnly;
import com.enderio.api.capability.IEnderCapabilityProvider;
import com.enderio.api.capability.SideConfig;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.LogicalSide;

/**
 * IO Config defines how each side of a block interacts with other blocks.
 */
public interface IIOConfig extends INBTSerializable<CompoundTag>, IEnderCapabilityProvider<SideConfig> {
    /**
     * Get the current IO mode for the given side.
     */
    IOMode getIOMode(Direction side);

    /**
     * Set the IO mode for this side.
     * Must be supported mode, otherwise {@link IOMode#NONE} will be set.
     */
    void setIOMode(Direction side, IOMode state);

    /**
     * Determine whether a certain side supports the provided mode.
     */
    boolean supportsIOMode(Direction side, IOMode state);

    default IOMode getNextIOMode(Direction side) {
        return getNextIOMode(side, getIOMode(side));
    }

    default IOMode getNextIOMode(Direction side, IOMode currentMode) {
        // Get the index of the current and next mode.
        int curOrd = currentMode.ordinal();
        int nextOrd = (curOrd + 1) % IOMode.values().length;

        // Cycle until we loop back on ourselves.
        while (nextOrd != curOrd) {
            IOMode next = IOMode.values()[nextOrd];

            if (supportsIOMode(side, next)) {
                return next;
            }

            nextOrd = (nextOrd + 1) % IOMode.values().length;
        }

        return currentMode;
    }

    default void cycleIOMode(Direction side) {
        setIOMode(side, getNextIOMode(side));
    }

    /**
     * Whether the IO overlay should be rendered.
     */
    @UseOnly(LogicalSide.CLIENT)
    boolean renderOverlay();
}
