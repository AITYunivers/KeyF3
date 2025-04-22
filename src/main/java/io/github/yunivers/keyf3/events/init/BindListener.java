package io.github.yunivers.keyf3.events.init;

import io.github.yunivers.keyf3.KeyF3;
import io.github.yunivers.keyf3.registry.F3BindRegistry;
import io.github.yunivers.keyf3.registry.event.F3BindCalledEvent;
import io.github.yunivers.keyf3.registry.helper.F3Bind;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import static io.github.yunivers.keyf3.KeyF3.NAMESPACE;

@SuppressWarnings("unused")
public class BindListener
{
    @EventListener
    public void bindListen(F3BindCalledEvent event)
    {
        if (event.id == NAMESPACE.id("f3bind_reloadChunks"))
        {
            Minecraft.INSTANCE.worldRenderer.reload();
        }
        else if (event.id == NAMESPACE.id("f3bind_showHitboxes"))
        {
            KeyF3.renderHitboxes = !KeyF3.renderHitboxes;
            event.player.sendMessage("§e§l[Debug]: §rHitboxes: " + (KeyF3.renderHitboxes ? "shown" : "hidden"));
        }
        else if (event.id == NAMESPACE.id("f3bind_showBinds"))
        {
            for (F3Bind bind : F3BindRegistry.getBinds())
                event.player.sendMessage("F3 + " + Keyboard.getKeyName(bind.key) + " = " + bind.name);
        }
    }
}
