package com.vincenthuto.moremagichemlabels;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MoreMagichemLabels.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    public static boolean renderVesselText;
    private static final ForgeConfigSpec.BooleanValue RENDER_VESSEL_TEXT = BUILDER
            .comment("Whether or not to render visible text on materia vessels")
            .define("renderVesselText", true);

    public static boolean renderTooltipBorders;
    private static final ForgeConfigSpec.BooleanValue RENDER_TOOLTIP_BORDERS = BUILDER
            .comment("Whether or not to render a custom border around materia bottles")
            .define("renderTooltipBorders", true);

    public static List<? extends String> customIconLocations;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CUSTOM_ICONS= BUILDER
            .comment("List of custom spell icons or resource locations to be loaded.")
            .defineList(
                    "customIcons",
                    List.of("minecraft:example", "yourmod:custom_spell_icon"),
                    o -> o instanceof String && ResourceLocation.isValidResourceLocation((String) o)
            );

    static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
     static void onLoad(final ModConfigEvent event) {
        renderVesselText = RENDER_VESSEL_TEXT.get();
        renderTooltipBorders = RENDER_TOOLTIP_BORDERS.get();
        customIconLocations  = CUSTOM_ICONS.get();
    }
}
