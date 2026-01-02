package com.integral.gastrit.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockHider {
    private static final Map<String, Map<BlockPos, IBlockState>> originalBlocks = new HashMap<>();

    public static void hideBlocks(EntityPlayerMP player, String[] blockIds, BlockPos pos1, BlockPos pos2) {
        World world = player.world;
        String key = player.getName();
        Map<BlockPos, IBlockState> saved = originalBlocks.computeIfAbsent(key, k -> new HashMap<>());

        Set<Block> targetBlocks = new HashSet<>();

        for (String id : blockIds) {
            Block block = Block.REGISTRY.getObject(new ResourceLocation(id));
            if (block != null) {
                targetBlocks.add(block);
            }
        }

        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(pos);

                    if (targetBlocks.contains(state.getBlock())) {
                        saved.put(pos, state);

                        SPacketBlockChange packet = new SPacketBlockChange(world, pos);
                        packet.blockState = Blocks.AIR.getDefaultState();
                        player.connection.sendPacket(packet);
                    }
                }
            }
        }
    }

    public static void showBlocks(EntityPlayerMP player) {
        String key = player.getName();

        if (!originalBlocks.containsKey(key)) return;

        World world = player.world;

        for (Map.Entry<BlockPos, IBlockState> entry : originalBlocks.get(key).entrySet()) {
            SPacketBlockChange packet = new SPacketBlockChange(world, entry.getKey());
            packet.blockState = entry.getValue();
            player.connection.sendPacket(packet);
        }

        originalBlocks.remove(key);
    }
}
