package com.spyrauks.shardshulker.server;

import com.spyrauks.shardshulker.ShardShulker;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record InsertShulkerPayload(int slotIndex) implements CustomPacketPayload {

    public static final Type<InsertShulkerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ShardShulker.MODID,"quick_insert_shulker_box"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InsertShulkerPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, InsertShulkerPayload::slotIndex, InsertShulkerPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
