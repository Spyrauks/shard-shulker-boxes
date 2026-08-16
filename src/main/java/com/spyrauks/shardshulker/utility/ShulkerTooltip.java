package com.spyrauks.shardshulker.utility;

import com.spyrauks.shardshulker.ShardShulker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ShulkerTooltip implements ClientTooltipComponent {
    private final NonNullList<ItemStack> items;
    private final int selectedIndex;
    private final int rows;
    private final int cols;

    private ResourceLocation CONTAINER_TEXTURE = ResourceLocation.fromNamespaceAndPath("shardshulker","textures/gui/container/generic_27_tooltip.png");
    private static final ResourceLocation AMETHYST_CONTAINER_TEXTURE = ResourceLocation.fromNamespaceAndPath("shardshulker","textures/gui/container/generic_54_tooltip.png");
    private static final ResourceLocation PRISMARINE_CONTAINER_TEXTURE = ResourceLocation.fromNamespaceAndPath("shardshulker","textures/gui/container/generic_81_tooltip.png");
    private static final ResourceLocation ECHO_CONTAINER_TEXTURE = ResourceLocation.fromNamespaceAndPath("shardshulker","textures/gui/container/generic_108_tooltip.png");

    public ShulkerTooltip(ShulkerTooltipComponent component) {
        this.items = component.items();
        this.selectedIndex = component.selectedIndex();
        this.rows = Math.min(component.containerSize(),81) / 9;
        this.cols = component.containerSize() / this.rows;

        switch (component.containerSize()) {
            case ShardShulker.AMETHYST_SIZE:
                CONTAINER_TEXTURE = AMETHYST_CONTAINER_TEXTURE;
                break;
            case ShardShulker.PRISMARINE_SIZE:
                CONTAINER_TEXTURE = PRISMARINE_CONTAINER_TEXTURE;
                break;
            case ShardShulker.ECHO_SIZE:
                CONTAINER_TEXTURE = ECHO_CONTAINER_TEXTURE;
                break;
        }
    }

    @Override
    public int getHeight() {
        return rows*18 + 14 + 2;
    } //+ 2 to make a little bit of space between the container texture and the advanced tooltip

    @Override
    public int getWidth(Font font) {
        return cols*18 + 14;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.blit(CONTAINER_TEXTURE,x,y,0,0,getWidth(font),getHeight());

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row*cols + col;
                int xSlot = 7 + x + col*18;
                int ySlot = 7 + y + row*18;

                if (index == selectedIndex) {
                    guiGraphics.fill(xSlot, ySlot, xSlot + 18, ySlot + 18, 0x80FFFFFF);
                }

                ItemStack stack = this.items.get(index);
                if (!stack.isEmpty()) {
                    guiGraphics.renderItem(stack,xSlot + 1, ySlot + 1);
                    guiGraphics.renderItemDecorations(font,stack,xSlot + 1,ySlot + 1);
                }
            }
        }
    }
}
