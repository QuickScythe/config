package me.quickscythe;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static final Map<Class<? extends Config.Content>, Config> configs = new HashMap<>();

    public static Config registerConfig(JavaPlugin mainClass, Class<? extends Config.Content> configTemplate) {
        try {
            Config config = new Config(mainClass, configTemplate);
            configs.put(configTemplate, config);
            return config;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getConfig(Class<? extends Config.Content> clazz) {
        return configs.get(clazz);
    }
}
