package com.spyrauks.shardshulker.menu;

import com.spyrauks.shardshulker.ShardShulker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ShardShulker.MODID);

    private static MenuType<ShardShulkerBoxMenu> createMenuTypeAmethyst() {
        MenuType<ShardShulkerBoxMenu>[] holder = new MenuType[1];
        holder[0] = new MenuType<>((id, inv) -> new ShardShulkerBoxMenu(holder[0], id, inv, new SimpleContainer(ShardShulker.AMETHYST_SIZE)), FeatureFlags.VANILLA_SET);
        return holder[0];
    }

    private static MenuType<ShardShulkerBoxMenu> createMenuTypePrismarine() {
        MenuType<ShardShulkerBoxMenu>[] holder = new MenuType[1];
        holder[0] = new MenuType<>((id, inv) -> new ShardShulkerBoxMenu(holder[0], id, inv, new SimpleContainer(ShardShulker.PRISMARINE_SIZE)), FeatureFlags.VANILLA_SET);
        return holder[0];
    }

    private static MenuType<ShardShulkerBoxMenu> createMenuTypeEcho() {
        MenuType<ShardShulkerBoxMenu>[] holder = new MenuType[1];
        holder[0] = new MenuType<>((id, inv) -> new ShardShulkerBoxMenu(holder[0], id, inv, new SimpleContainer(ShardShulker.ECHO_SIZE)), FeatureFlags.VANILLA_SET);
        return holder[0];
    }

    public static final Supplier<MenuType<ShardShulkerBoxMenu>> AMETHYSTSHULKERMENU =
            MENUS.register("amethyst_shulker_box", ModMenus::createMenuTypeAmethyst);

    public static final Supplier<MenuType<ShardShulkerBoxMenu>> PRISMARINESHULKERMENU =
            MENUS.register("prismarine_shulker_box", ModMenus::createMenuTypePrismarine);

    public static final Supplier<MenuType<ShardShulkerBoxMenu>> ECHOSHULKERMENU =
            MENUS.register("echo_shulker_box", ModMenus::createMenuTypeEcho);


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
