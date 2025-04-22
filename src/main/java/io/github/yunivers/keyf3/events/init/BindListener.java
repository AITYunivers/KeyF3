package io.github.yunivers.keyf3.events.init;

import io.github.yunivers.keyf3.registry.F3BindRegistry;
import io.github.yunivers.keyf3.registry.event.F3BindCalledEvent;
import io.github.yunivers.keyf3.registry.helper.F3Bind;
import net.mine_diver.unsafeevents.listener.EventListener;
import org.lwjgl.input.Keyboard;

import static io.github.yunivers.keyf3.KeyF3.NAMESPACE;

@SuppressWarnings("unused")
public class BindListener
{
    @EventListener
    public void bindListen(F3BindCalledEvent event)
    {
        if (event.id == NAMESPACE.id("f3bind_showBinds"))
        {
            for (F3Bind bind : F3BindRegistry.getBinds())
                event.player.sendMessage("F3 + " + Keyboard.getKeyName(bind.key) + " = " + bind.name);
        }
    }
}
