package me.vankka.placeholdersplus.plugin.bungee;

import me.vankka.placeholdersplus.common.logger.JavaUtilLoggerWrapper;
import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusPlugin;
import me.vankka.placeholdersplus.common.module.ModuleManager;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.bungee.replacers.ProxiedPlayerReplacer;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public class PlaceholdersPlusBungee extends Plugin implements PlaceholdersPlusPlugin {

    private Logger logger;
    private final ModuleManager moduleManager = new ModuleManager(this);

    @Override
    public void onEnable() {
        logger = new JavaUtilLoggerWrapper(getLogger());
        logger.setHeader("[Bungee] ");

        PlaceholderHook placeholderHook = PlaceholderHook.createInstance(logger);

        // Replacers
        placeholderHook.addPlaceholderReplacers(
                new ProxiedPlayerReplacer()
        );
    }

    @Override
    public PlaceholderHook getPlaceholderHook() {
        return PlaceholderHook.getInstance();
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
