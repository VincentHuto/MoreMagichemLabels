package com.vincenthuto.moremagichemlabels.mixin.core;

import com.aranaira.magichem.block.entity.MateriaVesselBlockEntity;
import com.aranaira.magichem.block.entity.renderer.MateriaVesselBlockEntityRenderer;
import com.aranaira.magichem.foundation.BlockRendererCoords;
import com.aranaira.magichem.foundation.MagiChemBlockStateProperties;
import com.aranaira.magichem.item.AdmixtureItem;
import com.aranaira.magichem.util.render.MateriaVesselContentsRenderUtil;
import com.aranaira.magichem.util.render.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MateriaVesselBlockEntityRenderer.class)
public abstract class MixinMateriaVesselBlockEntityRenderer implements BlockEntityRenderer<MateriaVesselBlockEntity> {


    @Shadow public abstract void render(MateriaVesselBlockEntity mvbe, float pPartialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay);

    public MixinMateriaVesselBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Inject(method = "render(Lcom/aranaira/magichem/block/entity/MateriaVesselBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("TAIL"))
    public void moremagichemlabels$rendernewlabels(
            MateriaVesselBlockEntity mvbe,
            float pPartialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            CallbackInfo ci
    ) {

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.armorCutoutNoCull(InventoryMenu.BLOCK_ATLAS));
        PoseStack.Pose last = poseStack.last();

        if(mvbe!=null){
            if(mvbe.getMateriaType() !=null){
                if (mvbe.getMateriaType() instanceof AdmixtureItem ai) {
                    renderVesselAdmixtureLabel(
                            last.pose(), last.normal(), buffer, ai, mvbe.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), packedLight
                    );
                }
                if(MoreMagichemLabels.renderVesselText){
                    BlockState blockState = mvbe.getBlockState();
                    Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    String text = mvbe.getMateriaType().getMateriaName();
                    if (text == null || text.isEmpty()) return;
                    poseStack.pushPose();
                    poseStack.translate(0.5, 2.5, 0.5);
                    switch (facing) {
                        case NORTH:
                            poseStack.mulPose(Axis.YP.rotationDegrees(180));
                            break;
                        case SOUTH:
                            break;
                        case EAST:
                            poseStack.mulPose(Axis.YP.rotationDegrees(90));
                            break;
                        case WEST:
                            poseStack.mulPose(Axis.YP.rotationDegrees(-90));
                            break;
                    }

                    Font font = Minecraft.getInstance().font;

                    // Calculate text positioning (center it)
                    int textWidth = font.width(text);
                    float x = -textWidth / 4.0f;
                    float y = -font.lineHeight / 4.0f;

                    boolean stacked = blockState.getValue(MagiChemBlockStateProperties.STACKED);

                    if(stacked){
                        poseStack.translate(-0.1, -1.55, 0.4); // Slightly in front of the block face
                        // Scale the text appropriately
                        float scale = 0.01f; // Adjust this to make text bigger/smaller
                        poseStack.scale(scale, -scale, scale); // Negative Y scale to flip text right-side up
                        // Render the text
                        font.drawInBatch(text.substring(0, 1).toUpperCase() + text.substring(1), x, y, 0xb97500, // Brown color
                                false, // Drop shadow
                                poseStack.last().pose(),
                                bufferSource,
                                Font.DisplayMode.NORMAL,
                                0x7e5000 , // Background color
                                packedLight);
                    }else{
                        poseStack.translate(-0.07, -1.55, 0.2); // Slightly in front of the block face
                        // Scale the text appropriately
                        float scale = 0.0075f; // Adjust this to make text bigger/smaller
                        poseStack.scale(scale, -scale, scale); // Negative Y scale to flip text right-side up
                        // Render the text
                        font.drawInBatch(text.substring(0, 1).toUpperCase() + text.substring(1), x, y, 0xb97500, // Brown color
                                false, // Drop shadow
                                poseStack.last().pose(),
                                bufferSource,
                                Font.DisplayMode.NORMAL,
                                0x7e5000 , // Background color
                                packedLight);
                    }



                    poseStack.popPose();
                }
            }
        }

    }

    //Grainmerge on blank with 1c1200 at 100% opac to get good gradient
    private static ResourceLocation getTextureByEssentiaName(String in) {
        return switch (in) {
            case "acid", "adornment" -> MoreMagichemLabels.LABEL_TEXTURE_ACID_ADORNMENT;
            case "alcohol", "alien" -> MoreMagichemLabels.LABEL_TEXTURE_ALCOHOL_ALIEN;
            case "bargain", "blood" -> MoreMagichemLabels.LABEL_TEXTURE_BARGAIN_BLOOD;
            case "bone", "breath" -> MoreMagichemLabels.LABEL_TEXTURE_BONE_BREATH;
            case "change", "cold" -> MoreMagichemLabels.LABEL_TEXTURE_CHANGE_COLD;
            case "color", "comfort" -> MoreMagichemLabels.LABEL_TEXTURE_COLOR_COMFORT;
            case "construct", "creature" -> MoreMagichemLabels.LABEL_TEXTURE_CONSTRUCT_CREATURE;
            case "crystal", "curse" -> MoreMagichemLabels.LABEL_TEXTURE_CRYSTAL_CURSE;
            case "darkness", "death" -> MoreMagichemLabels.LABEL_TEXTURE_DARKNESS_DEATH;
            case "delight", "demon" -> MoreMagichemLabels.LABEL_TEXTURE_DELIGHT_DEMON;
            case "depths", "destruction" -> MoreMagichemLabels.LABEL_TEXTURE_DEPTHS_DESTRUCTION;
            case "disaster", "domestication" -> MoreMagichemLabels.LABEL_TEXTURE_DISASTER_DOMESTICATION;
            case "dust", "energy" -> MoreMagichemLabels.LABEL_TEXTURE_DUST_ENERGY;
            case "erosion", "exanimate" -> MoreMagichemLabels.LABEL_TEXTURE_EROSION_EXANIMATE;
            case "fey", "filth" -> MoreMagichemLabels.LABEL_TEXTURE_FEY_FILTH;
            case "firmament", "flight" -> MoreMagichemLabels.LABEL_TEXTURE_FIRMAMENT_FLIGHT;
            case "forests", "fungus" -> MoreMagichemLabels.LABEL_TEXTURE_FORESTS_FUNGUS;
            case "gourmet", "healing" -> MoreMagichemLabels.LABEL_TEXTURE_GOURMET_HEALING;
            case "hells", "history" -> MoreMagichemLabels.LABEL_TEXTURE_HELLS_HISTORY;
            case "industry", "instrument" -> MoreMagichemLabels.LABEL_TEXTURE_INDUSTRY_INSTRUMENT;
            case "legend", "lies" -> MoreMagichemLabels.LABEL_TEXTURE_LEGEND_LIES;
            case "light", "luck" -> MoreMagichemLabels.LABEL_TEXTURE_LIGHT_LUCK;
            case "mana", "memory" -> MoreMagichemLabels.LABEL_TEXTURE_MANA_MEMORY;
            case "metal", "monster" -> MoreMagichemLabels.LABEL_TEXTURE_METAL_MONSTER;
            case "motion", "mountains" -> MoreMagichemLabels.LABEL_TEXTURE_MOTION_MOUNTAINS;
            case "nectar", "odors" -> MoreMagichemLabels.LABEL_TEXTURE_NECTAR_ODORS;
            case "permanence", "plains" -> MoreMagichemLabels.LABEL_TEXTURE_PERMANENCE_PLAINS;
            case "plant", "poison" -> MoreMagichemLabels.LABEL_TEXTURE_PLANT_POISON;
            case "potential", "protection" -> MoreMagichemLabels.LABEL_TEXTURE_POTENTIAL_PROTECTION;
            case "realm", "satiety" -> MoreMagichemLabels.LABEL_TEXTURE_REALM_SATIETY;
            case "sight", "signals" -> MoreMagichemLabels.LABEL_TEXTURE_SIGHT_SIGNALS;
            case "sleep", "sorcery" -> MoreMagichemLabels.LABEL_TEXTURE_SLEEP_SORCERY;
            case "sound", "spirit" -> MoreMagichemLabels.LABEL_TEXTURE_SOUND_SPIRIT;
            case "stone", "storm" -> MoreMagichemLabels.LABEL_TEXTURE_STONE_STORM;
            case "swamps", "thought" -> MoreMagichemLabels.LABEL_TEXTURE_SWAMPS_THOUGHT;
            case "trap", "travel" -> MoreMagichemLabels.LABEL_TEXTURE_TRAP_TRAVEL;
            case "vessel", "violence" -> MoreMagichemLabels.LABEL_TEXTURE_VESSEL_VIOLENCE;
            case "wastes", "whimsy" -> MoreMagichemLabels.LABEL_TEXTURE_WASTES_WHIMSY;
            case "witchcraft", "wizard" -> MoreMagichemLabels.LABEL_TEXTURE_WITCHCRAFT_WIZARD;
            case "wood" -> MoreMagichemLabels.LABEL_TEXTURE_WOOD;
            default -> ResourceLocation.fromNamespaceAndPath("minecraft", "block/redstone_block");
        };
    }

    private static BlockRendererCoords getCoordsByEssentiaName(String admixtureName) {
        BlockRendererCoords out = new BlockRendererCoords(0.390625F, 0.359375F, 0.21875F, 0.28125F);
        switch (admixtureName) {
            case "acid":
            case "alcohol":
            case "bargain":
            case "bone":
            case "change":
            case "color":
            case "construct":
            case "crystal":
            case "darkness":
            case "delight":
            case "depths":
            case "disaster":
            case "dust":
            case "erosion":
            case "fey":
            case "firmament":
            case "forests":
            case "gourmet":
            case "hells":
            case "industry":
            case "legend":
            case "light":
            case "mana":
            case "metal":
            case "motion":
            case "nectar":
            case "permanence":
            case "plant":
            case "potential":
            case "realm":
            case "sight":
            case "sleep":
            case "sound":
            case "stone":
            case "swamps":
            case "trap":
            case "vessel":
            case "wastes":
            case "witchcraft":
            case "wood":
                out.setUV(0.0F, 0.5F, 0.5625F, 0.0F);
                break;
            case "adornment":
            case "alien":
            case "blood":
            case "breath":
            case "cold":
            case "comfort":
            case "creature":
            case "curse":
            case "death":
            case "demon":
            case "destruction":
            case "domestication":
            case "energy":
            case "exanimate":
            case "filth":
            case "flight":
            case "fungus":
            case "healing":
            case "history":
            case "instrument":
            case "lies":
            case "luck":
            case "memory":
            case "monster":
            case "mountains":
            case "odors":
            case "plains":
            case "poison":
            case "protection":
            case "satiety":
            case "signals":
            case "sorcery":
            case "spirit":
            case "storm":
            case "thought":
            case "travel":
            case "violence":
            case "whimsy":
            case "wizard":
                out.setUV(0.5F, 1.0F, 0.5625F, 0.0F);
                break;
        }
        return out;
    }

    private static void renderVesselAdmixtureLabel(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, AdmixtureItem ai, Direction dir, int packedLight) {
        TextureAtlasSprite textureMain = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(getTextureByEssentiaName(ai.getMateriaName()));
        TextureAtlasSprite textureBookend = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MateriaVesselContentsRenderUtil.LABEL_TEXTURE_BOOKENDS);

        BlockRendererCoords bookendLeft =  new BlockRendererCoords(0.265625F, 0.359375F, 0.125F, 0.28125F);
        bookendLeft.setUV(0.0F, 0.25F, 0.5625F, 0.0F);
        float blx = bookendLeft.x;
        float bly = bookendLeft.y;
        float blz = 0.171875F;
        float blw = bookendLeft.w;
        float blh = bookendLeft.h;
        float blu = bookendLeft.u;
        float bluw = bookendLeft.uw;
        float blv = bookendLeft.v;
        float blvh = bookendLeft.vh;

        BlockRendererCoords bookendRight = new BlockRendererCoords(0.609375F, 0.359375F, 0.125F, 0.28125F);
        bookendRight.setUV(0.25F, 0.5F, 0.5625F, 0.0F);
        float brx = bookendRight.x;
        float bry = bookendRight.y;
        float brz = 0.171875F;
        float brw = bookendRight.w;
        float brh = bookendRight.h;
        float bru = bookendRight.u;
        float bruw = bookendRight.uw;
        float brv = bookendRight.v;
        float brvh = bookendRight.vh;


        BlockRendererCoords coords = getCoordsByEssentiaName(ai.getMateriaName());
        float x = coords.x;
        float y = coords.y;
        float z = 0.171875F;
        float w = coords.w;
        float h = coords.h;
        float u = coords.u;
        float uw = coords.uw;
        float v = coords.v;
        float vh = coords.vh;


        // Handle direction-based UV flipping
        if (dir == Direction.NORTH) {
            u = coords.uw;
            uw = coords.u;
            v = coords.vh;
            vh = coords.v;
            blv = bookendLeft.vh;
            blvh = bookendLeft.v;
            brv = bookendRight.vh;
            brvh = bookendRight.v;
            dir = dir.getOpposite();
        } else if (dir == Direction.SOUTH) {
            dir = dir.getOpposite();
        } else if (dir == Direction.EAST) {
            u = coords.uw;
            uw = coords.u;
            v = coords.vh;
            vh = coords.v;
            blv = bookendLeft.vh;
            blvh = bookendLeft.v;
            brv = bookendRight.vh;
            brvh = bookendRight.v;
        }
        RenderUtils.renderFaceWithUV(dir.getOpposite(), pose, normal, consumer, textureMain, x, y, z, w, h, u, uw, v, vh, -1, packedLight);
        RenderUtils.renderFaceWithUV(dir.getOpposite(), pose, normal, consumer, textureBookend, blx, bly, blz, blw, blh, blu, bluw, blv, blvh, -1, packedLight);
        RenderUtils.renderFaceWithUV(dir.getOpposite(), pose, normal, consumer, textureBookend, brx, bry, brz, brw, brh, bru, bruw, brv, brvh, -1, packedLight);
    }
}