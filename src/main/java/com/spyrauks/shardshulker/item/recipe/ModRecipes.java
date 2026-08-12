package com.spyrauks.shardshulker.item.recipe;

import com.spyrauks.shardshulker.ShardShulker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final boolean ENABLE_COLORING = false;

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ShardShulker.MODID);

    public static final Supplier<RecipeSerializer<ShardShulkerBoxCrafting>> AMETHYST_SHULKER_BOX_CRAFTING =
            RECIPE_SERIALIZERS.register("amethyst_shulker_box_crafting",
                    () -> new SimpleCraftingRecipeSerializer<>((category) -> new ShardShulkerBoxCrafting(category, Items.AMETHYST_SHARD)));

    public static final Supplier<RecipeSerializer<ShardShulkerBoxCrafting>> PRISMARINE_SHULKER_BOX_CRAFTING =
            RECIPE_SERIALIZERS.register("prismarine_shulker_box_crafting",
                    () -> new SimpleCraftingRecipeSerializer<>((category) -> new ShardShulkerBoxCrafting(category, Items.PRISMARINE_SHARD)));

    public static final Supplier<RecipeSerializer<ShardShulkerBoxCrafting>> ECHO_SHULKER_BOX_CRAFTING =
            RECIPE_SERIALIZERS.register("echo_shulker_box_crafting",
                    () -> new SimpleCraftingRecipeSerializer<>((category) -> new ShardShulkerBoxCrafting(category, Items.ECHO_SHARD)));

    public static final Supplier<RecipeSerializer<ShardShulkerBoxColoring>> SHARD_SHULKER_BOX_COLORING =
            RECIPE_SERIALIZERS.register("shard_shulker_box_coloring",
                    () -> new SimpleCraftingRecipeSerializer<>(ShardShulkerBoxColoring::new));

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
