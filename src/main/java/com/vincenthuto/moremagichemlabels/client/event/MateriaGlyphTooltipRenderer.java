package com.vincenthuto.moremagichemlabels.client.event;

import com.aranaira.magichem.MagiChemMod;
import com.aranaira.magichem.foundation.BlockRendererCoords;
import com.aranaira.magichem.item.AdmixtureItem;
import com.aranaira.magichem.item.EssentiaItem;
import com.aranaira.magichem.item.MateriaItem;
import com.aranaira.magichem.recipe.DistillationFabricationRecipe;
import com.vincenthuto.moremagichemlabels.MoreMagichemLabels;
import com.vincenthuto.moremagichemlabels.MoreMateriaVesselContentsRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class MateriaGlyphTooltipRenderer implements ClientTooltipComponent {

    private final MateriaGlyphTooltipComponent comp;
    private final int spacing = Minecraft.getInstance().font.lineHeight + 2;

    // Store positions and widths for proper alignment
    private final List<Integer> glyphPositions = new ArrayList<>();
    private final List<Integer> glyphWidths = new ArrayList<>();
    private int totalWidth = 0;

    public MateriaGlyphTooltipRenderer(MateriaGlyphTooltipComponent comp) {
        this.comp = comp;
        calculateGlyphPositions();
    }

    private void calculateGlyphPositions() {
        glyphPositions.clear();
        glyphWidths.clear();
        int currentX = 0;

        if (comp.stack.getItem() instanceof MateriaItem materiaItem) {
            BlockRendererCoords coords = MoreMateriaVesselContentsRenderUtil.getCoordsByMateriaName(materiaItem);
            int width = getGlyphWidth(coords);
            glyphPositions.add(currentX);
            glyphWidths.add(width);
            totalWidth = width;
        } else {
            DistillationFabricationRecipe drecipe = DistillationFabricationRecipe
                    .getDistillingRecipe(Minecraft.getInstance().level, new ItemStack(comp.stack().getItem()));

            if (drecipe != null) {
                for (int i = 0; i < drecipe.getComponentMateria().size(); i++) {
                    MateriaItem materiaItem = (MateriaItem) drecipe.getComponentMateria().get(i).getItem();
                    BlockRendererCoords coords = MoreMateriaVesselContentsRenderUtil.getCoordsByMateriaName(materiaItem);
                    int width = getGlyphWidth(coords);

                    glyphPositions.add(currentX);
                    glyphWidths.add(width);

                    currentX += width + 2; // Add 2 pixel spacing between glyphs
                }
                totalWidth = currentX - 2; // Remove last spacing
            }
        }
    }

    private int getGlyphWidth(BlockRendererCoords coords) {
        float uw = coords.uw;
        // Display width for positioning
        if (uw > 0.5f) {
            return 10; // Wider glyphs get more space
        } else {
            return 8; // Standard width
        }
    }

    private int getTextureWidth(BlockRendererCoords coords, MateriaItem materiaItem) {
        float uw = coords.uw;
        // Exact texture width to prevent bleeding
        if (materiaItem instanceof AdmixtureItem) {
            return 8; // AdmixtureItems always use 8 width
        }
        // For EssentiaItem, calculate based on UV coordinates
        // but cap it to prevent texture bleeding
        if (uw > 0.5f) {
            // For wider textures, use a slightly smaller width to avoid bleeding
            return 9; // Slightly less than 10 to avoid capturing adjacent pixels
        } else {
            return 7; // Slightly less than 8 to avoid bleeding
        }
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(totalWidth, 16);
    }

    @Override
    public void renderText(Font font, int pX, int pY, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        ClientTooltipComponent.super.renderText(font, pX, pY, matrix4f, bufferSource);

        if (comp.stack.getItem() instanceof MateriaItem materiaItem) {
            String countText = String.valueOf(comp.stack.getCount());
            int textWidth = font.width(countText) / 2; // Divided by 2 because we scale by 0.5
            int glyphCenter = glyphPositions.get(0) + (glyphWidths.get(0) / 2);

            matrix4f.scale(0.5f);
            font.drawInBatch(Component.literal(countText),
                    2 * (pX + glyphCenter) - textWidth+4,
                    2 * pY + 14, // Slightly lower for better appearance
                    0xAABBCC, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            matrix4f.scale(2f);
        } else {
            DistillationFabricationRecipe drecipe = DistillationFabricationRecipe
                    .getDistillingRecipe(Minecraft.getInstance().level, new ItemStack(comp.stack().getItem()));

            if (drecipe != null) {
                for (int i = 0; i < drecipe.getComponentMateria().size(); i++) {
                    int compmateriaCount = drecipe.getComponentMateria().get(i).getCount();
                    MateriaItem materiaItem = (MateriaItem) drecipe.getComponentMateria().get(i).getItem();

                    String countText = String.valueOf(compmateriaCount);
                    int textWidth = font.width(countText) / 2; // Divided by 2 because we scale by 0.5
                    int glyphCenter = glyphPositions.get(i) + (glyphWidths.get(i) / 2);

                    if (materiaItem instanceof EssentiaItem ei || materiaItem instanceof AdmixtureItem ai) {
                        matrix4f.scale(0.5f);
                        font.drawInBatch(Component.literal(countText),
                                2 * (pX + glyphCenter+4) - textWidth*1.25f,
                                2 * pY + 14, // Slightly lower for better appearance
                                0xAABBCC, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                        matrix4f.scale(2f);
                    }
                }
            }
        }
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics graphics) {
        ClientTooltipComponent.super.renderImage(font, pX, pY, graphics);


        if (comp.stack.getItem() instanceof MateriaItem materiaItem) {
            ResourceLocation materiaLocation = MoreMateriaVesselContentsRenderUtil.getTextureByMateriaName(materiaItem);
            BlockRendererCoords coords = MoreMateriaVesselContentsRenderUtil.getCoordsByMateriaName(materiaItem);
            ResourceLocation rl = null;
            float u = coords.u;
            float uw = coords.uw;
            float v = coords.v;
            int castU = (int) (u * 16);
            int castUW = getGlyphWidth(coords);

            if (materiaItem instanceof EssentiaItem ei) {
                rl = ResourceLocation.
                        fromNamespaceAndPath(MagiChemMod.MODID, "textures/" + materiaLocation.getPath() + ".png");
                graphics.blit(rl,
                        pX, pY, 0, castU, v, castUW, 8, 16, 16);
            }
            if (materiaItem instanceof AdmixtureItem ai) {
                rl = ResourceLocation.
                        fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/" + materiaLocation.getPath() + ".png");
                graphics.blit(rl,
                        pX, pY, 0, castU, v, 8, 8, 16, 16);
            }
        } else {
            DistillationFabricationRecipe drecipe = DistillationFabricationRecipe
                    .getDistillingRecipe(Minecraft.getInstance().level, new ItemStack(comp.stack().getItem()));

            if (drecipe != null) {
                for (int i = 0; i < drecipe.getComponentMateria().size(); i++) {
                    ItemStack compmateria = drecipe.getComponentMateria().get(i);

                    if (compmateria.getItem() instanceof MateriaItem materiaItem) {
                        ResourceLocation materiaLocation = MoreMateriaVesselContentsRenderUtil.getTextureByMateriaName(materiaItem);
                        BlockRendererCoords coords = MoreMateriaVesselContentsRenderUtil.getCoordsByMateriaName(materiaItem);
                        ResourceLocation rl = null;
                        float u = coords.u;
                        float uw = coords.uw;
                        float v = coords.v;
                        int castU = (int) (u * 16);
                        int textureWidth = getTextureWidth(coords, materiaItem);

                        int xPos = pX + glyphPositions.get(i);

                        if (materiaItem instanceof EssentiaItem ei) {
                            rl = ResourceLocation.
                                    fromNamespaceAndPath(MagiChemMod.MODID, "textures/" + materiaLocation.getPath() + ".png");
                            graphics.blit(rl,
                                    xPos, pY, 0, castU, (int)v, textureWidth, 8, 16, 16);
                        }
                        if (materiaItem instanceof AdmixtureItem ai) {
                            rl = ResourceLocation.
                                    fromNamespaceAndPath(MoreMagichemLabels.MODID, "textures/" + materiaLocation.getPath() + ".png");
                            graphics.blit(rl,
                                    xPos, pY, 0, castU, (int)v, textureWidth, 8, 16, 16);
                        }
                    }
                }
            }
        }
    }

    public record MateriaGlyphTooltipComponent(ItemStack stack
    ) implements TooltipComponent {
    }
}