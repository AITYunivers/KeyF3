package io.github.yunivers.keyf3.registry.event;

import io.github.yunivers.keyf3.registry.F3BindRegistry;
import io.github.yunivers.keyf3.registry.helper.F3Bind;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

@SuppressWarnings("UnstableApiUsage")
@EventPhases(StationAPI.INTERNAL_PHASE)
public class F3BindRegistryEvent extends RegistryEvent.EntryTypeBound<F3Bind, F3BindRegistry>
{
    public F3BindRegistryEvent()
    {
        super(F3BindRegistry.INSTANCE);
    }
}
