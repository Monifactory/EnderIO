package com.enderio.conduits.common.blockentity;

import com.enderio.api.conduit.ConduitMenuData;
import com.enderio.conduits.common.init.ConduitItems;
import net.minecraft.world.item.Item;

public enum SlotType {
    FILTER_EXTRACT,
    FILTER_INSERT,
    UPGRADE_EXTRACT;

    public static final int Y_POSITION = 71;

    public boolean acceptsItem(Item item) {
        return switch (this) {
            case FILTER_INSERT, FILTER_EXTRACT -> item.equals(ConduitItems.BASIC_ITEM_FILTER.asItem()) || item.equals(ConduitItems.BIG_ITEM_FILTER.asItem());
            case UPGRADE_EXTRACT -> item.equals(ConduitItems.SPEED_DOWNGRADE.asItem()) || item.equals(ConduitItems.SPEED_UPGRADE.asItem());
        };
    }

    public int getX() {
        return switch (this) {
                case FILTER_EXTRACT -> 113;
                case FILTER_INSERT -> 23;
                case UPGRADE_EXTRACT -> 131;
        };
    }

    public int getY() {
        return Y_POSITION;
    }

    public boolean isAvailableFor(ConduitMenuData data) {
        return switch (this) {
            case FILTER_INSERT -> data.hasFilterInsert();
            case FILTER_EXTRACT -> data.hasFilterExtract();
            case UPGRADE_EXTRACT -> data.hasUpgrade();
        };
    }

}
