package me.vankka.placeholdersplus.plugin.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.logger.SLF4JLoggerWrapper;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusPlugin;
import me.vankka.placeholdersplus.common.module.ModuleManager;
import me.vankka.placeholdersplus.hook.PlaceholderHook;
import me.vankka.placeholdersplus.plugin.velocity.replacers.PlayerReplacer;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "placeholdersplus", name = "PlaceholdersPlus", authors = {"Vankka"})
public class PlaceholdersPlusVelocity implements PlaceholdersPlusPlugin {

    private final File dataFolder;
    private final Logger logger;
    private ModuleManager moduleManager;

    @Inject
    public PlaceholdersPlusVelocity(final org.slf4j.Logger slf4jLogger, @DataDirectory final Path dataDirectory, final ProxyServer proxyServer) {
        logger = new SLF4JLoggerWrapper(slf4jLogger);
        logger.setHeader("[Velocity] ");
        dataFolder = dataDirectory.toFile();

        PlaceholderHook placeholderHook = PlaceholderHook.createInstance(logger);

        // Replacers
        placeholderHook.addPlaceholderReplacers(
                new PlayerReplacer()
        );

        proxyServer.getScheduler().buildTask(this,
                () -> moduleManager = new ModuleManager(this, getClass().getClassLoader()))
                .delay(0L, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
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
