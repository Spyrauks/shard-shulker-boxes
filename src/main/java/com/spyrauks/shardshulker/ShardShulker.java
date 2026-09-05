package com.spyrauks.shardshulker;

import com.spyrauks.shardshulker.block.ModBlocks;
import com.spyrauks.shardshulker.block.blockentity.ModBlockEntities;
import com.spyrauks.shardshulker.block.custom.ShardShulkerBoxBlock;
import com.spyrauks.shardshulker.item.ModItems;
import com.spyrauks.shardshulker.item.recipe.ModRecipes;
import com.spyrauks.shardshulker.menu.ModMenus;
import com.spyrauks.shardshulker.server.ExtractShulkerPayload;
import com.spyrauks.shardshulker.server.InsertShulkerPayload;
import com.spyrauks.shardshulker.utility.ShulkerTweaks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ShardShulker.MODID)
public class ShardShulker {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "shardshulker";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final int AMETHYST_SIZE = 54;
    public static final int PRISMARINE_SIZE = 81;
    public static final int ECHO_SIZE = 108;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ShardShulker(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ShardShulker) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        ModRecipes.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(this::registerPacketDistribution);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.AMETHYST_SHULKER_BOX);
            event.accept(ModBlocks.PRISMARINE_SHULKER_BOX);
            event.accept(ModBlocks.ECHO_SHULKER_BOX);
        }
    }

    private void registerPacketDistribution(RegisterPayloadHandlersEvent event) {
        var register = event.registrar("1");

        register.playToServer(InsertShulkerPayload.TYPE, InsertShulkerPayload.STREAM_CODEC,this::serverInsertShulkerPayloadHandler);

        register.playToServer(ExtractShulkerPayload.TYPE, ExtractShulkerPayload.STREAM_CODEC,this::serverExtractShulkerPayloadHandler);
    }

    private void serverInsertShulkerPayloadHandler(InsertShulkerPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            AbstractContainerMenu menu = player.containerMenu;

            if (menu != null && (payload.slotIndex() >= 0) && (payload.slotIndex() < menu.slots.size())) {
                Slot slot = menu.getSlot(payload.slotIndex());
                ItemStack shulkerStack = slot.getItem();
                ItemStack stack = menu.getCarried();

                if (!stack.isEmpty() && (Block.byItem(shulkerStack.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock)) {
                    int containerSize = 27;
                    if (shulkerBoxBlock instanceof ShardShulkerBoxBlock shardShulkerBoxBlock) {
                        containerSize = shardShulkerBoxBlock.getContainerSize();
                    }

                    ItemStack tempShulkerStack = shulkerStack.copy();
                    ItemStack tempStack = stack.copy();

                    ShulkerTweaks insertTweak = new ShulkerTweaks();
                    insertTweak.InsertShulkerBox(containerSize,tempShulkerStack,tempStack);

                    slot.set(tempShulkerStack);

                    menu.setCarried(tempStack);

                    menu.broadcastChanges();
                }
            }
        });
    }

    private void serverExtractShulkerPayloadHandler(ExtractShulkerPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            AbstractContainerMenu menu = player.containerMenu;

            if (menu != null && (payload.slotIndex() >= 0) && (payload.slotIndex() < menu.slots.size())) {
                Slot slot = menu.getSlot(payload.slotIndex());
                ItemStack shulkerStack = slot.getItem();
                ItemStack stack = menu.getCarried();

                if (stack.isEmpty() && (Block.byItem(shulkerStack.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock)) {
                    int containerSize = 27;
                    if (shulkerBoxBlock instanceof ShardShulkerBoxBlock shardShulkerBoxBlock) {
                        containerSize = shardShulkerBoxBlock.getContainerSize();
                    }

                    ItemStack tempShulkerStack = shulkerStack.copy();
                    int selectedIndex = payload.selectedIndex();

                    ShulkerTweaks extractTweak = new ShulkerTweaks();
                    ItemStack extractedItemStack = extractTweak.ExtractShulkerBox(containerSize,tempShulkerStack,selectedIndex);

                    if (!extractedItemStack.isEmpty()) {
                        slot.set(tempShulkerStack);

                        menu.setCarried(extractedItemStack);

                        menu.broadcastChanges();
                    }
                }
            }
        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
