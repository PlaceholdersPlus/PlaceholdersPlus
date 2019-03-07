package me.vankka.placeholdersplus.plugin.sponge;

import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.logger.SLF4JLoggerWrapper;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.sponge.replacers.PlayerReplacer;
import me.vankka.placeholdersplus.plugin.sponge.replacers.UserReplacer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;

@Plugin(id = "placeholdersplus", name = "PlaceholdersPlus", authors = {"Vankka"})
public class PlaceholdersPlusSponge {

    @Inject
    private org.slf4j.Logger logger;

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        Logger logger = new SLF4JLoggerWrapper(this.logger);
        logger.setHeader("[Sponge] ");

        PlaceholderHook placeholderHook = PlaceholderHook.createInstance(logger);

        // Replacers
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer(),
                new UserReplacer()
        );
    }
}
