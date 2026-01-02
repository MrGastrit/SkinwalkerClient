package com.integral.gastrit.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleCustomRedstone extends Particle {
    public ParticleCustomRedstone(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);

        this.particleRed = 1.0F;
        this.particleGreen = 0.0F;
        this.particleBlue = 0.0F;
        this.particleAlpha = 1.0F;
        this.particleScale = 1.0F;
        this.particleMaxAge = 120;

        this.setParticleTextureIndex(65);
    }

    @Override
    public int getFXLayer() {
        return 0;
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        return 0xF000F0;
    }

    public boolean isTransparent() {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        int fadeStart = 100;
        int fadeDuration = 20;

        if (this.particleAge >= fadeStart) {
            float fadeProgress = (float)(this.particleAge - fadeStart) / (float)fadeDuration;
            this.particleAlpha = 1.0F - fadeProgress;
        } else {
            this.particleAlpha = 1.0F;
        }
    }

}
