package com.vincenthuto.moremagichemlabels.common.event;

import com.aranaira.magichem.item.AdmixtureItem;
import com.aranaira.magichem.item.EssentiaItem;
import com.aranaira.magichem.item.MateriaItem;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = MoreMagichemLabels.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipBorderHandler {

    @SubscribeEvent
    public static void onTooltipDisplay(TooltipDisplayEvent event) {
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

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/gui/tooltip/frame/gold_frame.png");
        if (materiaItem instanceof AdmixtureItem admixtureItem) {
            int depth = admixtureItem.getDepth();
            System.out.println(depth);
            texture = switch (depth) {
                case (0), (1) ->
                        ResourceLocation.fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/gui/tooltip/frame/quicksilver_frame.png");
                case (2) ->
                        ResourceLocation.fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/gui/tooltip/frame/gold_frame.png");
                case (3), (4) ->
                        ResourceLocation.fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/gui/tooltip/frame/electrum_frame.png");
                default ->
                        ResourceLocation.fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/gui/tooltip/frame/chimerite_frame.png");
            };

        }
        if (materiaItem instanceof EssentiaItem essentiaItem) {
            texture = ResourceLocation.fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/gui/tooltip/frame/chimerite_frame.png");
        }


        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, texture);
        Minecraft.getInstance().getTextureManager().getTexture(texture).bind();

        int texWidth = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int texHeight = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

        if (texHeight == 0 || texWidth == 0)
            return;

        int patternWidth = 160;
        int patternHeight = 64;

        int cornerWidth = 32;
        int cornerHeight = 32;

        int middleWidth = 96;

        // Define the size of the tileable border sections
        int borderThickness = 16; // Adjust this based on your texture
        int tileSize = 48; // Size of one tile segment for smoother tiling

        poseStack.pushPose();
        RenderSystem.enableBlend();
        poseStack.translate(0, 0, 410.0);
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
        int verticalEnd = y + height - cornerHeight / 2 + 3;
        int verticalLength = verticalEnd - verticalStart;

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
}