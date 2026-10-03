package com.radik.property;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import java.io.*;

public class Property<T extends Enum<T> & PropertyEnum<T>> {
    protected String FILE_NAME;
    protected String FILE_PATH;
    protected final Class<T> enumClass;
    protected final Properties PROPERTIES = new Properties();
    public final HashMap<T, Object> PROPERTY = new HashMap<>();

    public Property(@NotNull Class<T> enumClass, String name) {
        this.FILE_NAME = name;
        this.FILE_PATH = "core/" + FILE_NAME + ".properties";
        this.enumClass = enumClass;
        createFile();
        load();
        boolean needSave = false;
        for (T p : enumClass.getEnumConstants()) {
            String val = PROPERTIES.getProperty(p.getId());
            if (val == null || val.trim().isEmpty()) {
                PROPERTIES.setProperty(p.getId(), p.getDef().toString());
                needSave = true;
            }
        }
        if (needSave) save();
        for (T p : enumClass.getEnumConstants()) {
            String raw = PROPERTIES.getProperty(p.getId());
            update(p, raw);
        }
    }

    protected void createFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileOutputStream output = new FileOutputStream(file)) {
                    for (T p : enumClass.getEnumConstants()) {
                        PROPERTIES.setProperty(p.getId(), p.getDef().toString());
                    }
                    PROPERTIES.store(output, "Default properties file created");
                }
                System.out.println("File " + FILE_NAME + " created with default properties");
            } catch (IOException ex) {
                System.err.println("Error while creating file: " + ex.getMessage());
            }
        }
    }

    protected void load() {
        try (InputStream input = new FileInputStream(FILE_PATH)) {
            PROPERTIES.load(input);
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    public String getString(T key) {
        return PROPERTY.get(key).toString();
    }

    public int getInt(T key) {
        return (int) PROPERTY.get(key);
    }

    public boolean getBoolean(T key) {
        return (boolean) PROPERTY.get(key);
    }

    public double getDouble(T key) {
        return (double) PROPERTY.get(key);
    }

    public <V> V getValue(T key, @NotNull Class<V> targetType) {
        Object val = PROPERTY.get(key);
        if (!targetType.isInstance(val)) {
            throw new ClassCastException("Value for " + key.getId() + " is " + val.getClass() + ", not " + targetType);
        }
        return targetType.cast(val);
    }

    public void set(@NotNull T key, Object value) {
        String stringValue = String.valueOf(value);
        PROPERTIES.setProperty(key.getId(), stringValue);
        save();
        update(key, stringValue);
    }

    public void set(String propertyId, Object value) {
        T key = PropertyEnum.getProperty(enumClass, propertyId)
            .orElseThrow(() -> new IllegalArgumentException("Unknown property: " + propertyId));
        set(key, value);
    }

    private void update(@NotNull T key, String stringValue) {
        Object typedValue = convertStringToType(stringValue, key.getType());
        PROPERTY.put(key, typedValue);
    }

    private Object convertStringToType(String str, Class<?> type) {
        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(str);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(str);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(str);
        } else if (type == String.class) {
            return str;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    private void save() {
        try (FileOutputStream output = new FileOutputStream(FILE_PATH)) {
            PROPERTIES.store(output, "Updated properties");
        } catch (IOException ex) {
            System.err.println("Error while saving property: " + ex.getMessage());
        }
    }
}
