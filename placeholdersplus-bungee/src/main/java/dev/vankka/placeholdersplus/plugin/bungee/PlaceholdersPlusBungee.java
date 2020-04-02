package dev.vankka.placeholdersplus.plugin.bungee;

import dev.vankka.placeholdersplus.common.plugin.configuration.BaseConfiguration;
import dev.vankka.placeholdersplus.common.plugin.configuration.ConfigurationManager;
import dev.vankka.placeholdersplus.common.plugin.logger.JavaUtilLoggerWrapper;
import dev.vankka.placeholdersplus.common.plugin.placeholder.PluginPlaceholderHook;
import dev.vankka.placeholdersplus.common.plugin.plugin.PluginManager;
import dev.vankka.placeholdersplus.common.plugin.scheduler.Scheduler;
import dev.vankka.placeholdersplus.hook.PlaceholderHook;
import dev.vankka.placeholdersplus.plugin.bungee.configuration.BungeeConfiguration;
import dev.vankka.placeholdersplus.plugin.bungee.plugin.BungeePluginManager;
import dev.vankka.placeholdersplus.plugin.bungee.replacers.ProxiedPlayerReplacer;
import dev.vankka.placeholdersplus.common.plugin.logger.Logger;
import dev.vankka.placeholdersplus.plugin.bungee.command.PlaceholdersPlusCommandBungee;
import dev.vankka.placeholdersplus.common.plugin.PlaceholdersPlusPlugin;
import dev.vankka.placeholdersplus.common.plugin.module.ModuleManager;
import dev.vankka.placeholdersplus.plugin.bungee.scheduler.BungeeScheduler;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class PlaceholdersPlusBungee extends Plugin implements PlaceholdersPlusPlugin {

    private Logger logger;
    private ModuleManager moduleManager;
    private PluginManager pluginManager;
    private Scheduler scheduler;
    private BungeeConfiguration configuration;

    @Override
    public void onEnable() {
        logger = new JavaUtilLoggerWrapper(getLogger());

        try {
            configuration = ConfigurationManager.configuration(this, BungeeConfiguration.class, BungeeConfiguration::new);
        } catch (IOException e) {
            logger().error("Failed to load configuration", e);
            return;
        }

        PlaceholderHook placeholderHook = PluginPlaceholderHook.createInstance(logger);
        placeholderHook.addPlaceholderReplacers(
                new ProxiedPlayerReplacer()
        );

        this.pluginManager = new BungeePluginManager(this);
        scheduler = new BungeeScheduler(this);

        getProxy().getScheduler().schedule(this, () -> {
            moduleManager = new ModuleManager(this, getClass().getClassLoader());
        }, 0L, TimeUnit.SECONDS);

        getProxy().getPluginManager().registerCommand(this, new PlaceholdersPlusCommandBungee(this));
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
        return "BungeeCord";
    }

    @Override
    public String version() {
        return getDescription().getVersion();
    }
}
