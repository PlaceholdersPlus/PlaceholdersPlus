package dev.vankka.placeholdersplus.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.vankka.placeholdersplus.common.plugin.PlaceholdersPlusPlugin;
import dev.vankka.placeholdersplus.common.plugin.configuration.ConfigurationManager;
import dev.vankka.placeholdersplus.common.plugin.logger.Logger;
import dev.vankka.placeholdersplus.common.plugin.logger.SLF4JLoggerWrapper;
import dev.vankka.placeholdersplus.common.plugin.module.ModuleManager;
import dev.vankka.placeholdersplus.common.plugin.placeholder.PluginPlaceholderHook;
import dev.vankka.placeholdersplus.common.plugin.plugin.PluginManager;
import dev.vankka.placeholdersplus.common.plugin.scheduler.Scheduler;
import dev.vankka.placeholdersplus.hook.PlaceholderHook;
import dev.vankka.placeholdersplus.plugin.velocity.command.PlaceholdersPlusCommandVelocity;
import dev.vankka.placeholdersplus.plugin.velocity.configuration.VelocityConfiguration;
import dev.vankka.placeholdersplus.plugin.velocity.plugin.VelocityPluginManager;
import dev.vankka.placeholdersplus.plugin.velocity.replacers.PlayerReplacer;
import dev.vankka.placeholdersplus.plugin.velocity.scheduler.VelocityScheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "placeholdersplus", name = "PlaceholdersPlus", authors = {"Vankka"}, version = "@VERSION@")
public class PlaceholdersPlusVelocity implements PlaceholdersPlusPlugin {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    public PluginDescription pluginDescription;

    @Inject
    private org.slf4j.Logger slf4jLogger;

    @Inject
    @DataDirectory
    private Path dataFolder;

    private Logger logger;
    private ModuleManager moduleManager;
    private PluginManager pluginManager;
    private Scheduler scheduler;
    private VelocityConfiguration configuration;

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger = new SLF4JLoggerWrapper(slf4jLogger);

        try {
            configuration = ConfigurationManager.configuration(this, VelocityConfiguration.class, VelocityConfiguration::new);
        } catch (IOException e) {
            logger().error("Failed to load configuration", e);
            return;
        }

        PlaceholderHook placeholderHook = PluginPlaceholderHook.createInstance(logger);
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer()
        );

        pluginManager = new VelocityPluginManager(this);
        scheduler = new VelocityScheduler(this);

        proxyServer.getScheduler().buildTask(this, () -> {
            moduleManager = new ModuleManager(this, getClass().getClassLoader());
        }).delay(0L, TimeUnit.SECONDS).schedule();

        getProxyServer().getCommandManager().register(new PlaceholdersPlusCommandVelocity(this), "placeholdersplus", "pplus");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        onDisable();
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
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
    public File getDataFolder() {
        return dataFolder.toFile();
    }

    @Override
    public VelocityConfiguration configuration() {
        return configuration;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public String platformName() {
        return "Velocity";
    }

    @Override
    public String version() {
        return pluginDescription.getVersion().orElse("Unknown");
    }
}
