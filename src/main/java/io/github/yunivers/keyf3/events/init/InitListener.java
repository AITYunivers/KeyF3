package io.github.yunivers.keyf3.events.init;

import io.github.yunivers.keyf3.registry.event.F3BindRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

import static io.github.yunivers.keyf3.KeyF3.NAMESPACE;
import static io.github.yunivers.keyf3.KeyF3.LOGGER;

@SuppressWarnings("unused")
public class InitListener
{
    @EventListener
    public static void serverInit(InitEvent event)
    {
        LOGGER.info(NAMESPACE.toString());
        StationAPI.EVENT_BUS.post(new F3BindRegistryEvent());
    }
}
