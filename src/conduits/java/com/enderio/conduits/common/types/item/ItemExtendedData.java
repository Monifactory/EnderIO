package com.enderio.conduits.common.types.item;

import com.enderio.api.conduit.ExtendedConduitData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class ItemExtendedData implements ExtendedConduitData<ItemExtendedData> {

    private final Map<Direction, ItemSidedData> itemSidedData = new EnumMap<>(Direction.class);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (Direction direction: Direction.values()) {
            @Nullable
            ItemSidedData sidedData = itemSidedData.get(direction);
            if (sidedData != null) {
                tag.put(direction.name(), sidedData.toNbt());
            }
        }
        return tag;
    }

    @Override
    public CompoundTag serializeGuiNBT() {
        CompoundTag tag = new CompoundTag();
        for (Direction direction: Direction.values()) {
            @Nullable
            ItemSidedData sidedData = itemSidedData.get(direction);
            if (sidedData != null) {
                tag.put(direction.name(), sidedData.toGuiNbt());
            }
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (Direction direction: Direction.values()) {
            if (nbt.contains(direction.name())) {
                itemSidedData.put(direction, ItemSidedData.fromNbt(nbt.getCompound(direction.name())));
            }
        }
    }

    public ItemSidedData get(Direction direction) {
        return itemSidedData.getOrDefault(direction, new ItemSidedData());
    }
    public ItemSidedData compute(Direction direction) {
        return itemSidedData.computeIfAbsent(direction, dir -> new ItemSidedData());
    }

    public static class ItemSidedData {
        public boolean roundRobin = false;
        public int rotatingIndex = 0;
        public boolean selfFeed = false;
        public int priority = 0;
        public ItemStack insertFilter = ItemStack.EMPTY;
        public ItemStack extractFilter = ItemStack.EMPTY;
        public int extractRate = 4;

        // region Serialization
        private static final String KEY_ROTATING_INDEX = "RotatingIndex";
        private static final String KEY_ROUND_ROBIN = "RoundRobin";
        private static final String KEY_SELF_FEED = "SelfFeed";
        private static final String KEY_PRIORITY = "Priority";
        private static final String KEY_INSERT_FILTER = "InsertFilter";
        private static final String KEY_EXTRACT_FILTER = "ExtractFilter";
        private static final String KEY_EXTRACT_RATE = "ExtractRate";

        private CompoundTag toNbt() {
            CompoundTag nbt = toGuiNbt();
            nbt.putInt(KEY_ROTATING_INDEX, rotatingIndex);
            nbt.putInt(KEY_EXTRACT_RATE, extractRate);
            nbt.put(KEY_INSERT_FILTER, insertFilter.save(new CompoundTag()));
            nbt.put(KEY_EXTRACT_FILTER, extractFilter.save(new CompoundTag()));
            return nbt;
        }

        private CompoundTag toGuiNbt() {
            CompoundTag nbt = new CompoundTag();
            nbt.putBoolean(KEY_ROUND_ROBIN, roundRobin);
            nbt.putBoolean(KEY_SELF_FEED, selfFeed);
            nbt.putInt(KEY_PRIORITY, priority);
            return nbt;
        }

        private static ItemSidedData fromNbt(CompoundTag nbt) {
            ItemSidedData sidedData = new ItemSidedData();
            sidedData.roundRobin = nbt.getBoolean(KEY_ROUND_ROBIN);
            sidedData.selfFeed = nbt.getBoolean(KEY_SELF_FEED);
            sidedData.priority = nbt.getInt(KEY_PRIORITY);
            sidedData.extractRate = nbt.getInt(KEY_EXTRACT_RATE);
            if (nbt.contains(KEY_ROTATING_INDEX)) {
                sidedData.rotatingIndex= nbt.getInt(KEY_ROTATING_INDEX);
            }
            sidedData.insertFilter = ItemStack.of(nbt.getCompound(KEY_INSERT_FILTER));
            sidedData.extractFilter = ItemStack.of(nbt.getCompound(KEY_EXTRACT_FILTER));

            return sidedData;
        }

        // endregion
    }
}
