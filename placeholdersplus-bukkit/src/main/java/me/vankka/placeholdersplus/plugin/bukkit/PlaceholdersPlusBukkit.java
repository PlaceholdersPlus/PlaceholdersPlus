package me.vankka.placeholdersplus.plugin.bukkit;

import me.vankka.placeholdersplus.common.logger.JavaUtilLoggerWrapper;
import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusPlugin;
import me.vankka.placeholdersplus.common.module.ModuleManager;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.bukkit.replacers.PlayerReplacer;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PlaceholdersPlusBukkit extends JavaPlugin implements PlaceholdersPlusPlugin {

    private Logger logger;
    private final ModuleManager moduleManager = new ModuleManager(this);

    @Override
    public void onEnable() {
        logger = new JavaUtilLoggerWrapper(getLogger());
        logger.setHeader("[Bukkit] ");

        PlaceholderHook placeholderHook = PlaceholderHook.createInstance(logger);

        // Replacers
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer()
        );
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
