package com.spoonypanda.ceramiccrocks.util;

import net.minecraft.util.StringRepresentable;

public enum CrockSize implements StringRepresentable {
    LARGE("large",12, 4,10,14,2),
    SMALL("small",6, 3,7,12,4);

    private final String name;
    private final int slots;
    private final int columns;
    private final double height;
    private final double width;
    private final double widthOffset;

    CrockSize(String name, int slotCount, int columns, double height, double width, double widthOffset) {
        this.name = name;
        this.slots = slotCount;
        this.columns = columns;
        this.height = height;
        this.width = width;
        this.widthOffset = widthOffset;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public int getSlotCount() {
        return slots;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return slots / columns;
    }

    public double getHeight(){
        return height;
    }

    public double getWidth(){
        return width;
    }

    public double getWidthOffset(){
        return widthOffset;
    }
}
