package com.vincenthuto.moremagichemlabels.mixin.core;

import com.vincenthuto.moremagichemlabels.client.tooltip.TooltipDisplayEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(GuiGraphics.class)
public class MixinGuiGraphics {
    @Inject(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onTooltipRender(Font font, List<ClientTooltipComponent> tooltip, int x, int y, ClientTooltipPositioner positioner, CallbackInfo info, RenderTooltipEvent.Pre event, int width, int height, int postWidth, int postHeight, Vector2ic postPos) {
        if (!tooltip.isEmpty())
            MinecraftForge.EVENT_BUS.post(new TooltipDisplayEvent(event.getItemStack(),
                    (GuiGraphics) (Object) this, postWidth, postHeight, postPos.x(), postPos.y()));
    }
}