package me.vankka.placeholdersplus.plugin.bukkit;

import me.vankka.placeholdersplus.common.logger.JavaUtilLoggerWrapper;
import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.bukkit.replacers.PlayerReplacer;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PlaceholdersPlusBukkit extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger logger = new JavaUtilLoggerWrapper(getLogger());
        logger.setHeader("[Bukkit] ");

        PlaceholderHook hook = PlaceholderHook.createInstance(logger);

        // Replacers
        hook.addPlaceholderReplacers(
                new PlayerReplacer()
        );
    }
}
