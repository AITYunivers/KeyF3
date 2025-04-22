package io.github.yunivers.keyf3.registry.helper;

import javax.annotation.Nullable;

public class F3Bind
{
    public final String name;
    public final int key;
    public final @Nullable String debugMessage;

    public F3Bind(String name, int key, @Nullable String debugMessage)
    {
        this.name = name;
        this.key = key;
        this.debugMessage = debugMessage;
    }

    @SuppressWarnings("unused")
    public F3Bind(String name, int key)
    {
        this(name, key, null);
    }
}
