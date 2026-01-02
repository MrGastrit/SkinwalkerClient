package com.integral.gastrit.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import com.integral.gastrit.particles.ParticleCustomRedstone;

public class PacketSpawnParticle implements IMessage {
    private double x, y, z;

    public PacketSpawnParticle() {
    }

    public PacketSpawnParticle(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }

    public static class Handler implements IMessageHandler<PacketSpawnParticle, IMessage> {
        @Override
        public IMessage onMessage(PacketSpawnParticle message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    World world = Minecraft.getMinecraft().world;

                    if (world != null) {
                        Particle particle = new ParticleCustomRedstone(world, message.x, message.y, message.z);
                        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                    }
                });
            }
            return null;
        }
    }
}