package dev.vankka.placeholdersplus.common.plugin.module;

import dev.vankka.placeholdersplus.common.plugin.PlaceholdersPlusPlugin;

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

    private final List<InternalModule> modules = new ArrayList<>();
    private final PlaceholdersPlusPlugin plugin;
    private final ClassLoader classLoader;
    private final File modulesFolder;
    private final File downloadsFile;

    public ModuleManager(PlaceholdersPlusPlugin plugin, ClassLoader classLoader) {
        this.plugin = plugin;
        this.classLoader = classLoader;
        this.modulesFolder = new File(plugin.getDataFolder(), "modules");
        this.downloadsFile = new File(plugin.getDataFolder(), "downloaded_modules.json");

        try {
            if (!plugin.getDataFolder().exists()) {
                Files.createDirectory(plugin.getDataFolder().toPath());
            }

            if (!modulesFolder.exists()) {
                Files.createDirectory(modulesFolder.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadModules();
        enableAll();
    }

    public List<InternalModule> getModules() {
        return modules;
    }

    public File getModulesFolder() {
        return modulesFolder;
    }

    public File getDownloadsFile() {
        return downloadsFile;
    }

    private void loadModules() {
        for (File module : Objects.requireNonNull(modulesFolder.listFiles())) {
            if (!module.isFile() || !module.getName().endsWith(".jar")) {
                continue;
            }

            InternalModule internalModule = loadModule(module);
            if (internalModule != null) {
                modules.add(internalModule);
            }
        }
    }

    public InternalModule loadModule(File file) {
        if (file == null || !file.exists()) {
            plugin.logger().error("Cannot load a null or non-existent file: " + (file != null ? file.getPath() : "null"));
            return null;
        }

        if (modules.stream().anyMatch(module -> module.getFile().getName().equalsIgnoreCase(file.getName()))) {
            plugin.logger().error("Module " + file.getName() + " is already loaded");
            return null;
        }

        Class<?> clazz = null;

        try {
            URL url = file.toURI().toURL();
            try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url}, classLoader)) {
                try (JarInputStream inputStream = new JarInputStream(url.openStream())) {
                    while (true) {
                        JarEntry jarEntry = inputStream.getNextJarEntry();
                        if (jarEntry == null) {
                            break;
                        }

                        String name = jarEntry.getName();
                        if (name == null || name.isEmpty()) {
                            continue;
                        }

                        if (!name.endsWith(".class")) {
                            continue;
                        }

                        name = name.replace("/", ".");
                        name = name.substring(0, name.lastIndexOf(".class"));

                        Class<?> currentClazz = urlClassLoader.loadClass(name);
                        if (currentClazz != null && currentClazz.getSuperclass().isAssignableFrom(PlaceholdersPlusModule.class)) {
                            clazz = currentClazz;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            plugin.logger().error("Failed to load module: " + file.getName(), e);
            return null;
        } catch (NoClassDefFoundError e) {
            plugin.logger().error("Failed to load module (probably due to missing class): " + file.getName(), e);
            return null;
        } catch (ClassNotFoundException ignored) {}

        if (clazz == null) {
            plugin.logger().warning(file.getName() + " is not a valid module (no PlaceholdersPlusModule class found)");
            return null;
        }

        PlaceholdersPlusModule module = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() != 1) {
                continue;
            }

            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            try {
                module = (PlaceholdersPlusModule) constructor.newInstance(file);
                break;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                plugin.logger().error("Failed to initiate class "
                        + clazz.getName() + ", module" + file.getName(), exception);
                return null;
            }
        }

        if (module == null) {
            plugin.logger().error("No 1-argument (java.io.File) constructor in class "
                    + clazz.getName() + ", module " + file.getName());
            return null;
        }

        module.load();
        plugin.logger().info("Successfully loaded module: " + file.getName());
        return new InternalModule(module, file);
    }

    public void enableAll() {
        modules.forEach(this::enable);
    }

    public void enable(InternalModule module) {
        module.enable(plugin);
    }

    public void disableAll() {
        modules.forEach(this::disable);
    }

    public void disable(InternalModule module) {
        module.disable(plugin);
    }

    public void unloadModule(InternalModule module) {
        if (module.isEnabled()) {
            disable(module);
        }
        modules.remove(module);
    }
}
