package com.vincenthuto.moremagichemlabels.mixin.core;

import com.aranaira.magichem.item.AdmixtureItem;
import com.aranaira.magichem.item.MateriaItem;
import com.aranaira.magichem.item.renderer.*;
import com.aranaira.magichem.registry.BlockRegistry;
import com.aranaira.magichem.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import com.vincenthuto.moremagichemlabels.MoreMateriaVesselContentsRenderUtil;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MasterItemRenderer.class)
@Debug(export = true)
public abstract class MixinMasterItemRenderer extends BlockEntityWithoutLevelRenderer {

    public MixinMasterItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Inject(method = "renderByItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At("HEAD"))
    public void moremagichemlabels$renderByItem(
            ItemStack pStack,
            ItemDisplayContext pDisplayContext,
            PoseStack pPoseStack,
            MultiBufferSource pBuffer,
            int pPackedLight,
            int pPackedOverlay,
            CallbackInfo ci){
        if (pStack.getItem() == BlockRegistry.MATERIA_VESSEL.get().asItem()) {
            moremagichemlabels$renderMateriaVessel(pStack, pDisplayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }

    }
    private void moremagichemlabels$renderMateriaVessel(
            ItemStack pStack,
            ItemDisplayContext pDisplayContext,
            PoseStack pPoseStack,
            MultiBufferSource pBuffer,
            int pPackedLight,
            int pPackedOverlay
    ) {

        MoreMagichemLabels.LOGGER.info("moremagichemlabels$renderMateriaVessel");
        PoseStack.Pose moreMagichemLabels$last = pPoseStack.last();
        VertexConsumer moreMagichemLabels$buffer = pBuffer.getBuffer(RenderType.solid());
        CompoundTag moreMagichemLabels$nbt = pStack.getTag();
        if (moreMagichemLabels$nbt != null && moreMagichemLabels$nbt.contains("type")) {
            MateriaItem moreMagichemLabels$materia = ItemRegistry.getMateriaMap(false, false).get(moreMagichemLabels$nbt.getString("type"));
            if (moreMagichemLabels$materia instanceof AdmixtureItem ai) {

                MoreMateriaVesselContentsRenderUtil.renderVesselAdmixtureLabel(moreMagichemLabels$last.pose(),
                        moreMagichemLabels$last.normal(), moreMagichemLabels$buffer, ai, Direction.NORTH, pPackedLight);
            }
        }
    }
}
