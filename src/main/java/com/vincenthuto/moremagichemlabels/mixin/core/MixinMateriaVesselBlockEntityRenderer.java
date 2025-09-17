package com.vincenthuto.moremagichemlabels.mixin.core;

import com.aranaira.magichem.block.entity.MateriaVesselBlockEntity;
import com.aranaira.magichem.block.entity.renderer.MateriaVesselBlockEntityRenderer;
import com.aranaira.magichem.foundation.MagiChemBlockStateProperties;
import com.aranaira.magichem.item.AdmixtureItem;
import com.aranaira.magichem.item.EssentiaItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import com.vincenthuto.moremagichemlabels.MoreMateriaVesselContentsRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value=MateriaVesselBlockEntityRenderer.class,remap=false)
@Debug(export = true)
public abstract class MixinMateriaVesselBlockEntityRenderer implements BlockEntityRenderer<MateriaVesselBlockEntity> {

    public MixinMateriaVesselBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Inject(method = "render(Lcom/aranaira/magichem/block/entity/MateriaVesselBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("TAIL"))
    public void moremagichemlabels$render(
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
                    MoreMateriaVesselContentsRenderUtil.renderVesselAdmixtureLabel(
                            last.pose(), last.normal(), buffer, ai, mvbe.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), packedLight
                    );
                }
                if(MoreMagichemLabels.renderVesselText){
                    BlockState blockState = mvbe.getBlockState();
                    Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

                    String text =  mvbe.getMateriaType() instanceof EssentiaItem
                            ? "item.magichem.essentia_" + mvbe.getMateriaType().getMateriaName() + ".truncated"
                            : "item.magichem.admixture_" +  mvbe.getMateriaType().getMateriaName() + ".truncated";

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
                    int textWidth = font.width(Component.translatable(text));
                    float x = -textWidth / 4.0f;
                    float y = -font.lineHeight / 4.0f;

                    boolean stacked = blockState.getValue(MagiChemBlockStateProperties.STACKED);

                    if(stacked){
                        poseStack.translate(-0.1, -1.55, 0.4); // Slightly in front of the block face
                        // Scale the text appropriately
                        float scale = 0.01f; // Adjust this to make text bigger/smaller
                        poseStack.scale(scale, -scale, scale); // Negative Y scale to flip text right-side up
                        // Render the text
                        font.drawInBatch(Component.translatable(text), x, y, 0xb97500, // Brown color
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
                        font.drawInBatch(Component.translatable(text), x, y, 0xb97500, // Brown color
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




}