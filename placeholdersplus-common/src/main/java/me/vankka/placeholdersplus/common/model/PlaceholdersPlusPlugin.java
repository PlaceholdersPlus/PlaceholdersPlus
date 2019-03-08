package me.vankka.placeholdersplus.common.model;

import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.module.ModuleManager;

import java.io.File;

public interface PlaceholdersPlusPlugin {

    ModuleManager getModuleManager();
    File getDataFolder();
    Logger logger();

    default void onDisable() {
        getModuleManager().disableAll();
    }
}
