package me.vankka.placeholdersplus.common.model;

import me.vankka.placeholdersplus.hook.PlaceholderHook;

public abstract class PlaceholdersPlusModule {

    protected PlaceholderHook placeholderHook = PlaceholderHook.getInstance();

    public void load() {}
    public void enable(PlaceholdersPlusPlugin plugin) {}
    public void disable(PlaceholdersPlusPlugin plugin) {}
}
