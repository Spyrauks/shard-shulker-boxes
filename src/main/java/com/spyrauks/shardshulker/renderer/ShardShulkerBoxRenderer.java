package com.spyrauks.shardshulker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.spyrauks.shardshulker.block.blockentity.ShardShulkerBoxBlockEntity;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShardShulkerBoxRenderer implements BlockEntityRenderer<ShardShulkerBoxBlockEntity> {
    private final ShulkerModel<?> model;
    public static final Material DEFAULT_AMETHYST_SHULKER_TEXTURE =
            new Material(Sheets.SHULKER_SHEET, ResourceLocation.fromNamespaceAndPath("shardshulker", "entity/shulker/amethyst_shulker"));
    public static final Material DEFAULT_PRISMARINE_SHULKER_TEXTURE =
            new Material(Sheets.SHULKER_SHEET, ResourceLocation.fromNamespaceAndPath("shardshulker", "entity/shulker/prismarine_shulker"));
    public static final Material DEFAULT_ECHO_SHULKER_TEXTURE =
            new Material(Sheets.SHULKER_SHEET, ResourceLocation.fromNamespaceAndPath("shardshulker", "entity/shulker/echo_shulker"));



    public ShardShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ShulkerModel(context.bakeLayer(ModelLayers.SHULKER));
    }

    public void render(ShardShulkerBoxBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction direction = Direction.UP;
        if (blockEntity.hasLevel()) {
            BlockState blockstate = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
            if (blockstate.getBlock() instanceof ShulkerBoxBlock) {
                direction = blockstate.getValue(ShulkerBoxBlock.FACING);
            }
        }

        DyeColor dyecolor = blockEntity.getColor();
        Item materialType = blockEntity.getMaterial();
        Material material;
        if (materialType == Items.AMETHYST_SHARD) {
            material = DEFAULT_AMETHYST_SHULKER_TEXTURE;
        } else if (materialType == Items.PRISMARINE_SHARD) {
            material = DEFAULT_PRISMARINE_SHULKER_TEXTURE;
        } else {
            material = DEFAULT_ECHO_SHULKER_TEXTURE;
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(direction.getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        poseStack.translate(0.0F, -1.0F, 0.0F);
        ModelPart modelpart = this.model.getLid();
        modelpart.setPos(0.0F, 24.0F - blockEntity.getProgress(partialTick) * 0.5F * 16.0F, 0.0F);
        modelpart.yRot = 270.0F * blockEntity.getProgress(partialTick) * ((float) Math.PI / 180F);
        VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entityCutoutNoCull);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    public AABB getRenderBoundingBox(ShardShulkerBoxBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 0.5F, pos.getY() - 0.5F, pos.getZ() - 0.5F, pos.getX() + 1.5F, pos.getY() + 1.5F, pos.getZ() + 1.5F);
    }
}