package com.enderio.machines.common.souldata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EngineSoul {

    public record SoulData(ResourceLocation entitytype, String fluid, int powerpermb, int tickpermb) implements com.enderio.machines.common.souldata.SoulData {
        @Override
        public ResourceLocation getKey() {
            return entitytype();
        }
    }

    public static final Codec<SoulData> CODEC = RecordCodecBuilder.create(soulDataInstance ->
       soulDataInstance.group(ResourceLocation.CODEC.fieldOf("entity").forGetter(EngineSoul.SoulData::entitytype),
           Codec.STRING.fieldOf("fluid").forGetter(EngineSoul.SoulData::fluid),
           Codec.INT.fieldOf("power/mb").forGetter(EngineSoul.SoulData::powerpermb),
           Codec.INT.fieldOf("tick/mb").forGetter(EngineSoul.SoulData::tickpermb))
           .apply(soulDataInstance, EngineSoul.SoulData::new));

    public static final String NAME = "engine";
    public static final SoulDataReloadListener<SoulData> ENGINE = new SoulDataReloadListener<>(NAME, CODEC);

    @SubscribeEvent
    static void addResource(AddReloadListenerEvent event) {
        event.addListener(ENGINE);
    }
}
