package me.vankka.placeholdersplus.plugin.sponge;

import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.logger.SLF4JLoggerWrapper;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusPlugin;
import me.vankka.placeholdersplus.common.module.ModuleManager;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.sponge.replacers.PlayerReplacer;
import me.vankka.placeholdersplus.plugin.sponge.replacers.UserReplacer;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.TimeUnit;

@Plugin(id = "placeholdersplus", name = "PlaceholdersPlus", authors = {"Vankka"})
public class PlaceholdersPlusSponge implements PlaceholdersPlusPlugin {

    @Inject
    private Game game;
    private ModuleManager moduleManager;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File dataFolder;

    @Inject
    private org.slf4j.Logger slf4jLogger;
    private Logger logger;

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        logger = new SLF4JLoggerWrapper(this.slf4jLogger);
        logger.setHeader("[Sponge] ");

        PlaceholderHook placeholderHook = PlaceholderHook.createInstance(logger);

        // Replacers
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer(),
                new UserReplacer()
        );

        game.getScheduler().createSyncExecutor(this).schedule(() -> moduleManager =
                new ModuleManager(this, getClass().getClassLoader()), 0L, TimeUnit.SECONDS);
    }

    @Listener
    public void onGameStoppingServer(GameStoppingServerEvent event) {
        onDisable();
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
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
