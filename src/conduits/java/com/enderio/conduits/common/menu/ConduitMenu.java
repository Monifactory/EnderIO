package com.enderio.conduits.common.menu;

import com.enderio.api.conduit.ConduitRegistries;
import com.enderio.api.conduit.ConduitType;
import com.enderio.conduits.common.blockentity.ConduitBlockEntity;
import com.enderio.conduits.common.blockentity.ConduitBundle;
import com.enderio.conduits.common.blockentity.SlotType;
import com.enderio.conduits.common.blocks.ConduitBlock;
import com.enderio.conduits.common.init.ConduitMenus;
import com.enderio.core.common.menu.SyncedMenu;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConduitMenu extends SyncedMenu<ConduitBlockEntity> {

    public List<ConduitSlot> getConduitSlots() {
        return conduitSlots;
    }

    //List capped to MaxConduitTypes inner list with 3 (2 filter, 1 upgrade) slots, because that's the maximum. All non visible slots will be made noninteractable
    private final List<ConduitSlot> conduitSlots = new ArrayList<>();

    private Direction direction;
    private ConduitType<?> type;


    public ConduitMenu(@Nullable ConduitBlockEntity blockEntity, Inventory inventory, int pContainerId, Direction direction, ConduitType type) {
        super(blockEntity, inventory, ConduitMenus.CONDUIT_MENU.get(), pContainerId);
        this.direction = direction;
        this.type = type;
        if (blockEntity != null) {
            IItemHandler conduitItemHandler = blockEntity.getConduitItemHandler();
            for (Direction forDirection : Direction.values()) {
                for (int i = 0; i < ConduitBundle.MAX_CONDUIT_TYPES; i++) {
                    for (SlotType slotType: SlotType.values()) {
                        ConduitSlot slot = new ConduitSlot(blockEntity.getBundle(),conduitItemHandler, () -> this.direction, forDirection, () -> blockEntity.getBundle().getTypes().indexOf(this.type), i, slotType);
                        conduitSlots.add(slot);
                        slot.updateVisibilityPosition();
                        addSlot(slot);
                    }
                }
            }
        }
        addInventorySlots(23,113);
    }

    // EnderIO Unofficial: Override parent method
    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = reverseDirection ? endIndex - 1 : startIndex;
        Slot slot1;
        ItemStack itemstack;
        if (stack.isStackable()) {
            while(!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) break;
                } else if (i >= endIndex) break;

                slot1 = this.slots.get(i);
                itemstack = slot1.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    // EnderIO Unofficial: Allow Stack-dependent maxStackSize in ConduitSlots
                    int maxSize = Math.min(slot1 instanceof ConduitSlot cs ? cs.getMaxStackSizeItemAware(stack) : slot1.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot1.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot1.setChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while(true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                slot1 = this.slots.get(i);
                itemstack = slot1.getItem();
                if (itemstack.isEmpty() && slot1.mayPlace(stack)) {
                    // EnderIO Unofficial: Allow Stack-dependent maxStackSize in ConduitSlots
                    int slotMaxStackSize = slot1 instanceof ConduitSlot cs ? cs.getMaxStackSizeItemAware(stack) : slot1.getMaxStackSize();
                    if (stack.getCount() > slotMaxStackSize) {
                        slot1.setByPlayer(stack.split(slotMaxStackSize));
                    } else {
                        slot1.setByPlayer(stack.split(stack.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < this.slots.size() - 36) {
                if (!this.moveItemStackTo(itemstack1, this.slots.size() - 36, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.slots.size() - 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return getBlockEntity() != null
            && getBlockEntity().stillValid(player)
            //usually called serverside only, but I want to have a clientcheck for type, to close the screen clientside immediatly
            && (player instanceof ServerPlayer ||clientValid());
    }

    private boolean clientValid() {
        return getBlockEntity().getBundle().getTypes().contains(type)
            && ConduitBlock.canBeOrIsValidConnection(getBlockEntity(), type, direction);
    }

    public static ConduitMenu factory(@Nullable MenuType<ConduitMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        Direction direction = buf.readEnum(Direction.class);
        ConduitType<?> type = ConduitRegistries.getRegistry().getValue(buf.readInt());
        if (entity instanceof ConduitBlockEntity castBlockEntity) {
            return new ConduitMenu(castBlockEntity, inventory, pContainerId, direction, type);
        }

        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new ConduitMenu(null, inventory, pContainerId, direction, type);
    }

    public ConduitType<?> getConduitType() {
        return type;
    }

    public void setConduitType(ConduitType<?> type) {
        this.type = type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (getBlockEntity() != null
            && player instanceof ServerPlayer serverPlayer
            && serverPlayer.serverLevel().players().stream().filter(p -> p != player).noneMatch(p -> p.containerMenu instanceof ConduitMenu)) {
            getBlockEntity().updateEmptyDynConnection();
        }
    }
}
