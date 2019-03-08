package me.vankka.placeholdersplus.common.model;

import me.vankka.placeholdersplus.hook.PlaceholderHook;

public abstract class Module {

    protected PlaceholderHook placeholderHook = PlaceholderHook.getInstance();

    public void load() {}
    public void enable() {}
    public void disable() {}
}
