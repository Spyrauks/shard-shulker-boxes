package com.spyrauks.shardshulker.item.recipe;


import com.spyrauks.shardshulker.block.ModBlocks;
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
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.util.Dictionary;
import java.util.Hashtable;

import static com.spyrauks.shardshulker.item.recipe.ModRecipes.AMETHYST_SHULKER_BOX_CRAFTING;
import static com.spyrauks.shardshulker.item.recipe.ModRecipes.PRISMARINE_SHULKER_BOX_CRAFTING;
import static com.spyrauks.shardshulker.item.recipe.ModRecipes.ECHO_SHULKER_BOX_CRAFTING;

public class ShardShulkerBoxCrafting extends CustomRecipe {
    public final Item material;
    private Dictionary<Item,Item> dictMaterials = new Hashtable<>();

    public ShardShulkerBoxCrafting(CraftingBookCategory category, Item material) {
        super(category);
        this.material = material;
        dictMaterials.put(Items.AMETHYST_SHARD,Items.PRISMARINE_SHARD);
        dictMaterials.put(Items.PRISMARINE_SHARD,Items.ECHO_SHARD);
    }

    public boolean matches(CraftingInput input, Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < input.size(); ++k) {
            ItemStack itemstack = input.getItem(k);
            if (!itemstack.isEmpty()) {
                if ((Block.byItem(itemstack.getItem()) instanceof ShulkerBoxBlock) && k == 4) {
                    if (Block.byItem(itemstack.getItem()) instanceof ShardShulkerBoxBlock shardBlock) {
                        Item craftingMaterial = shardBlock.getMaterial();
                        if (this.material == dictMaterials.get(craftingMaterial)) {
                            ++i;
                        }
                    } else {
                        if (this.material == Items.AMETHYST_SHARD) {
                            ++i;
                        }
                    }
                } else {
                    if (!itemstack.is(this.material)) {
                        return false;
                    }

                    ++j;
                }

                if (j > 8 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 8;
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack itemstack = ItemStack.EMPTY;
        DyeColor dyecolor = null;

        for(int i = 0; i < input.size(); ++i) {
            ItemStack itemstack1 = input.getItem(i);
            if (!itemstack1.isEmpty()) {
                Item item = itemstack1.getItem();
                if (Block.byItem(item) instanceof ShulkerBoxBlock) {
                    itemstack = itemstack1;
                    DyeColor tmp = DyeColor.getColor(itemstack1);
                    if (tmp != null) {
                        dyecolor = tmp;
                    }
                }
            }
        }
        if (!ModRecipes.ENABLE_COLORING) {
            dyecolor = null;
        }
        Block block = ShardShulkerBoxBlock.getShardBlockByMaterialByColor(dyecolor,this.material);
        return itemstack.transmuteCopy(block, 1);
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 9;
    }

    public RecipeSerializer<?> getSerializer() {
        if (this.material == Items.AMETHYST_SHARD) {
            return AMETHYST_SHULKER_BOX_CRAFTING.get();
        } else if (this.material == Items.PRISMARINE_SHARD) {
            return PRISMARINE_SHULKER_BOX_CRAFTING.get();
        } else {
            return ECHO_SHULKER_BOX_CRAFTING.get();
        }
    }
}
