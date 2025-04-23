package io.github.yunivers.keyf3;

import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("UnstableApiUsage")
public class KeyF3
{
    public static boolean debugBindPressed;
    public static boolean renderHitboxes;
    public static boolean renderChunkBorders;
    public static boolean dontPauseOnInactive;
    public static int crashTimer;
    public static final Namespace NAMESPACE = Namespace.resolve();
    public static final Logger LOGGER = NAMESPACE.getLogger();

    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }
}
