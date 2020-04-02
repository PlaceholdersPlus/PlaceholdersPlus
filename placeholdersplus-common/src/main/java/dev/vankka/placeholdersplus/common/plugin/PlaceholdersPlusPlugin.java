package dev.vankka.placeholdersplus.common.plugin;

import dev.vankka.placeholdersplus.common.plugin.configuration.BaseConfiguration;
import dev.vankka.placeholdersplus.common.plugin.logger.Logger;
import dev.vankka.placeholdersplus.common.plugin.module.ModuleManager;
import dev.vankka.placeholdersplus.common.plugin.plugin.PluginManager;
import dev.vankka.placeholdersplus.common.plugin.scheduler.Scheduler;
import dev.vankka.placeholdersplus.hook.PlaceholderHook;

import java.io.File;

public interface PlaceholdersPlusPlugin {

    PlaceholderHook getPlaceholderHook();
    ModuleManager getModuleManager();
    PluginManager getPluginManager();
    Scheduler getScheduler();
    File getDataFolder();
    BaseConfiguration configuration();
    Logger logger();
    String platformName();
    String version();

    default void onDisable() {
        getModuleManager().disableAll();
        getPlaceholderHook().shutdown();
    }
}
