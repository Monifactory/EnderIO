package com.enderio.api.conduit;

import com.enderio.api.UseOnly;
import com.enderio.api.conduit.ticker.ConduitTicker;
import com.enderio.api.misc.RedstoneControl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ConduitType<T extends ExtendedConduitData<T>> {

    ResourceLocation getTexture(T extendedData);
    ResourceLocation getItemTexture();

    /**
     * Override this method if your conduit type and your conduit item registry name don't match
     * @return the conduit item that holds this type
     */
    default Item getConduitItem() {
        return ForgeRegistries.ITEMS.getValue(ConduitRegistries.getRegistry().getKey(this));
    }

    default boolean canBeInSameBlock(ConduitType<?> other) {
        return true;
    }

    default boolean canBeReplacedBy(ConduitType<?> other) {
        return false;
    }

    ConduitTicker getTicker();

    @UseOnly(LogicalSide.CLIENT)
    ClientConduitData<T> getClientData();
    ConduitMenuData getMenuData();

    T createExtendedConduitData(Level level, BlockPos pos);

    default <K> Optional<LazyOptional<K>> proxyCapability(
        Capability<K> cap,
        T extendedConduitData,
        Level level,
        BlockPos pos,
        @Nullable Direction direction,
        @Nullable Optional<NodeIdentifier.IOState> state) {
        return Optional.empty();
    }

    /**
     * @param level the level
     * @param pos conduit position
     * @param direction direction the conduit connects to
     * @return the connectiondata that should be set on connection based on context
     */
    default ConduitConnectionData getDefaultConnection(Level level, BlockPos pos, Direction direction) {
        return new ConduitConnectionData(false, true, RedstoneControl.NEVER_ACTIVE);
    }

    record ConduitConnectionData(boolean isInsert, boolean isExtract, RedstoneControl control) {}
}
