package dev.vankka.placeholdersplus.plugin.bukkit;

import dev.vankka.placeholdersplus.common.plugin.PlaceholdersPlusPlugin;
import dev.vankka.placeholdersplus.common.plugin.configuration.BaseConfiguration;
import dev.vankka.placeholdersplus.common.plugin.configuration.ConfigurationManager;
import dev.vankka.placeholdersplus.common.plugin.logger.JavaUtilLoggerWrapper;
import dev.vankka.placeholdersplus.common.plugin.logger.Logger;
import dev.vankka.placeholdersplus.common.plugin.module.ModuleManager;
import dev.vankka.placeholdersplus.common.plugin.placeholder.PluginPlaceholderHook;
import dev.vankka.placeholdersplus.common.plugin.plugin.PluginManager;
import dev.vankka.placeholdersplus.common.plugin.scheduler.Scheduler;
import dev.vankka.placeholdersplus.hook.PlaceholderHook;
import dev.vankka.placeholdersplus.plugin.bukkit.command.PlaceholdersPlusCommandBukkit;
import dev.vankka.placeholdersplus.plugin.bukkit.configuration.BukkitConfiguration;
import dev.vankka.placeholdersplus.plugin.bukkit.plugin.BukkitPluginManager;
import dev.vankka.placeholdersplus.plugin.bukkit.replacers.PlayerReplacer;
import dev.vankka.placeholdersplus.plugin.bukkit.scheduler.BukkitScheduler;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings("unused")
public class PlaceholdersPlusBukkit extends JavaPlugin implements PlaceholdersPlusPlugin {

    private Logger logger;
    private ModuleManager moduleManager;
    private PluginManager pluginManager;
    private Scheduler scheduler;
    private BukkitConfiguration configuration;

    @Override
    public void onEnable() {
        logger = new JavaUtilLoggerWrapper(getLogger());

        try {
            configuration = ConfigurationManager.configuration(this, BukkitConfiguration.class, BukkitConfiguration::new);
        } catch (IOException e) {
            logger().error("Failed to load configuration", e);
            return;
        }

        PlaceholderHook placeholderHook = PluginPlaceholderHook.createInstance(this);
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer()
        );

        pluginManager = new BukkitPluginManager(this);
        scheduler = new BukkitScheduler(this);

        getServer().getScheduler().runTaskLater(this, () -> {
            moduleManager = new ModuleManager(this, getClassLoader());
        }, 0L);

        PluginCommand pluginCommand = getCommand("placeholdersplus");
        PlaceholdersPlusCommandBukkit command = new PlaceholdersPlusCommandBukkit(this);
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }

    @Override
    public PlaceholderHook getPlaceholderHook() {
        return PluginPlaceholderHook.getInstance();
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public BaseConfiguration configuration() {
        return configuration;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public String platformName() {
        return "Bukkit";
    }

    @Override
    public String version() {
        return getDescription().getVersion();
    }
}
