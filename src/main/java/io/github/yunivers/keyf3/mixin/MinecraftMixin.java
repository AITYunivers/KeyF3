package io.github.yunivers.keyf3.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.yunivers.keyf3.KeyF3;
import io.github.yunivers.keyf3.registry.F3BindRegistry;
import io.github.yunivers.keyf3.registry.event.F3BindCalledEvent;
import io.github.yunivers.keyf3.registry.helper.F3Bind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.modificationstation.stationapi.api.StationAPI;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
	@Shadow public ClientPlayerEntity player;

	@Shadow public GameOptions options;

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/player/ClientPlayerEntity;updateKey(IZ)V",
			shift = At.Shift.AFTER
		)
	)
	public void keyf3$tick_debugOnRelease(CallbackInfo ci)
	{
		boolean keyDown = Keyboard.getEventKeyState();
		int key = Keyboard.getEventKey();
		if (keyDown)
		{
			if (key == Keyboard.KEY_F3)
			{
				KeyF3.debugBindPressed = false;

				if (Keyboard.next())
				 	player.updateKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_F3))
				for (F3Bind bind : F3BindRegistry.getBinds())
					if (key == bind.key)
					{
						if (bind.debugMessage != null)
							player.sendMessage("§e§l[Debug]: §r" + bind.debugMessage);

						StationAPI.EVENT_BUS.post(
							F3BindCalledEvent.builder()
								.id(F3BindRegistry.getBindIdentifier(bind))
								.player(player)
								.build());

						KeyF3.debugBindPressed = true;
						break;
					}
		}
		else if (key == Keyboard.KEY_F3 && !KeyF3.debugBindPressed)
			options.debugHud = !options.debugHud;
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lorg/lwjgl/input/Keyboard;getEventKey()I",
			remap = false
		)
	)
	public int keyf3$tick_disableKey(Operation<Integer> original)
	{
		int key = original.call();
		if (key == Keyboard.KEY_F3 || Keyboard.isKeyDown(Keyboard.KEY_F3) && F3BindRegistry.hasBind(key))
			return 0;
		return key;
	}
}
