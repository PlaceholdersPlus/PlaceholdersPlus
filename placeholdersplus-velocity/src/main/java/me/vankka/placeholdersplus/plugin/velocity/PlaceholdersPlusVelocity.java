package me.vankka.placeholdersplus.plugin.velocity;

import com.velocitypowered.api.plugin.Plugin;
import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.logger.SLF4JLoggerWrapper;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.velocity.replacers.PlayerReplacer;

import javax.inject.Inject;

@Plugin(id = "placeholdersplus", name = "PlaceholdersPlus", authors = {"Vankka"})
public class PlaceholdersPlusVelocity {

    @Inject
    public PlaceholdersPlusVelocity(final org.slf4j.Logger slf4jLogger) {
        Logger logger = new SLF4JLoggerWrapper(slf4jLogger);
        logger.setHeader("[Velocity] ");
        PlaceholderHook placeholderHook = PlaceholderHook.createInstance(logger);

        // Replacers
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer()
        );
    }
}
