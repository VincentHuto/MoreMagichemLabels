package com.vincenthuto.moremagichemlabels.client.tooltip;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class TooltipDisplayEvent extends Event {
    private final ItemStack stack;

    private final GuiGraphics graphics;

    private final int width;
    private final int height;

    private final int x;
    private final int y;

    public TooltipDisplayEvent(ItemStack stack, GuiGraphics graphics, int width, int height, int x, int y) {
        this.stack = stack;
        this.graphics = graphics;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public ItemStack getStack() {
        return stack;
    }

    public GuiGraphics getGraphics() {
        return graphics;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}