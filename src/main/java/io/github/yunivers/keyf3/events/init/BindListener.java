package io.github.yunivers.keyf3.events.init;

import io.github.yunivers.keyf3.KeyF3;
import io.github.yunivers.keyf3.client.gui.screen.SilentPauseScreen;
import io.github.yunivers.keyf3.registry.F3BindRegistry;
import io.github.yunivers.keyf3.registry.event.F3BindCalledEvent;
import io.github.yunivers.keyf3.registry.helper.F3Bind;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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
        else if (event.id == NAMESPACE.id("f3bind_copyTp"))
        {
            NumberFormat formatter = new DecimalFormat("#0.00");
            String command =
                "tp " +
                formatter.format(event.player.x) + " " +
                formatter.format(event.player.y) + " " +
                formatter.format(event.player.z);

            if (FabricLoader.getInstance().isModLoaded("retrocommands") && event.player.world.isRemote)
                command += " " + DimensionRegistry.INSTANCE.getIdByLegacyId(event.player.world.dimension.id)
                    .orElse(DimensionRegistry.INSTANCE.getId(event.player.world.dimension.id).orElseThrow());


            StringSelection stringSelection = new StringSelection("/" + command);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
        else if (event.id == NAMESPACE.id("f3bind_clearChat"))
        {
            Minecraft.INSTANCE.inGameHud.clearChat();
        }
        else if (event.id == NAMESPACE.id("f3bind_showChunkBorders"))
        {
            KeyF3.renderChunkBorders = !KeyF3.renderChunkBorders;
            event.player.sendMessage("§e§l[Debug]: §rChunk borders: " + (KeyF3.renderChunkBorders ? "shown" : "hidden"));
        }
        else if (event.id == NAMESPACE.id("f3bind_pauseInactive"))
        {
            KeyF3.dontPauseOnInactive = !KeyF3.dontPauseOnInactive;
            event.player.sendMessage("§e§l[Debug]: §rPause on lost focus: " + (KeyF3.dontPauseOnInactive ? "disabled" : "enabled"));
        }
        else if (event.id == NAMESPACE.id("f3bind_showBinds"))
        {
            // LOL
            for (F3Bind bind : F3BindRegistry.getBinds())
                event.player.sendMessage("F3 + " + (bind.key == Keyboard.KEY_ESCAPE ? "Esc" : Keyboard.getKeyName(bind.key)) + " = " + bind.name);
        }
        else if (event.id == NAMESPACE.id("f3bind_reloadPack"))
        {
            Minecraft.INSTANCE.textureManager.reload();
        }
        else if (event.id == NAMESPACE.id("f3bind_hiddenPause"))
        {
            if (Minecraft.INSTANCE.currentScreen == null)
            {
                if (!event.player.world.isRemote)
                    Minecraft.INSTANCE.setScreen(new SilentPauseScreen());
                else
                    Minecraft.INSTANCE.pauseGame();
            }
        }
        else if (event.id == NAMESPACE.id("f3bind_toggleCreative"))
        {
            if (FabricLoader.getInstance().isModLoaded("bhcreative"))
            {
                boolean oldGamemode = event.player.creative_isCreative();
                event.player.creative_setCreative(!oldGamemode);
                // TODO: Verify this ⬇️ works
                if (oldGamemode == event.player.creative_isCreative())
                    event.player.sendMessage("§e§l[Debug]: §rCould not change game mode, do you have permission?");
                else if (event.player.creative_isCreative())
                    event.player.sendMessage("§e§l[Debug]: §rSet own game mode to Creative Mode");
                else
                    event.player.sendMessage("§e§l[Debug]: §rSet own game mode to Survival Mode");
            }
            else
                event.player.sendMessage("§e§l[Debug]: §rBHCreative not installed!");
        }
    }
}
