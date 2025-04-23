package io.github.yunivers.keyf3.mixin.binds;

import io.github.yunivers.keyf3.KeyF3;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void keyf3$tick_crashBinding(CallbackInfo ci) throws Exception
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_C))
        {
            KeyF3.crashTimer++;
            if (KeyF3.crashTimer >= 200)
                throw new Exception("KeyF3 sends it's regards.");
        }
        else KeyF3.crashTimer = 0;
    }
}
