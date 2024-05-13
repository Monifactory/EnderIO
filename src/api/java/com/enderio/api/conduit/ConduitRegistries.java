package com.enderio.api.conduit;

import com.enderio.api.registry.EnderIORegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public class ConduitRegistries {
    /**
     * @apiNote this DeferredRegister is not exposed for you, it's just a requirement to construct the ForgeRegistry.
     */
    @ApiStatus.Internal
    public static final DeferredRegister<ConduitType<?>> CONDUIT_TYPES = DeferredRegister.create(EnderIORegistries.Keys.CONDUIT_TYPES, "enderio");

    /**
     * Create a new DeferredRegister using this ForgeRegistry as a base
     */
    public static final Supplier<IForgeRegistry<ConduitType<?>>> REGISTRY = CONDUIT_TYPES.makeRegistry(RegistryBuilder::new);

    // if only we can use vanilla mc registry
    @SuppressWarnings("UnstableApiUsage")
    public static ForgeRegistry<ConduitType<?>> getRegistry() {
        //should always be a forgeRegistry. Needed for IDs for networking/ordering
        return (ForgeRegistry<ConduitType<?>>) REGISTRY.get();
    }

    public static void register(IEventBus bus) {
        CONDUIT_TYPES.register(bus);
    }
}
