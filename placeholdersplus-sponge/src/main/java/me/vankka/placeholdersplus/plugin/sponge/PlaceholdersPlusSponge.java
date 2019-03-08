package me.vankka.placeholdersplus.plugin.sponge;

import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.logger.SLF4JLoggerWrapper;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusPlugin;
import me.vankka.placeholdersplus.common.module.ModuleManager;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.sponge.replacers.PlayerReplacer;
import me.vankka.placeholdersplus.plugin.sponge.replacers.UserReplacer;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;
import java.io.File;

@Plugin(id = "placeholdersplus", name = "PlaceholdersPlus", authors = {"Vankka"})
public class PlaceholdersPlusSponge implements PlaceholdersPlusPlugin {

    private final ModuleManager moduleManager = new ModuleManager(this);

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
    }

    @Listener
    public void onGameStoppingServer(GameStoppingServerEvent event) {
        onDisable();
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
