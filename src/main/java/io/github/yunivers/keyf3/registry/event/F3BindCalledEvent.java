package io.github.yunivers.keyf3.registry.event;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Identifier;

@SuperBuilder
public class F3BindCalledEvent extends Event
{
    public final Identifier id;
    public final PlayerEntity player;
}
