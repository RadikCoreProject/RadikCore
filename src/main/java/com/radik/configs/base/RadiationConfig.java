package com.radik.configs.base;

import com.google.common.reflect.TypeToken;
import com.radik.Radik;
import com.radik.configs.BaseConfig;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RadiationConfig {
    private static RadiationConfig instance;
    private final String name = "radiation.json";
    private Map<Integer, List<String>> levels = new ConcurrentHashMap<>();
    private List<String> defaultMessages = new ArrayList<>();
    private List<String> deadDefaultMessages = new ArrayList<>();

    private RadiationConfig() {
        load();
    }

    public static RadiationConfig getInstance() {
        if (instance == null) instance = new RadiationConfig();
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void load() {
        Type dataType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> raw = BaseConfig.register(name, dataType, HashMap::new);

        Object def = raw.get("default");
        if (def instanceof List) defaultMessages = new ArrayList<>((List<String>) def);
        else defaultMessages = List.of("☢ Радиация усиливается! ☢");

        Object defs = raw.get("default_1");
        if (defs instanceof List) deadDefaultMessages = new ArrayList<>((List<String>) defs);
        else deadDefaultMessages = List.of("☢ Радиация усиливается! ☢");

        Object levelsObj = raw.get("levels");
        if (levelsObj instanceof Map) {
            Map<String, List<String>> rawLevels = (Map<String, List<String>>) levelsObj;
            levels.clear();
            for (Map.Entry<String, List<String>> entry : rawLevels.entrySet()) {
                try {
                    int level = Integer.parseInt(entry.getKey()) - 1;
                    levels.put(level, new ArrayList<>(entry.getValue()));
                } catch (NumberFormatException e) {
                    Radik.LOGGER.warn("Invalid radiation level key: {}", entry.getKey());
                }
            }
        } else levels = new ConcurrentHashMap<>();
    }

    public void reload() {
        load();
    }

    @NotNull
    public String getRandomMessage(int level) {
        List<String> list = levels.get(level);
        if (list == null || list.isEmpty()) {
            if (defaultMessages.isEmpty()) return "☢ Радиационный уровень " + level + " ☢";
            return defaultMessages.get(new Random().nextInt(defaultMessages.size()));
        }
        return list.get(new Random().nextInt(list.size()));
    }

    public String getRandomDefaultMessage() {
        if (defaultMessages.isEmpty()) {
            return "Я чувствую себя нехорошо...";
        }
        return defaultMessages.get(new Random().nextInt(defaultMessages.size()));
    }

    public String getRandomDefault1Message() {
        if (deadDefaultMessages.isEmpty()) {
            return "§4КТО Я?";
        }
        return deadDefaultMessages.get(new Random().nextInt(deadDefaultMessages.size()));
    }
}