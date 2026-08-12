package com.spyrauks.shardshulker.item.recipe;

import com.spyrauks.shardshulker.block.custom.ShardShulkerBoxBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import static com.spyrauks.shardshulker.item.recipe.ModRecipes.SHARD_SHULKER_BOX_COLORING;

public class ShardShulkerBoxColoring extends CustomRecipe {
    public ShardShulkerBoxColoring(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < input.size(); ++k) {
            ItemStack itemstack = input.getItem(k);
            if (!itemstack.isEmpty()) {
                if (Block.byItem(itemstack.getItem()) instanceof ShardShulkerBoxBlock) {
                    ++i;
                } else {
                    if (!itemstack.is(Tags.Items.DYES)) {
                        return false;
                    }

                    ++j;
                }

                if (j > 1 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 1;
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack itemstack = ItemStack.EMPTY;
        DyeColor dyecolor = DyeColor.WHITE;
        Item material = Items.AIR;

        for(int i = 0; i < input.size(); ++i) {
            ItemStack itemstack1 = input.getItem(i);
            if (!itemstack1.isEmpty()) {
                Item item = itemstack1.getItem();
                if (Block.byItem(item) instanceof ShardShulkerBoxBlock varBlock) {
                    itemstack = itemstack1;
                    material = varBlock.getMaterial();
                } else {
                    DyeColor tmp = DyeColor.getColor(itemstack1);
                    if (tmp != null) {
                        dyecolor = tmp;
                    }
                }
            }
        }
        Block block = ShardShulkerBoxBlock.getShardBlockByMaterialByColor(dyecolor,material);
        if (!ModRecipes.ENABLE_COLORING) {
            return itemstack.transmuteCopy(Blocks.AIR, 1);
        } else {
            return itemstack.transmuteCopy(block, 1);
        }

    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return SHARD_SHULKER_BOX_COLORING.get();
    }
}