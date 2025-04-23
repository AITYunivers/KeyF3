package io.github.yunivers.keyf3.registry;

import com.mojang.serialization.Lifecycle;
import io.github.yunivers.keyf3.registry.event.F3BindRegistryEvent;
import io.github.yunivers.keyf3.registry.helper.F3Bind;
import net.fabricmc.loader.api.FabricLoader;
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
            .accept("f3bind_reloadChunks", new F3Bind("Reload chunks", Keyboard.KEY_A, "Reloading all chunks"))
            .accept("f3bind_showHitboxes", new F3Bind("Show hitboxes", Keyboard.KEY_B))
            .accept("f3bind_copyTp", new F3Bind("Copy location as /tp command, hold F3 + C to crash the game", Keyboard.KEY_C, "Copied location to clipboard"))
            .accept("f3bind_clearChat", new F3Bind("Clear chat", Keyboard.KEY_D))
            .accept("f3bind_showChunkBorders", new F3Bind("Show chunk boundaries", Keyboard.KEY_G))
            .accept("f3bind_pauseInactive", new F3Bind("Pause on lost focus", Keyboard.KEY_P))
            .accept("f3bind_showBinds", new F3Bind("Show this list", Keyboard.KEY_Q, "Key bindings:"))
            .accept("f3bind_reloadPack", new F3Bind("Reload resource packs", Keyboard.KEY_T, "Reloaded resource packs"))
            .accept("f3bind_hiddenPause", new F3Bind("Pause without pause menu (if pausing is possible)", Keyboard.KEY_ESCAPE));

        if (FabricLoader.getInstance().isModLoaded("bhcreative"))
            event.register(NAMESPACE).accept("f3bind_toggleCreative", new F3Bind("Cycle survival <-> creative", Keyboard.KEY_F4));

        // TODO: Dump dynamic textures, bc it'd be cool
    }
}
