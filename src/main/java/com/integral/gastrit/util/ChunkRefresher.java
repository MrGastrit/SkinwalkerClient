package com.integral.gastrit.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketChunkData;

public class ChunkRefresher {
    public static void refreshChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
        WorldServer world = player.getServerWorld();

        Chunk chunk = world.getChunkProvider().provideChunk(chunkX, chunkZ);

        player.connection.sendPacket(new SPacketUnloadChunk(chunkX, chunkZ));
        player.connection.sendPacket(new SPacketChunkData(chunk, 65535));
    }

    public static void refreshChunks(EntityPlayerMP player, int radius) {
        int px = player.chunkCoordX;
        int pz = player.chunkCoordZ;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                refreshChunk(player, px + dx, pz + dz);
            }
        }
    }
}
