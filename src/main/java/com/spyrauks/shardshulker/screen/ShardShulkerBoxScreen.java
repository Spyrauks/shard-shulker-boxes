package com.spyrauks.shardshulker.screen;


import com.spyrauks.shardshulker.ShardShulker;
import com.spyrauks.shardshulker.menu.ShardShulkerBoxMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShardShulkerBoxScreen extends AbstractContainerScreen<ShardShulkerBoxMenu> {
    private static final ResourceLocation AMETHYST_CONTAINER_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final ResourceLocation PRISMARINE_CONTAINER_TEXTURE = ResourceLocation.fromNamespaceAndPath("shardshulker","textures/gui/container/generic_81.png");
    private static final ResourceLocation ECHO_CONTAINER_TEXTURE = ResourceLocation.fromNamespaceAndPath("shardshulker","textures/gui/container/generic_108.png");
    private ResourceLocation CONTAINER_TEXTURE;
    private int containerRows;
    private int containerCols;

    public ShardShulkerBoxScreen(ShardShulkerBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu,playerInventory,title);

        this.containerRows = 6;
        this.containerCols = 9;

        switch (menu.getContainerSize()) {
            case ShardShulker.AMETHYST_SIZE:
                CONTAINER_TEXTURE = AMETHYST_CONTAINER_TEXTURE;
                this.containerRows = 6;
                this.containerCols = 9;
                this.imageHeight = 114 + this.containerRows * 18 + 2;
                this.imageWidth = 14 + this.containerCols * 18;
                this.titleLabelY = 6;
                this.inventoryLabelY = this.imageHeight - 1 - 94;

                break;
            case ShardShulker.PRISMARINE_SIZE:
                CONTAINER_TEXTURE = PRISMARINE_CONTAINER_TEXTURE;
                this.containerRows = 9;
                this.containerCols = 9;
                this.imageHeight = 114+ this.containerRows * 18 - 20;
                this.imageWidth = 14 + this.containerCols * 18;
                this.titleLabelY = 1;
                this.inventoryLabelY = this.imageHeight - 1 - 84;

                break;

            case ShardShulker.ECHO_SIZE:
                CONTAINER_TEXTURE = ECHO_CONTAINER_TEXTURE;
                this.containerRows = 9;
                this.containerCols = 12;
                this.imageHeight = 114 + this.containerRows * 18 - 20;
                this.imageWidth = 14 + this.containerCols * 18;
                this.titleLabelY = 1;
                this.inventoryLabelY = this.imageHeight - 1 - 84;

                this.inventoryLabelX = 35;

                break;
        }


    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int w = 175;
        int h = 221;

        switch (menu.getContainerSize()) {
            case ShardShulker.AMETHYST_SIZE:
                w = 175;
                h = 221;
                break;
            case ShardShulker.PRISMARINE_SIZE:
                w = 175;
                h = 255;
                break;
            case ShardShulker.ECHO_SIZE:
                w = 247;
                h = 255;
                break;
        }

        guiGraphics.blit(CONTAINER_TEXTURE, i, j, 0, 0, w, h);

    }
}
