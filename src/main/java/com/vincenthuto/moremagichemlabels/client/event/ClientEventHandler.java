package com.vincenthuto.moremagichemlabels.client.event;

import com.aranaira.magichem.item.AdmixtureItem;
import com.aranaira.magichem.item.EssentiaItem;
import com.aranaira.magichem.item.MateriaItem;
import com.aranaira.magichem.recipe.DistillationFabricationRecipe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.vincenthuto.moremagichemlabels.ClientConfig;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import com.vincenthuto.moremagichemlabels.ResourceUtil;
import com.vincenthuto.moremagichemlabels.client.tooltip.AnimationData;
import com.vincenthuto.moremagichemlabels.client.tooltip.TooltipDisplayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MoreMagichemLabels.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {

    @SubscribeEvent
    public static void GatherTooltipComponents(RenderTooltipEvent.GatherComponents e) {

        List<Either<FormattedText, TooltipComponent>> list = e.getTooltipElements();
        if (!(e.getItemStack().getItem() instanceof MateriaItem && Minecraft.getInstance().level != null)) {
            DistillationFabricationRecipe drecipe = DistillationFabricationRecipe
                    .getDistillingRecipe(Minecraft.getInstance().level, new ItemStack(e.getItemStack().getItem()));
            if (drecipe != null) {
                e.getTooltipElements().add(list.size(), Either.right(new MateriaGlyphTooltipRenderer.MateriaGlyphTooltipComponent(e.getItemStack())));
            }
        }
    }

    @SubscribeEvent
    public static void drawToolTipBorders(TooltipDisplayEvent event) {
        if (ClientConfig.renderTooltipBorders) {
            LocalPlayer player = Minecraft.getInstance().player;

            if (player == null)
                return;

            ItemStack stack = event.getStack();

            if (!(stack.getItem() instanceof MateriaItem materiaItem))
                return;

            GuiGraphics graphics = event.getGraphics();
            PoseStack poseStack = graphics.pose();

            int width = event.getWidth();
            int height = event.getHeight();

            int x = event.getX();
            int y = event.getY();


            ResourceLocation texture = ResourceUtil.CHIMERITE_FRAME;
            if (materiaItem instanceof AdmixtureItem admixtureItem) {
                int depth = admixtureItem.getDepth();
                texture = switch (depth) {
                    case (0), (1) -> ResourceUtil.QUICKSILVER_FRAME;
                    case (2) -> ResourceUtil.GOLD_FRAME;
                    case (3), (4) -> ResourceUtil.ELECTRUM_FRAME;
                    default -> ResourceUtil.CHIMERITE_FRAME;
                };

            }

            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, texture);
            Minecraft.getInstance().getTextureManager().getTexture(texture).bind();


            int cornerWidth = 32;
            int cornerHeight = 32;

            int middleWidth = 96;

            // Define the size of the tileable border sections
            int borderThickness = 16; // Adjust this based on your texture
            int tileSize = 48; // Size of one tile segment for smoother tiling

            poseStack.pushPose();
            RenderSystem.enableBlend();
            poseStack.translate(0, 0, 410.0);


            int texWidth = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            int texHeight = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

            if (texHeight == 0 || texWidth == 0)
                return;

            int patternWidth = 160;
            int patternHeight = 64;
            int frame = AnimationData.construct(texHeight, patternHeight, 2).getFrameByTime(player.tickCount).getKey();
            int offset = patternHeight * frame;

            // === CORNERS ===
            // Top-left corner
            graphics.blit(texture, x - cornerWidth / 2 - 3, y - cornerHeight / 2 - 3,
                    0, offset, cornerWidth, cornerHeight, texWidth, texHeight);

            // Top-right corner
            graphics.blit(texture, x + width - cornerWidth / 2 + 3, y - cornerHeight / 2 - 3,
                    patternWidth - cornerWidth, offset, cornerWidth, cornerHeight, texWidth, texHeight);

            // Bottom-left corner
            graphics.blit(texture, x - cornerWidth / 2 - 3, y + height - cornerHeight / 2 + 3,
                    0, (patternHeight - cornerHeight) + offset, cornerWidth, cornerHeight, texWidth, texHeight);

            // Bottom-right corner
            graphics.blit(texture, x + width - cornerWidth / 2 + 3, y + height - cornerHeight / 2 + 3,
                    patternWidth - cornerWidth, (patternHeight - cornerHeight) + offset, cornerWidth, cornerHeight, texWidth, texHeight);

            // === TOP AND BOTTOM BORDERS (Tiled) ===
            int horizontalStart = x + cornerWidth / 2 - 3;
            int horizontalEnd = x + width - cornerWidth / 2 + 3;
            int horizontalLength = horizontalEnd - horizontalStart;

            // Top border tiling
            int topY = y - cornerHeight / 2 - 3;
            renderHorizontalTiledBorder(graphics, texture, horizontalStart, topY, horizontalLength,
                    cornerWidth, offset, middleWidth, borderThickness,
                    texWidth, texHeight, tileSize);

            // Bottom border tiling
            int bottomY = y + height - borderThickness / 2 + 3;
            renderHorizontalTiledBorder(graphics, texture, horizontalStart, bottomY + 8, horizontalLength,
                    cornerWidth, patternHeight - borderThickness + offset,
                    middleWidth, borderThickness, texWidth, texHeight, tileSize);

            // === LEFT AND RIGHT BORDERS (Tiled) ===
            int verticalStart = y + cornerHeight / 2 - 3;

            // Left border tiling
            int leftX = x - cornerWidth / 2 - 3;
            renderVerticalTiledBorder(graphics, texture, leftX, verticalStart, 32,
                    96, 64 * frame, borderThickness,
                    patternHeight - cornerHeight * 2, texWidth, texHeight, tileSize);

            // Right border tiling
            int rightX = x + width - borderThickness / 2 + 3;
            renderVerticalTiledBorder(graphics, texture, rightX + 8, verticalStart, 32,
                    112, 64 * frame,
                    borderThickness, patternHeight - cornerHeight * 2,
                    texWidth, texHeight, tileSize);

            RenderSystem.disableBlend();
            poseStack.popPose();
        }

    }

    /**
     * Renders a horizontally tiled border
     */
    private static void renderHorizontalTiledBorder(GuiGraphics graphics, ResourceLocation texture,
                                                    int startX, int y, int totalWidth,
                                                    int texU, int texV, int texWidth, int texHeight,
                                                    int fullTexWidth, int fullTexHeight, int tileSize) {
        int currentX = startX;
        int remainingWidth = totalWidth;

        while (remainingWidth > 0) {
            int renderWidth = Math.min(tileSize, remainingWidth);

            // Calculate texture coordinates for partial tiles
            int actualTexWidth = (int) ((float) renderWidth / tileSize * texWidth);

            graphics.blit(texture, currentX, y, texU, texV,
                    renderWidth, texHeight, fullTexWidth, fullTexHeight);

            currentX += renderWidth;
            remainingWidth -= renderWidth;
        }
    }

    /**
     * Renders a vertically tiled border
     */
    private static void renderVerticalTiledBorder(GuiGraphics graphics, ResourceLocation texture,
                                                  int x, int startY, int totalHeight,
                                                  int texU, int texV, int texWidth, int texHeight,
                                                  int fullTexWidth, int fullTexHeight, int tileSize) {
        int currentY = startY;
        int remainingHeight = totalHeight;

        while (remainingHeight > 0) {
            int renderHeight = Math.min(tileSize, remainingHeight);

            // Calculate texture coordinates for partial tiles
            int actualTexHeight = (int) ((float) renderHeight / tileSize * texHeight);

            graphics.blit(texture, x, currentY, texU, texV,
                    texWidth, renderHeight, fullTexWidth, fullTexHeight);

            currentY += renderHeight;
            remainingHeight -= renderHeight;
        }
    }


    @Mod.EventBusSubscriber(modid = MoreMagichemLabels.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(MateriaGlyphTooltipRenderer.MateriaGlyphTooltipComponent.class, MateriaGlyphTooltipRenderer::new);
        }

    }

}
