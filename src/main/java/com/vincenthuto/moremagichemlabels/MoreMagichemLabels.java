package com.vincenthuto.moremagichemlabels;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MoreMagichemLabels.MODID)
public class MoreMagichemLabels
{
    public static final String MODID = "moremagichemlabels";
    public static final Logger LOGGER = LogManager.getLogger();

    public MoreMagichemLabels(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
