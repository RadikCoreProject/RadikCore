package bot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

// класс для использования radik.properties
public class Properties {
    public static String MINECRAFT_VERSION;
    public static String CLIENT_VERSION;
    public static String SERVER_VERSION;
    public static String SERVER_IP;
    public static String ALTERNATIVE_IP;
    public static String LAST_UPDATE;
    public static String RADIK_CORE_FILE;
    public static String FABRIC_API_FILE;

    private static final String FILE_PATH = "radik.properties";
    private static final java.util.Properties PROPERTIES = new java.util.Properties();
    private static boolean initialized = false; // Добавляем флаг инициализации

    public static void init() {
        if (!initialized) {
            loadProperties();
            initialized = true;
            updateVariables();
        }
    }

    private static void loadProperties() {
        try (InputStream input = new FileInputStream(FILE_PATH)) {
            PROPERTIES.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.err.println("Error loading properties file: " + ex.getMessage());
        }
    }

    private static void updateVariables() {
        MINECRAFT_VERSION = getProperty("version_minecraft");
        CLIENT_VERSION = getProperty("version_client");
        SERVER_VERSION = getProperty("version_server");
        SERVER_IP = getProperty("server_ip");
        ALTERNATIVE_IP = getProperty("alternative_ip");
        LAST_UPDATE = getProperty("last_update");
        RADIK_CORE_FILE = getProperty("radik_core");
        FABRIC_API_FILE = getProperty("fabric_api");
    }


    public static synchronized String getProperty(String property) {
        if (!initialized) {
            init();
        }
        return PROPERTIES.getProperty(property);
    }

    // задать property
    public static synchronized void setProperty(String property, String value) {
        PROPERTIES.setProperty(property, value);
        saveProperties();
        loadProperties();
        updateVariables();
        HashMap<Integer, Integer> a = new HashMap<>();
    }

    private static void saveProperties() {
        try (OutputStream output = new FileOutputStream(FILE_PATH)) {
            PROPERTIES.store(new OutputStreamWriter(output, StandardCharsets.UTF_8), null); // Явно указываем кодировку UTF-8
        } catch (IOException ex) {
            System.err.println("Error saving properties file: " + ex.getMessage());
        }
    }
}
