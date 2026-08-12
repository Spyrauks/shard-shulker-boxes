package com.spyrauks.shardshulker.menu;

import com.spyrauks.shardshulker.ShardShulker;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class ShardShulkerBoxMenu extends AbstractContainerMenu {
    private final int containerSize;
    private final Container container;


    public ShardShulkerBoxMenu(MenuType<ShardShulkerBoxMenu> type,int containerId, Inventory playerInventory) {
        this(type,containerId, playerInventory, new SimpleContainer(ShardShulker.ECHO_SIZE));
    }

    public ShardShulkerBoxMenu(MenuType<ShardShulkerBoxMenu> type,int containerId, Inventory playerInventory, Container container) {
        super(type, containerId);
        checkContainerSize(container, container.getContainerSize());
        this.containerSize = container.getContainerSize();
        this.container = container;
        container.startOpen(playerInventory.player);

        int i;
        int j;
        int xOffsetBox;
        int xOffsetInv;
        int yOffsetBox;
        int yOffsetInv;
        int yOffsetHotBar;

        if (containerSize == ShardShulker.AMETHYST_SIZE) {
            i = 6;
            j = 9;
            xOffsetBox = 8;
            xOffsetInv = 8;
            yOffsetBox = 18;
            yOffsetInv = 32;
            yOffsetHotBar = yOffsetInv + 4;
        } else if (containerSize == ShardShulker.PRISMARINE_SIZE) {
            i = 9;
            j = 9;
            xOffsetBox = 8;
            xOffsetInv = 8;
            yOffsetBox = 10;
            yOffsetInv = 19;
            yOffsetHotBar = yOffsetInv + 2;
        } else {
            i = 9;
            j = 12;
            xOffsetBox = 8;
            xOffsetInv = 35;
            yOffsetBox = 10;
            yOffsetInv = 19;
            yOffsetHotBar = yOffsetInv + 2;
        }

        for(int k = 0; k < i; ++k) {
            for(int l = 0; l < j; ++l) {
                this.addSlot(new ShulkerBoxSlot(container, l + k * j, xOffsetBox + l * 18, yOffsetBox + k * 18));
            }
        }

        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, xOffsetInv + k1 * 18, yOffsetInv+i*18 + i1 * 18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInventory, j1, xOffsetInv + j1 * 18, yOffsetHotBar+i*18+3*18));
        }

    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public int getContainerSize() {return this.containerSize;};
}
