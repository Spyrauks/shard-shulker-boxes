package com.spyrauks.shardshulker.server;

import com.spyrauks.shardshulker.ShardShulker;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ExtractShulkerPayload(int slotIndex, int selectedIndex) implements CustomPacketPayload {

    public static final Type<ExtractShulkerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ShardShulker.MODID,"quick_extract_shulker_box"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtractShulkerPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ExtractShulkerPayload::slotIndex,ByteBufCodecs.VAR_INT, ExtractShulkerPayload::selectedIndex, ExtractShulkerPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
