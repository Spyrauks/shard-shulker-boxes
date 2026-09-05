package com.spyrauks.shardshulker.block;

import com.spyrauks.shardshulker.ShardShulker;
import com.spyrauks.shardshulker.block.custom.ShardShulkerBoxBlock;
import com.spyrauks.shardshulker.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "shardshulker" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ShardShulker.MODID);

    public static final DeferredBlock<Block> AMETHYST_SHULKER_BOX = registerBlock("amethyst_shulker_box", () -> amethystShulkerBox(null, MapColor.COLOR_PURPLE));

    public static final DeferredBlock<Block> PRISMARINE_SHULKER_BOX = registerBlock("prismarine_shulker_box", () -> prismarineShulkerBox(null, MapColor.COLOR_PURPLE));

    public static final DeferredBlock<Block> ECHO_SHULKER_BOX = registerBlock("echo_shulker_box", () -> echoShulkerBox(null, MapColor.COLOR_PURPLE));

    private static final BlockBehaviour.StatePredicate NOT_CLOSED_SHULKER = (p_304352_, p_304353_, p_304354_) -> {
        BlockEntity patt0$temp = p_304353_.getBlockEntity(p_304354_);
        boolean var10000;
        if (patt0$temp instanceof ShulkerBoxBlockEntity shulkerboxblockentity) {
            var10000 = shulkerboxblockentity.isClosed();
        } else {
            var10000 = true;
        }

        return var10000;
    };

    private static Block amethystShulkerBox(@Nullable DyeColor color, MapColor mapColor) {
        return new ShardShulkerBoxBlock(color, BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().strength(2.0F).dynamicShape().noOcclusion().isSuffocating(NOT_CLOSED_SHULKER).isViewBlocking(NOT_CLOSED_SHULKER).pushReaction(PushReaction.DESTROY).sound(SoundType.AMETHYST),ShardShulker.AMETHYST_SIZE, Items.AMETHYST_SHARD);
    }

    private static Block prismarineShulkerBox(@Nullable DyeColor color, MapColor mapColor) {
        return new ShardShulkerBoxBlock(color, BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().strength(2.0F).dynamicShape().noOcclusion().isSuffocating(NOT_CLOSED_SHULKER).isViewBlocking(NOT_CLOSED_SHULKER).pushReaction(PushReaction.DESTROY).sound(SoundType.CORAL_BLOCK),ShardShulker.PRISMARINE_SIZE, Items.PRISMARINE_SHARD);
    }

    private static Block echoShulkerBox(@Nullable DyeColor color, MapColor mapColor) {
        return new ShardShulkerBoxBlock(color, BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().strength(2.0F).dynamicShape().noOcclusion().isSuffocating(NOT_CLOSED_SHULKER).isViewBlocking(NOT_CLOSED_SHULKER).pushReaction(PushReaction.DESTROY).sound(SoundType.SCULK),ShardShulker.ECHO_SIZE, Items.ECHO_SHARD);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(1)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
