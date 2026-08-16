package com.spyrauks.shardshulker.utility;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class ShulkerTweaks {

    public void InsertShulkerBox(int containerSize, ItemStack shulkerBoxStack, ItemStack stack) {
        ItemContainerContents shulkerContents = shulkerBoxStack.getOrDefault(DataComponents.CONTAINER,ItemContainerContents.EMPTY);
        NonNullList<ItemStack> shulkerItems = NonNullList.withSize(containerSize,ItemStack.EMPTY);
        shulkerContents.copyInto(shulkerItems);

        int i = 0;
        int emptyStackIndex = -1;

        while (i < containerSize) {
            ItemStack selectedItemStack = shulkerItems.get(i);
            if (stack.getItem() == selectedItemStack.getItem()) {
                if (stack.isStackable()) {
                    int count = selectedItemStack.getCount() + stack.getCount();
                    int maxCount = selectedItemStack.getMaxStackSize();
                    if (count <= maxCount) {
                        stack.shrink(stack.getCount());
                        selectedItemStack.setCount(count);
                        shulkerItems.set(i, selectedItemStack.copy());
                        break;
                    } else if (selectedItemStack.getCount() < maxCount) {
                        stack.shrink(maxCount - selectedItemStack.getCount());
                        selectedItemStack.setCount(maxCount);
                        shulkerItems.set(i, selectedItemStack.copy());
                    }
                }
            }
            if (selectedItemStack.isEmpty()) {
                if (!stack.isStackable()) {
                    shulkerItems.set(i, stack.copy());
                    stack.setCount(0);
                    break;
                }
                if (emptyStackIndex == -1) {
                    emptyStackIndex = i;
                }
            }
            i++;
        }

        if (stack.isStackable() && (!stack.isEmpty()) && (emptyStackIndex != -1)) {
            shulkerItems.set(emptyStackIndex,stack.copy());
            stack.setCount(0);
        }

        shulkerBoxStack.set(DataComponents.CONTAINER,ItemContainerContents.fromItems(shulkerItems));
    }

    public ItemStack ExtractShulkerBox(int containerSize, ItemStack shulkerBoxStack, int selectedIndex) {
        ItemContainerContents shulkerContents = shulkerBoxStack.getOrDefault(DataComponents.CONTAINER,ItemContainerContents.EMPTY);
        NonNullList<ItemStack> shulkerItems = NonNullList.withSize(containerSize,ItemStack.EMPTY);
        shulkerContents.copyInto(shulkerItems);

        ItemStack selectedItemStack = shulkerItems.get(selectedIndex).copy();

        shulkerItems.set(selectedIndex,ItemStack.EMPTY);

        shulkerBoxStack.set(DataComponents.CONTAINER,ItemContainerContents.fromItems(shulkerItems));

        return selectedItemStack;
    }
}
