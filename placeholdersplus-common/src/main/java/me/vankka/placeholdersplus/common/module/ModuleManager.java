package me.vankka.placeholdersplus.common.module;

import me.vankka.placeholdersplus.common.model.PlaceholdersPlusModule;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ModuleManager {

    private final PlaceholdersPlusPlugin plugin;
    private final List<PlaceholdersPlusModule> modules = new ArrayList<>();

    public ModuleManager(PlaceholdersPlusPlugin plugin) {
        this.plugin = plugin;
        loadModules();
        enableAll();
    }

    private void loadModules() {
        try {
            if (!plugin.getDataFolder().exists())
                Files.createDirectory(plugin.getDataFolder().toPath());

            File file = new File(plugin.getDataFolder(), "modules");
            if (!file.exists())
                Files.createDirectory(file.toPath());

            for (File module : Objects.requireNonNull(file.listFiles())) {
                if (!module.isFile() || !module.getName().endsWith(".jar"))
                    continue;

                loadModule(module);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean loadModule(File file) {
        List<Class<?>> classes = new ArrayList<>();

        try {
            URL url = file.toURI().toURL();
            try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url})) {
                try (JarInputStream inputStream = new JarInputStream(url.openStream())) {
                    while (true) {
                        JarEntry jarEntry = inputStream.getNextJarEntry();
                        if (jarEntry == null)
                            break;

                        String name = jarEntry.getName();
                        if (name == null || name.isEmpty())
                            continue;

                        if (!name.endsWith(".class"))
                            continue;

                        name = name.replace("/", ".");
                        name = name.substring(0, name.lastIndexOf(".class"));
                        Class<?> clazz = urlClassLoader.loadClass(name);
                        if (clazz != null && clazz.isAssignableFrom(PlaceholdersPlusModule.class))
                            classes.add(clazz);
                    }
                }
            }
        } catch (IOException exception) {
            plugin.logger().error("Failed to load module", exception);
            return false;
        } catch (ClassNotFoundException ignored) {}

        if (classes.isEmpty()) {
            plugin.logger().warning(file.getName() + " is not a valid module");
            return false;
        }

        Class<?> clazz = classes.get(0);

        PlaceholdersPlusModule module = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() > 0)
                continue;

            if (!constructor.isAccessible())
                constructor.setAccessible(true);

            try {
                module = (PlaceholdersPlusModule) constructor.newInstance();
                break;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                plugin.logger().error("Failed to initiate class "
                        + clazz.getName() + ", module" + file.getName(), exception);
                return false;
            }
        }

        if (module == null) {
            plugin.logger().error("No no argument constructor in class "
                    + clazz.getName() + ", module " + file.getName());
            return false;
        }

        module.load();
        return true;
    }

    public void enableAll() {
        modules.forEach(this::enable);
    }

    public void enable(PlaceholdersPlusModule module) {
        module.enable();
        modules.add(module);
    }

    public void disableAll() {
        modules.forEach(this::disable);
    }

    public void disable(PlaceholdersPlusModule module) {
        modules.remove(module);
        module.disable();
    }
}
