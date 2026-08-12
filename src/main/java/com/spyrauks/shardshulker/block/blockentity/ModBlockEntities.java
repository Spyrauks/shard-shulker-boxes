package com.spyrauks.shardshulker.block.blockentity;

import com.spyrauks.shardshulker.ShardShulker;
import com.spyrauks.shardshulker.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ShardShulker.MODID);

    public static final Supplier<BlockEntityType<ShardShulkerBoxBlockEntity>> SHARDSHULKERBOX_BE =
            BLOCK_ENTITIES.register("shardshulkerbox_be", () -> BlockEntityType.Builder.of(ShardShulkerBoxBlockEntity::new,
                    ModBlocks.AMETHYST_SHULKER_BOX.get(),
                    ModBlocks.PRISMARINE_SHULKER_BOX.get(),
                    ModBlocks.ECHO_SHULKER_BOX.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
