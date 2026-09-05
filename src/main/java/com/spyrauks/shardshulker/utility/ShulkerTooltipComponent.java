package com.spyrauks.shardshulker.utility;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ShulkerTooltipComponent(int containerSize, NonNullList<ItemStack> items, int selectedIndex) implements TooltipComponent {
}
