package com.radik.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radik.Radik;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.lang.reflect.Type;

public final class BaseConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = Paths.get("core");
    private static final Map<String, Object> configs = new ConcurrentHashMap<>();

    static {
        try {Files.createDirectories(CONFIG_DIR);}
        catch (IOException e) {throw new RuntimeException("Failed to create config directory", e);}
    }

    @SuppressWarnings("unchecked")
    public static <T> T register(String name, Object typeObj, Supplier<T> defaultSupplier) {
        Path file = CONFIG_DIR.resolve(name);

        if (!Files.exists(file)) {
            T defaultConfig = defaultSupplier.get();
            save(file, defaultConfig);
            configs.put(name, defaultConfig);
            return defaultConfig;
        }

        try (var reader = Files.newBufferedReader(file)) {
            T config;
            if (typeObj instanceof Class) config = GSON.fromJson(reader, (Class<T>) typeObj);
            else config = GSON.fromJson(reader, (Type) typeObj);
            configs.put(name, config);
            return config;
        } catch (Exception e) {
            Radik.LOGGER.error("Failed to load config {}: {}", name, e.getMessage());
            T defaultConfig = defaultSupplier.get();
            configs.put(name, defaultConfig);
            return defaultConfig;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String name) {
        return (T) configs.get(name);
    }

    public static void save(String name) {
        Path file = CONFIG_DIR.resolve(name);
        Object config = configs.get(name);
        if (config != null) {
            save(file, config);
        }
    }

    public static void save(String name, Object config) {
        Path file = CONFIG_DIR.resolve(name);
        configs.put(name, config);
        save(file, config);
    }

    private static void save(Path file, Object config) {
        try (var writer = Files.newBufferedWriter(file)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            Radik.LOGGER.error("Failed to save config {}: {}", file.getFileName(), e.getMessage());
        }
    }
}
