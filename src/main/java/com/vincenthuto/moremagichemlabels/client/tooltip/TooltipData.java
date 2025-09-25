package com.vincenthuto.moremagichemlabels.client.tooltip;

public class TooltipData {

    private int borderTop = -1;
    private int borderBottom = -1;
    private int backgroundTop = -1;
    private int backgroundBottom = -1;
    private boolean textured = false;
    private String icon = "";

    public TooltipData(int borderTop, int borderBottom, int backgroundTop, int backgroundBottom, boolean textured, String icon) {
        this.borderTop = borderTop;
        this.borderBottom = borderBottom;
        this.backgroundTop = backgroundTop;
        this.backgroundBottom = backgroundBottom;
        this.textured = textured;
        this.icon = icon;
    }

    public int getBorderTop() {
        return borderTop;
    }

    public void setBorderTop(int borderTop) {
        this.borderTop = borderTop;
    }

    public int getBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(int borderBottom) {
        this.borderBottom = borderBottom;
    }

    public int getBackgroundTop() {
        return backgroundTop;
    }

    public void setBackgroundTop(int backgroundTop) {
        this.backgroundTop = backgroundTop;
    }

    public int getBackgroundBottom() {
        return backgroundBottom;
    }

    public void setBackgroundBottom(int backgroundBottom) {
        this.backgroundBottom = backgroundBottom;
    }

    public boolean isTextured() {
        return textured;
    }

    public void setTextured(boolean textured) {
        this.textured = textured;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
