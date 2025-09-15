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

    public static final ResourceLocation LABEL_TEXTURE_ACID_ADORNMENT = rloc("block/decorator/jar_label_acid-adornment");
    public static final ResourceLocation LABEL_TEXTURE_ALCOHOL_ALIEN = rloc("block/decorator/jar_label_alcohol-alien");
    public static final ResourceLocation LABEL_TEXTURE_BARGAIN_BLOOD = rloc("block/decorator/jar_label_bargain-blood");
    public static final ResourceLocation LABEL_TEXTURE_BONE_BREATH = rloc("block/decorator/jar_label_bone-breath");
    public static final ResourceLocation LABEL_TEXTURE_CHANGE_COLD = rloc("block/decorator/jar_label_change-cold");
    public static final ResourceLocation LABEL_TEXTURE_COLOR_COMFORT = rloc("block/decorator/jar_label_color-comfort");
    public static final ResourceLocation LABEL_TEXTURE_CONSTRUCT_CREATURE = rloc("block/decorator/jar_label_construct-creature");
    public static final ResourceLocation LABEL_TEXTURE_CRYSTAL_CURSE = rloc("block/decorator/jar_label_crystal-curse");
    public static final ResourceLocation LABEL_TEXTURE_DARKNESS_DEATH = rloc("block/decorator/jar_label_darkness-death");
    public static final ResourceLocation LABEL_TEXTURE_DELIGHT_DEMON = rloc("block/decorator/jar_label_delight-demon");
    public static final ResourceLocation LABEL_TEXTURE_DEPTHS_DESTRUCTION = rloc("block/decorator/jar_label_depths-destruction");
    public static final ResourceLocation LABEL_TEXTURE_DISASTER_DOMESTICATION = rloc("block/decorator/jar_label_disaster-domestication");
    public static final ResourceLocation LABEL_TEXTURE_DUST_ENERGY = rloc("block/decorator/jar_label_dust-energy");
    public static final ResourceLocation LABEL_TEXTURE_EROSION_EXANIMATE = rloc("block/decorator/jar_label_erosion-exanimate");
    public static final ResourceLocation LABEL_TEXTURE_FEY_FILTH = rloc("block/decorator/jar_label_fey-filth");
    public static final ResourceLocation LABEL_TEXTURE_FIRMAMENT_FLIGHT = rloc("block/decorator/jar_label_firmament-flight");
    public static final ResourceLocation LABEL_TEXTURE_FORESTS_FUNGUS = rloc("block/decorator/jar_label_forests-fungus");
    public static final ResourceLocation LABEL_TEXTURE_GOURMET_HEALING = rloc("block/decorator/jar_label_gourmet-healing");
    public static final ResourceLocation LABEL_TEXTURE_HELLS_HISTORY = rloc("block/decorator/jar_label_hells-history");
    public static final ResourceLocation LABEL_TEXTURE_INDUSTRY_INSTRUMENT = rloc("block/decorator/jar_label_industry-instrument");
    public static final ResourceLocation LABEL_TEXTURE_LEGEND_LIES = rloc("block/decorator/jar_label_legend-lies");
    public static final ResourceLocation LABEL_TEXTURE_LIGHT_LUCK = rloc("block/decorator/jar_label_light-luck");
    public static final ResourceLocation LABEL_TEXTURE_MANA_MEMORY = rloc("block/decorator/jar_label_mana-memory");
    public static final ResourceLocation LABEL_TEXTURE_METAL_MONSTER = rloc("block/decorator/jar_label_metal-monster");
    public static final ResourceLocation LABEL_TEXTURE_MOTION_MOUNTAINS = rloc("block/decorator/jar_label_motion-mountains");
    public static final ResourceLocation LABEL_TEXTURE_NECTAR_ODORS = rloc("block/decorator/jar_label_nectar-odors");
    public static final ResourceLocation LABEL_TEXTURE_PERMANENCE_PLAINS = rloc("block/decorator/jar_label_permanence-plains");
    public static final ResourceLocation LABEL_TEXTURE_PLANT_POISON = rloc("block/decorator/jar_label_plant-poison");
    public static final ResourceLocation LABEL_TEXTURE_POTENTIAL_PROTECTION = rloc("block/decorator/jar_label_potential-protection");
    public static final ResourceLocation LABEL_TEXTURE_REALM_SATIETY = rloc("block/decorator/jar_label_realm-satiety");
    public static final ResourceLocation LABEL_TEXTURE_SIGHT_SIGNALS = rloc("block/decorator/jar_label_sight-signals");
    public static final ResourceLocation LABEL_TEXTURE_SLEEP_SORCERY = rloc("block/decorator/jar_label_sleep-sorcery");
    public static final ResourceLocation LABEL_TEXTURE_SOUND_SPIRIT = rloc("block/decorator/jar_label_sound-spirit");
    public static final ResourceLocation LABEL_TEXTURE_STONE_STORM = rloc("block/decorator/jar_label_stone-storm");
    public static final ResourceLocation LABEL_TEXTURE_SWAMPS_THOUGHT = rloc("block/decorator/jar_label_swamps-thought");
    public static final ResourceLocation LABEL_TEXTURE_TRAP_TRAVEL = rloc("block/decorator/jar_label_trap-travel");
    public static final ResourceLocation LABEL_TEXTURE_VESSEL_VIOLENCE = rloc("block/decorator/jar_label_vessel-violence");
    public static final ResourceLocation LABEL_TEXTURE_WASTES_WHIMSY = rloc("block/decorator/jar_label_wastes-whimsy");
    public static final ResourceLocation LABEL_TEXTURE_WITCHCRAFT_WIZARD = rloc("block/decorator/jar_label_witchcraft-wizard");
    public static final ResourceLocation LABEL_TEXTURE_WOOD = rloc("block/decorator/jar_label_wood");

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue RENDER_VESSEL_TEXT = BUILDER
            .comment("Whether or not to render visible text on materia vessels")
            .define("renderVesselText", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean renderVesselText;

    public static ResourceLocation rloc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }


    public MoreMagichemLabels(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::onLoad);
        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.CLIENT, SPEC);
    }

    private void onLoad(final ModConfigEvent event)
    {
        renderVesselText = RENDER_VESSEL_TEXT.get();
    }

}
