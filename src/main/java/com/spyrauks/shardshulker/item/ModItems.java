package com.spyrauks.shardshulker.item;

import com.spyrauks.shardshulker.ShardShulker;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ShardShulker.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
