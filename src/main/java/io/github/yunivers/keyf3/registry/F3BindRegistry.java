package io.github.yunivers.keyf3.registry;

import com.mojang.serialization.Lifecycle;
import io.github.yunivers.keyf3.registry.event.F3BindRegistryEvent;
import io.github.yunivers.keyf3.registry.helper.F3Bind;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Optional;

import static io.github.yunivers.keyf3.KeyF3.NAMESPACE;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class F3BindRegistry extends SimpleRegistry<F3Bind>
{
    public static final RegistryKey<Registry<F3Bind>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("f3binds"));
    public static final F3BindRegistry INSTANCE = Registries.create(KEY, new F3BindRegistry(), registry -> null, Lifecycle.experimental());

    public F3BindRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }

    public static F3Bind bind(String name) {
        Optional<F3Bind> bind = F3BindRegistry.INSTANCE.getOrEmpty(Identifier.of(name));
        return bind.orElse(null);
    }

    public static F3Bind[] getBinds()
    {
        ArrayList<F3Bind> blocks = new ArrayList<>();

        for (F3Bind bind : INSTANCE)
            blocks.add(bind);

        return blocks.toArray(new F3Bind[0]);
    }

    public static boolean hasBind(int key)
    {
        for (F3Bind bind : INSTANCE)
            if (bind.key == key)
                return true;

        return false;
    }

    public static Identifier getBindIdentifier(F3Bind bind)
    {
        return INSTANCE.getId(bind);
    }

    @EventListener
    public static void registerF3Binds(F3BindRegistryEvent event)
    {
        event.register(NAMESPACE)
            .accept("f3bind_showBinds", new F3Bind("Show this list", Keyboard.KEY_Q, "Key bindings:"));
    }
}
