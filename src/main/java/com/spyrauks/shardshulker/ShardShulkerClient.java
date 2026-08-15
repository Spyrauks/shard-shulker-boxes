package com.spyrauks.shardshulker;

import com.spyrauks.shardshulker.block.ModBlocks;
import com.spyrauks.shardshulker.block.blockentity.ModBlockEntities;
import com.spyrauks.shardshulker.block.custom.ShardShulkerBoxBlock;
import com.spyrauks.shardshulker.item.ModItems;
import com.spyrauks.shardshulker.menu.ModMenus;
import com.spyrauks.shardshulker.renderer.ShardShulkerBoxRenderer;
import com.spyrauks.shardshulker.renderer.ShardShulkerItemRenderer;
import com.spyrauks.shardshulker.screen.ShardShulkerBoxScreen;
import com.spyrauks.shardshulker.utility.InsertShulkerPayload;
import com.spyrauks.shardshulker.utility.ShulkerTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.PacketDistributor;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ShardShulker.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ShardShulker.MODID, value = Dist.CLIENT)
public class ShardShulkerClient {
    public static final Material DEFAULT_AMETHYST_SHULKER_TEXTURE =
            new Material(Sheets.SHULKER_SHEET, ResourceLocation.fromNamespaceAndPath("shardshulker", "entity/shulker/amethyst_shulker"));
    public static final Material DEFAULT_PRISMARINE_SHULKER_TEXTURE =
            new Material(Sheets.SHULKER_SHEET, ResourceLocation.fromNamespaceAndPath("shardshulker", "entity/shulker/prismarine_shulker"));
    public static final Material DEFAULT_ECHO_SHULKER_TEXTURE =
            new Material(Sheets.SHULKER_SHEET, ResourceLocation.fromNamespaceAndPath("shardshulker", "entity/shulker/echo_shulker"));

    public ShardShulkerClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        ShardShulker.LOGGER.info("HELLO FROM CLIENT SETUP");
        ShardShulker.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.SHARDSHULKERBOX_BE.get(), ShardShulkerBoxRenderer::new);
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.AMETHYSTSHULKERMENU.get(), ShardShulkerBoxScreen::new);
        event.register(ModMenus.PRISMARINESHULKERMENU.get(), ShardShulkerBoxScreen::new);
        event.register(ModMenus.ECHOSHULKERMENU.get(), ShardShulkerBoxScreen::new);
    }

    @SubscribeEvent
    public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
        // Amethyst
        event.registerItem(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer =
                    new ShardShulkerItemRenderer(DEFAULT_AMETHYST_SHULKER_TEXTURE);

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        }, ModBlocks.AMETHYST_SHULKER_BOX.get().asItem());

        // Prismarine
        event.registerItem(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer =
                    new ShardShulkerItemRenderer(DEFAULT_PRISMARINE_SHULKER_TEXTURE);

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        }, ModBlocks.PRISMARINE_SHULKER_BOX.get().asItem());

        // Echo
        event.registerItem(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer =
                    new ShardShulkerItemRenderer(DEFAULT_ECHO_SHULKER_TEXTURE);

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        }, ModBlocks.ECHO_SHULKER_BOX.get().asItem());
    }

    @SubscribeEvent
    public static void PlayerInsert(ScreenEvent.MouseButtonPressed.Pre event) {
        if ((event.getButton() == 1) && (event.getScreen() instanceof AbstractContainerScreen<?> container)) {
            Slot slot = container.getSlotUnderMouse();
            if (slot != null) {
                ItemStack itemStack = slot.getItem();
                if (Block.byItem(itemStack.getItem()) instanceof ShulkerBoxBlock) {
                    ItemStack carried = container.getMenu().getCarried();

                    if (!((Block.byItem(carried.getItem()) instanceof ShulkerBoxBlock))) {
                    event.setCanceled(true);
                    PacketDistributor.sendToServer(new InsertShulkerPayload(slot.index));
}
                }
            }
        }
    }
}
