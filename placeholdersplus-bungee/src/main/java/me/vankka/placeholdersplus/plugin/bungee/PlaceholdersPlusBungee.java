package me.vankka.placeholdersplus.plugin.bungee;

import me.vankka.placeholdersplus.common.logger.JavaUtilLoggerWrapper;
import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.bungee.replacers.ProxiedPlayerReplacer;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public class PlaceholdersPlusBungee extends Plugin {

    @Override
    public void onEnable() {
        Logger logger = new JavaUtilLoggerWrapper(getLogger());
        logger.setHeader("[Bungee] ");

        PlaceholderHook placeholderHook = PlaceholderHook.createInstance(logger);

        // Replacers
        placeholderHook.addPlaceholderReplacers(
                new ProxiedPlayerReplacer()
        );
    }
}
