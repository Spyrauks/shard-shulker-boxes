package com.spyrauks.shardshulker.block.blockentity;

import com.spyrauks.shardshulker.ShardShulker;
import com.spyrauks.shardshulker.block.custom.ShardShulkerBoxBlock;
import com.spyrauks.shardshulker.menu.ModMenus;
import com.spyrauks.shardshulker.menu.ShardShulkerBoxMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ShardShulkerBoxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    private final int[] SLOTS;
    private NonNullList<ItemStack> itemStacks;
    private int openCount;
    private AnimationStatus animationStatus;
    private float progress;
    private float progressOld;
    @Nullable
    private final DyeColor color;
    private final int containerSize;
    private final Item material;

    public ShardShulkerBoxBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SHARDSHULKERBOX_BE.get(),pos,blockState);
        if (blockState.getBlock() instanceof ShardShulkerBoxBlock shardBlock) {
            this.containerSize = shardBlock.getContainerSize();
            this.material = shardBlock.getMaterial();
            this.color = shardBlock.getColor();
        } else {
            // Sécurité par défaut au cas où
            this.containerSize = 54;
            this.material = Items.AMETHYST_SHARD;
            this.color = null;
        }
        this.SLOTS = IntStream.range(0, containerSize).toArray();
        this.itemStacks = NonNullList.withSize(containerSize, ItemStack.EMPTY);
        this.animationStatus = ShardShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ShardShulkerBoxBlockEntity blockEntity) {
        blockEntity.updateAnimation(level, pos, state);
    }

    private void updateAnimation(Level level, BlockPos pos, BlockState state) {
        this.progressOld = this.progress;
        switch (this.animationStatus.ordinal()) {
            case 0:
                this.progress = 0.0F;
                break;
            case 1:
                this.progress += 0.1F;
                if (this.progressOld == 0.0F) {
                    doNeighborUpdates(level, pos, state);
                }

                if (this.progress >= 1.0F) {
                    this.animationStatus = ShardShulkerBoxBlockEntity.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                    doNeighborUpdates(level, pos, state);
                }

                this.moveCollidedEntities(level, pos, state);
                break;
            case 2:
                this.progress = 1.0F;
                break;
            case 3:
                this.progress -= 0.1F;
                if (this.progressOld == 1.0F) {
                    doNeighborUpdates(level, pos, state);
                }

                if (this.progress <= 0.0F) {
                    this.animationStatus = ShardShulkerBoxBlockEntity.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                    doNeighborUpdates(level, pos, state);
                }
        }

    }

    public AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AABB getBoundingBox(BlockState state) {
        return Shulker.getProgressAabb(1.0F, (Direction)state.getValue(ShulkerBoxBlock.FACING), 0.5F * this.getProgress(1.0F));
    }

    private void moveCollidedEntities(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof ShulkerBoxBlock) {
            Direction direction = (Direction)state.getValue(ShulkerBoxBlock.FACING);
            AABB aabb = Shulker.getProgressDeltaAabb(1.0F, direction, this.progressOld, this.progress).move(pos);
            List<Entity> list = level.getEntities((Entity)null, aabb);
            if (!list.isEmpty()) {
                for(Entity entity : list) {
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        entity.move(MoverType.SHULKER_BOX, new Vec3((aabb.getXsize() + 0.01) * (double)direction.getStepX(), (aabb.getYsize() + 0.01) * (double)direction.getStepY(), (aabb.getZsize() + 0.01) * (double)direction.getStepZ()));
                    }
                }
            }
        }

    }

    public int getContainerSize() {
        return containerSize;
    }

    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.openCount = type;
            if (type == 0) {
                this.animationStatus = ShardShulkerBoxBlockEntity.AnimationStatus.CLOSING;
            }

            if (type == 1) {
                this.animationStatus = ShardShulkerBoxBlockEntity.AnimationStatus.OPENING;
            }

            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    private static void doNeighborUpdates(Level level, BlockPos pos, BlockState state) {
        state.updateNeighbourShapes(level, pos, 3);
        level.updateNeighborsAt(pos, state.getBlock());
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }

            ++this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount == 1) {
                this.level.gameEvent(player, GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound((Player)null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            --this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount <= 0) {
                this.level.gameEvent(player, GameEvent.CONTAINER_CLOSE, this.worldPosition);
                this.level.playSound((Player)null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    protected Component getDefaultName() {
        Dictionary<Item,String> d = new Hashtable<>();
        d.put(Items.AMETHYST_SHARD,"container.shardshulker.amethyst_shulker_box");
        d.put(Items.PRISMARINE_SHARD,"container.shardshulker.prismarine_shulker_box");
        d.put(Items.ECHO_SHARD,"container.shardshulker.echo_shulker_box");

        return Component.translatable(d.get(material));
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.loadFromTag(tag, registries);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.itemStacks, false, registries);
        }

    }

    public void loadFromTag(CompoundTag tag, HolderLookup.Provider levelRegistry) {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag) && tag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(tag, this.itemStacks, levelRegistry);
        }

    }

    protected NonNullList<ItemStack> getItems() {
        return this.itemStacks;
    }

    protected void setItems(NonNullList<ItemStack> items) {
        this.itemStacks = items;
    }


    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return !(Block.byItem(itemStack.getItem()) instanceof ShulkerBoxBlock) && itemStack.canFitInsideContainerItems();
    }

    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public float getProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.progressOld, this.progress);
    }

    @Nullable
    public DyeColor getColor() {
        return this.color;
    }

    public Item getMaterial() {return this.material;}

    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        MenuType<ShardShulkerBoxMenu> type = switch (this.containerSize) {
            case (ShardShulker.AMETHYST_SIZE) -> ModMenus.AMETHYSTSHULKERMENU.get();
            case (ShardShulker.PRISMARINE_SIZE) -> ModMenus.PRISMARINESHULKERMENU.get();
            case (ShardShulker.ECHO_SIZE) -> ModMenus.ECHOSHULKERMENU.get();
            default -> throw new IllegalStateException("Unsupported container size: " + this.containerSize);
        };
        return new ShardShulkerBoxMenu(type, id, player, this);
    }

    public boolean isClosed() {
        return this.animationStatus == ShardShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    }


    public static enum AnimationStatus {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING;

        private AnimationStatus() {
        }
    }
}
