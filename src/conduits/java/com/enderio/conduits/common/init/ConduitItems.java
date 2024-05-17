package com.enderio.conduits.common.init;

import com.enderio.EnderIO;
import com.enderio.api.conduit.ConduitItemFactory;
import com.enderio.api.conduit.ConduitType;
import com.enderio.base.common.init.EIOCreativeTabs;
import com.enderio.conduits.common.items.FilterItem;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ConduitItems {
    private static final Registrate REGISTRATE = EnderIO.registrate();

    public static final ItemEntry<Item> ENERGY = createConduitItem(ConduitTypes.ENERGY, "energy");
    public static final ItemEntry<Item> FLUID = createConduitItem(ConduitTypes.FLUID, "fluid");
    public static final ItemEntry<Item> PRESSURIZED_FLUID = createConduitItem(ConduitTypes.FLUID2, "pressurized_fluid");
    public static final ItemEntry<Item> ENDER_FLUID = createConduitItem(ConduitTypes.FLUID3, "ender_fluid");
    public static final ItemEntry<Item> REDSTONE = createConduitItem(ConduitTypes.REDSTONE, "redstone");
    public static final ItemEntry<Item> ITEM = createConduitItem(ConduitTypes.ITEM, "item");

    public static final ItemEntry<FilterItem> BASIC_ITEM_FILTER = filterItemBasic("basic_item_filter").register();
    public static final ItemEntry<FilterItem> BIG_ITEM_FILTER = filterItemBig("big_item_filter").register();
    public static final ItemEntry<Item> SPEED_UPGRADE = REGISTRATE.item("extract_speed_upgrade", Item::new).tab(EIOCreativeTabs.MAIN).register();
    public static final ItemEntry<Item> SPEED_DOWNGRADE = REGISTRATE.item("extract_speed_downgrade", Item::new).tab(EIOCreativeTabs.MAIN).register();

    private static ItemEntry<Item> createConduitItem(Supplier<? extends ConduitType<?>> type, String itemName) {
        return REGISTRATE.item(itemName + "_conduit",
            properties -> ConduitItemFactory.build(type, properties))
            .tab(EIOCreativeTabs.CONDUITS)
            .model((ctx, prov) -> prov.withExistingParent(itemName+"_conduit", EnderIO.loc("item/conduit")).texture("0", type.get().getItemTexture()))
            .register();
    }

    private static ItemBuilder<FilterItem, Registrate> filterItemBasic(String name) {
        return REGISTRATE.item(name, props -> FilterItem.basic()).tab(EIOCreativeTabs.MAIN);
    }

    private static ItemBuilder<FilterItem, Registrate> filterItemBig(String name) {
        return REGISTRATE.item(name, props -> FilterItem.large()).tab(EIOCreativeTabs.MAIN);
    }

    public static void register() {}
}
