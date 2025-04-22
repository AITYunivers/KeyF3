package io.github.yunivers.keyf3.mixin;

import io.github.yunivers.keyf3.registry.F3BindRegistry;
import net.minecraft.entity.player.ClientPlayerEntity;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
    @Inject(
        method = "updateKey",
        at = @At("HEAD"),
        cancellable = true
    )
    public void keyf3$updateKey_cancelF3Bind(int key, boolean state, CallbackInfo ci)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_F3) && F3BindRegistry.hasBind(key))
            ci.cancel();
    }
}
