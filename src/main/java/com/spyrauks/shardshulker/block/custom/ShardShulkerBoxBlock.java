package com.spyrauks.shardshulker.block.custom;

import com.spyrauks.shardshulker.block.ModBlocks;
import com.spyrauks.shardshulker.block.blockentity.ModBlockEntities;
import com.spyrauks.shardshulker.block.blockentity.ShardShulkerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ShardShulkerBoxBlock extends ShulkerBoxBlock {
    private final int containerSize;
    private final Item material;

    public ShardShulkerBoxBlock(@Nullable DyeColor color, BlockBehaviour.Properties properties, int containerSize, Item material) {
        super(color,properties);
        this.containerSize = containerSize;
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShardShulkerBoxBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return InteractionResult.CONSUME;
        } else {
            BlockEntity var7 = level.getBlockEntity(pos);
            if (var7 instanceof ShardShulkerBoxBlockEntity) {
                ShardShulkerBoxBlockEntity shardshulkerboxblockentity = (ShardShulkerBoxBlockEntity)var7;
                if (shardCanOpen(state, level, pos, shardshulkerboxblockentity)) {
                    player.openMenu(shardshulkerboxblockentity);
                    player.awardStat(Stats.OPEN_SHULKER_BOX);
                    PiglinAi.angerNearbyPiglins(player, true);
                }

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    private static boolean shardCanOpen(BlockState state, Level level, BlockPos pos, ShardShulkerBoxBlockEntity blockEntity) {
        if (blockEntity.getAnimationStatus() != ShardShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
            return true;
        } else {
            AABB aabb = Shulker.getProgressDeltaAabb(1.0F, (Direction)state.getValue(FACING), 0.0F, 0.5F).move(pos).deflate(1.0E-6);
            return level.noCollision(aabb);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof ShardShulkerBoxBlockEntity shardshulkerboxblockentity) {
            if (!level.isClientSide && player.isCreative() && !shardshulkerboxblockentity.isEmpty()) {
                ItemStack itemstack = getColoredShardItemStack(this.getColor(),this.getMaterial());
                itemstack.applyComponents(blockentity.collectComponents());
                ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            } else {
                shardshulkerboxblockentity.unpackLootTable(player);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        BlockEntity blockentity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockentity instanceof ShardShulkerBoxBlockEntity shardshulkerboxblockentity) {
            params = params.withDynamicDrop(CONTENTS, (p_56219_) -> {
                for(int i = 0; i < shardshulkerboxblockentity.getContainerSize(); ++i) {
                    p_56219_.accept(shardshulkerboxblockentity.getItem(i));
                }

            });
        }

        return super.getDrops(state, params);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            super.onRemove(state, level, pos, newState, isMoving);
            if (blockentity instanceof ShardShulkerBoxBlockEntity) {
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
        }

    }

    public static Block getShardBlockByMaterialByColor(@Nullable DyeColor color,Item material) {
        if (material == Items.AMETHYST_SHARD) {
            if (color == null) {
                return ModBlocks.AMETHYST_SHULKER_BOX.get();
            } else {
                Block var10000;
                switch (color) {
                    case WHITE -> var10000 = Blocks.WHITE_SHULKER_BOX;
                    case ORANGE -> var10000 = Blocks.ORANGE_SHULKER_BOX;
                    case MAGENTA -> var10000 = Blocks.MAGENTA_SHULKER_BOX;
                    case LIGHT_BLUE -> var10000 = Blocks.LIGHT_BLUE_SHULKER_BOX;
                    case YELLOW -> var10000 = Blocks.YELLOW_SHULKER_BOX;
                    case LIME -> var10000 = Blocks.LIME_SHULKER_BOX;
                    case PINK -> var10000 = Blocks.PINK_SHULKER_BOX;
                    case GRAY -> var10000 = Blocks.GRAY_SHULKER_BOX;
                    case LIGHT_GRAY -> var10000 = Blocks.LIGHT_GRAY_SHULKER_BOX;
                    case CYAN -> var10000 = Blocks.CYAN_SHULKER_BOX;
                    case BLUE -> var10000 = Blocks.BLUE_SHULKER_BOX;
                    case BROWN -> var10000 = Blocks.BROWN_SHULKER_BOX;
                    case GREEN -> var10000 = Blocks.GREEN_SHULKER_BOX;
                    case RED -> var10000 = Blocks.RED_SHULKER_BOX;
                    case BLACK -> var10000 = Blocks.BLACK_SHULKER_BOX;
                    case PURPLE -> var10000 = Blocks.PURPLE_SHULKER_BOX;
                    default -> throw new MatchException((String) null, (Throwable) null);
                }

                return var10000;
            }
        } else if (material == Items.PRISMARINE_SHARD) {
            if (color == null) {
                return ModBlocks.PRISMARINE_SHULKER_BOX.get();
            } else {
                Block var10000;
                switch (color) {
                    case WHITE -> var10000 = Blocks.WHITE_SHULKER_BOX;
                    case ORANGE -> var10000 = Blocks.ORANGE_SHULKER_BOX;
                    case MAGENTA -> var10000 = Blocks.MAGENTA_SHULKER_BOX;
                    case LIGHT_BLUE -> var10000 = Blocks.LIGHT_BLUE_SHULKER_BOX;
                    case YELLOW -> var10000 = Blocks.YELLOW_SHULKER_BOX;
                    case LIME -> var10000 = Blocks.LIME_SHULKER_BOX;
                    case PINK -> var10000 = Blocks.PINK_SHULKER_BOX;
                    case GRAY -> var10000 = Blocks.GRAY_SHULKER_BOX;
                    case LIGHT_GRAY -> var10000 = Blocks.LIGHT_GRAY_SHULKER_BOX;
                    case CYAN -> var10000 = Blocks.CYAN_SHULKER_BOX;
                    case BLUE -> var10000 = Blocks.BLUE_SHULKER_BOX;
                    case BROWN -> var10000 = Blocks.BROWN_SHULKER_BOX;
                    case GREEN -> var10000 = Blocks.GREEN_SHULKER_BOX;
                    case RED -> var10000 = Blocks.RED_SHULKER_BOX;
                    case BLACK -> var10000 = Blocks.BLACK_SHULKER_BOX;
                    case PURPLE -> var10000 = Blocks.PURPLE_SHULKER_BOX;
                    default -> throw new MatchException((String) null, (Throwable) null);
                }

                return var10000;
            }
        } else {
            if (color == null) {
                return ModBlocks.ECHO_SHULKER_BOX.get();
            } else {
                Block var10000;
                switch (color) {
                    case WHITE -> var10000 = Blocks.WHITE_SHULKER_BOX;
                    case ORANGE -> var10000 = Blocks.ORANGE_SHULKER_BOX;
                    case MAGENTA -> var10000 = Blocks.MAGENTA_SHULKER_BOX;
                    case LIGHT_BLUE -> var10000 = Blocks.LIGHT_BLUE_SHULKER_BOX;
                    case YELLOW -> var10000 = Blocks.YELLOW_SHULKER_BOX;
                    case LIME -> var10000 = Blocks.LIME_SHULKER_BOX;
                    case PINK -> var10000 = Blocks.PINK_SHULKER_BOX;
                    case GRAY -> var10000 = Blocks.GRAY_SHULKER_BOX;
                    case LIGHT_GRAY -> var10000 = Blocks.LIGHT_GRAY_SHULKER_BOX;
                    case CYAN -> var10000 = Blocks.CYAN_SHULKER_BOX;
                    case BLUE -> var10000 = Blocks.BLUE_SHULKER_BOX;
                    case BROWN -> var10000 = Blocks.BROWN_SHULKER_BOX;
                    case GREEN -> var10000 = Blocks.GREEN_SHULKER_BOX;
                    case RED -> var10000 = Blocks.RED_SHULKER_BOX;
                    case BLACK -> var10000 = Blocks.BLACK_SHULKER_BOX;
                    case PURPLE -> var10000 = Blocks.PURPLE_SHULKER_BOX;
                    default -> throw new MatchException((String) null, (Throwable) null);
                }

                return var10000;
            }
        }
    }

    public static ItemStack getColoredShardItemStack(@Nullable DyeColor color,Item material) {
        return new ItemStack(getShardBlockByMaterialByColor(color,material));
    }

    public int getContainerSize() {return this.containerSize;};

    public Item getMaterial() {return this.material;};
}

