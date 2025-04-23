package io.github.yunivers.keyf3.mixin.binds;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.yunivers.keyf3.KeyF3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Shadow private Minecraft client;

    @Inject(
        method = "renderFrame",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glBlendFunc(II)V",
            remap = false
        )
    )
    public void keyf3$renderFrame_renderChunkBorders(float tickDelta, long time, CallbackInfo ci,
                                                     @Local(ordinal = 0) double dx,
                                                     @Local(ordinal = 1) double dy,
                                                     @Local(ordinal = 2) double dz)
    {
        if (!KeyF3.renderChunkBorders)
            return;

        GL11.glPushMatrix();
        GL11.glLineWidth(1.0F);
        GL11.glTranslated(-dx, -dy, -dz);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        PlayerEntity player = client.player;

        // Vertical Border Lines
        for (int x = player.chunkX - 1; x <= player.chunkX + 2; x++)
            for (int z = player.chunkZ - 1; z <= player.chunkZ + 2; z++)
                if ((x == player.chunkX || x == player.chunkX + 1) && (z == player.chunkZ || z == player.chunkZ + 1))
                    renderLine(new Vector3f(x * 16, 0, z * 16), new Vector3f(x * 16, 127, z * 16),0x3F3FFF);
                else
                    renderLine(new Vector3f(x * 16, 0, z * 16), new Vector3f(x * 16, 127, z * 16),0xFF0000);

        // Current Chunk Horizontal Lines
        for (int y = 0; y <= 128; y += 2)
        {
            int x = player.chunkX * 16;
            int z = player.chunkZ * 16;
            int color = y % 16 == 0 ? 0x3F3FFF : y % 8 == 0 ? 0x009B9B : 0xFFFF00;

            renderLine(new Vector3f(x, y, z), new Vector3f(x, y, z + 16), color);
            renderLine(new Vector3f(x, y, z + 16), new Vector3f(x + 16, y, z + 16), color);
            renderLine(new Vector3f(x + 16, y, z + 16), new Vector3f(x + 16, y, z), color);
            renderLine(new Vector3f(x + 16, y, z), new Vector3f(x, y, z), color);
        }

        // Current Chunk Vertical Lines
        for (int x = 0; x <= 16; x += 2)
            for (int z = 0; z <= 16; z += 2)
                if ((x == 0 || x == 16 || z == 0 || z == 16) && !(x % 16 == 0 && z % 16 == 0))
                {
                    int fullX = player.chunkX * 16 + x;
                    int fullZ = player.chunkZ * 16 + z;
                    int color = (x > 0 && x < 16 && x % 8 == 0) || (z > 0 && z < 16 && z % 8 == 0) ? 0x009B9B : 0xFFFF00;
                    renderLine(new Vector3f(fullX, 0, fullZ), new Vector3f(fullX, 127, fullZ), color);
                }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Unique
    private void renderLine(Vector3f startPos, Vector3f endPos, int color)
    {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        GL11.glColor3f(r, g, b);

        Tessellator t = Tessellator.INSTANCE;
        t.start(GL11.GL_LINES);
        t.vertex(startPos.x, startPos.y, startPos.z);
        t.vertex(endPos.x, endPos.y, endPos.z);
        t.draw();
    }

    @WrapOperation(
        method = "onFrameUpdate",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/Display;isActive()Z",
            remap = false
        )
    )
    public boolean keyf3$onFrameUpdate_dontPauseOnInactive(Operation<Boolean> original)
    {
        if (!KeyF3.dontPauseOnInactive)
            return original.call();

        client.focused = original.call();
        return true;
    }
}
