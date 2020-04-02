package dev.vankka.placeholdersplus.plugin.sponge;

import dev.vankka.placeholdersplus.common.plugin.configuration.BaseConfiguration;
import dev.vankka.placeholdersplus.common.plugin.configuration.ConfigurationManager;
import dev.vankka.placeholdersplus.common.plugin.logger.SLF4JLoggerWrapper;
import dev.vankka.placeholdersplus.common.plugin.placeholder.PluginPlaceholderHook;
import dev.vankka.placeholdersplus.common.plugin.plugin.PluginManager;
import dev.vankka.placeholdersplus.common.plugin.scheduler.Scheduler;
import dev.vankka.placeholdersplus.hook.PlaceholderHook;
import dev.vankka.placeholdersplus.common.plugin.logger.Logger;
import dev.vankka.placeholdersplus.common.plugin.PlaceholdersPlusPlugin;
import dev.vankka.placeholdersplus.common.plugin.module.ModuleManager;
import dev.vankka.placeholdersplus.plugin.sponge.command.PlaceholdersPlusCommandSponge;
import dev.vankka.placeholdersplus.plugin.sponge.configuration.SpongeConfiguration;
import dev.vankka.placeholdersplus.plugin.sponge.plugin.SpongePluginManager;
import dev.vankka.placeholdersplus.plugin.sponge.replacers.PlayerReplacer;
import dev.vankka.placeholdersplus.plugin.sponge.replacers.UserReplacer;
import dev.vankka.placeholdersplus.plugin.sponge.scheduler.SpongeScheduler;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Plugin(id = "placeholdersplus", name = "PlaceholdersPlus", authors = {"Vankka"})
public class PlaceholdersPlusSponge implements PlaceholdersPlusPlugin {

    @Inject
    private Game game;

    @Inject
    private org.slf4j.Logger slf4jLogger;

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File dataFolder;

    private Logger logger;
    private ModuleManager moduleManager;
    private PluginManager pluginManager;
    private Scheduler scheduler;
    private SpongeConfiguration configuration;

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        logger = new SLF4JLoggerWrapper(slf4jLogger);

        try {
            configuration = ConfigurationManager.configuration(this, SpongeConfiguration.class, SpongeConfiguration::new);
        } catch (IOException e) {
            logger().error("Failed to load configuration", e);
            return;
        }

        PlaceholderHook placeholderHook = PluginPlaceholderHook.createInstance(logger);
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer(),
                new UserReplacer()
        );

        pluginManager = new SpongePluginManager(this);
        scheduler = new SpongeScheduler(this);

        game.getScheduler().createSyncExecutor(this).schedule(() -> {
            moduleManager = new ModuleManager(this, getClass().getClassLoader());
        }, 0L, TimeUnit.SECONDS);

        getGame().getCommandManager().register(this, new PlaceholdersPlusCommandSponge(this), "placeholdersplus", "pplus");
    }

    @Listener
    public void onGameStoppingServer(GameStoppingServerEvent event) {
        onDisable();
    }

    public Game getGame() {
        return game;
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
        return dataFolder;
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
        return "Sponge";
    }

    @Override
    public String version() {
        return pluginContainer.getVersion().orElse("Unknown");
    }
}
