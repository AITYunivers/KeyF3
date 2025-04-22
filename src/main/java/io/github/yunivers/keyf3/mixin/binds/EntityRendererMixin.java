package io.github.yunivers.keyf3.mixin.binds;

import io.github.yunivers.keyf3.KeyF3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin
{
    @Inject(
        method = "postRender",
        at = @At("HEAD")
    )
    public void keyf3$postRender_renderHitboxes(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta, CallbackInfo ci)
    {
        if (!KeyF3.renderHitboxes)
            return;

        Box hitbox = entity.boundingBox.offset(-entity.x, -entity.y, -entity.z);
        if (hitbox != null)
        {
            GL11.glPushMatrix();
            GL11.glLineWidth(3.0F);
            GL11.glTranslated(dx, dy, dz);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.INSTANCE.worldRenderer.renderOutline(hitbox);

            GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
            hitbox.minY = hitbox.maxY = entity.getEyeHeight();
            if (entity instanceof LivingEntity)
                Minecraft.INSTANCE.worldRenderer.renderOutline(hitbox.expand(0, 0.01, 0));

            GL11.glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
            renderLine(
                new Vector3f(
                    (float)(hitbox.minX + (hitbox.maxX - hitbox.minX) / 2f),
                    (float)(hitbox.minY + (hitbox.maxY - hitbox.minY) / 2f),
                    (float)(hitbox.minZ + (hitbox.maxZ - hitbox.minZ) / 2f)),
                2.0f,
                new Vector2f(
                    entity.yaw,
                    entity.pitch
                ));

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    @SuppressWarnings("SameParameterValue")
    @Unique
    private void renderLine(Vector3f origin, double dist, Vector2f dir)
    {
        double yawRad = Math.toRadians(dir.x);
        double pitchRad = Math.toRadians(dir.y);

        double dx = -Math.sin(yawRad) * Math.cos(pitchRad) * dist;
        double dy = -Math.sin(pitchRad) * dist;
        double dz = Math.cos(yawRad) * Math.cos(pitchRad) * dist;

        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.start(GL11.GL_LINES);
        tessellator.vertex(origin.x, origin.y, origin.z);
        tessellator.vertex(origin.x + (float)dx, origin.y + (float)dy, origin.z + (float)dz);
        tessellator.draw();
    }
}
