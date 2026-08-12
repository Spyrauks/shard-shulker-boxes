package com.spyrauks.shardshulker.jei;

import com.spyrauks.shardshulker.block.ModBlocks;
import com.spyrauks.shardshulker.item.recipe.ShardShulkerBoxCrafting;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.IExtendableCraftingRecipeCategory;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.*;

@JeiPlugin
public class ShardShulkerJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("shardshulker", "jei");
    }

    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IExtendableCraftingRecipeCategory extandableCategory = registration.getCraftingCategory();
        extandableCategory.addExtension(ShardShulkerBoxCrafting.class,new ICraftingCategoryExtension<ShardShulkerBoxCrafting>() {
            @Override
            public void setRecipe(RecipeHolder<ShardShulkerBoxCrafting> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
                Dictionary<Item,Item> shardShulkerItems = new Hashtable<>();
                shardShulkerItems.put(Items.AMETHYST_SHARD,Items.SHULKER_BOX);
                shardShulkerItems.put(Items.PRISMARINE_SHARD,ModBlocks.AMETHYST_SHULKER_BOX.asItem());
                shardShulkerItems.put(Items.ECHO_SHARD,ModBlocks.PRISMARINE_SHULKER_BOX.asItem());

                ShardShulkerBoxCrafting recipe = recipeHolder.value();

                List<ItemStack> inputs = new ArrayList<>();
                List<List<ItemStack>> inputsHolder = new ArrayList<>();

                for (int i = 0; i < 9; i++) {
                    if (i == 4) {
                        inputs.add(new ItemStack(shardShulkerItems.get(recipe.material)));
                        inputsHolder.add(List.of(new ItemStack(shardShulkerItems.get(recipe.material))));
                    } else {
                        inputs.add(new ItemStack(recipe.material));
                        inputsHolder.add(List.of(new ItemStack(recipe.material)));
                    }
                }
                HolderLookup.Provider registries = net.minecraft.client.Minecraft.getInstance().getConnection().registryAccess();
                ItemStack output = recipe.assemble(CraftingInput.of(3, 3, inputs), registries);

                craftingGridHelper.createAndSetInputs(builder, inputsHolder, 3, 3);
                craftingGridHelper.createAndSetOutputs(builder, List.of(output));
            }
        });

    }
}