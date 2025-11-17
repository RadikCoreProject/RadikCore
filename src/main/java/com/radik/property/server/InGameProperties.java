package com.radik.property.server;

import com.radik.registration.IRegistry;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class InGameProperties implements IRegistry {
    private static final String FILE_PATH = "core/inGame.properties";
    private static final java.util.Properties PROPERTIES = new java.util.Properties();
    public static final HashMap<InGameProperty, String> PROPERTY = new HashMap<>();

    public static void initialize() {
        createFile();
        load();
        for (InGameProperty p : InGameProperty.values()) set(p.getId(), getDefault(p.getId()));
    }

    private static void createFile() {
        java.io.File file = new java.io.File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileOutputStream output = new FileOutputStream(FILE_PATH)) {
                    for (InGameProperty p : InGameProperty.values()) {
                        PROPERTIES.setProperty(p.getId(), p.getDef());
                    }
                    PROPERTIES.store(output, "Default properties file created");
                }
                System.out.println("FIle " + FILE_PATH + " created with default properties");
            } catch (IOException ex) {
                System.err.println("Error while creating file: " + ex.getMessage());
            }
        }
    }

    private static void load() {
        try (InputStream input = new FileInputStream(FILE_PATH)) { PROPERTIES.load(input); }
        catch (IOException ex) { System.err.println("Error: " + ex.getMessage()); }
    }

    private static String getDefault(String property) {
        String value = PROPERTIES.getProperty(property);
        if (value == null || value.trim().isEmpty()) {
            String defaultValue = InGameProperty.getProperty(property).orElse(InGameProperty.MINECART_MULTIPLIER).getDef();
            System.out.println("Property " + property + " not found. Used default propery: " + defaultValue);
            set(property, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static double get(String property) {
        return Double.parseDouble(PROPERTIES.getProperty(property));
    }

    public static void set(String property, String value) {
        PROPERTIES.setProperty(property, value);
        save();
        updateStatic(property, value);
    }

    private static void updateStatic(String property, String value) {
        PROPERTY.put(InGameProperty.getProperty(property).orElseThrow(), value);
    }

    private static void save() {
        try (FileOutputStream output = new FileOutputStream(FILE_PATH)) {
            PROPERTIES.store(output, "Updated properties");
        } catch (IOException ex) {
            System.err.println("Error while saving property: " + ex.getMessage());
        }
    }
}
