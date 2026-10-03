package com.radik;

import com.mojang.logging.LogUtils;
import com.radik.client.ClientInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InitScanner {
    private static final Logger LOGGER = LogUtils.getLogger();

    public enum Dist { COMMON, CLIENT }

    public static void scanAll(String packageName, Dist side) {
        try {
            List<Class<?>> classes = getClasses(packageName);
            LOGGER.info("InitScanner: Найдено {} классов в пакете {}", classes.size(), packageName);
            for (Class<?> clazz : classes) {
                invokeMethods(clazz, side);
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка при сканировании пакета {}", packageName, e);
        }
    }

    private static void invokeMethods(Class<?> clazz, Dist side) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            if (clazz.isAnnotationPresent(Environment.class)) {
                Environment env = clazz.getAnnotation(Environment.class);
                if (env == null) return;
                if (env.value() == EnvType.CLIENT) return;
            }
        }

        try {
            for (Method method : clazz.getDeclaredMethods()) {
                boolean isMain = (side == Dist.COMMON && method.isAnnotationPresent(MainInit.class));
                boolean isClient = (side == Dist.CLIENT && method.isAnnotationPresent(ClientInit.class));

                if (isMain || isClient) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        method.setAccessible(true);
                        method.invoke(null);
                        LOGGER.info("Инициализирован метод: {}.{}", clazz.getSimpleName(), method.getName());
                    } else {
                        LOGGER.warn("Метод {}.{} пропущен: он должен быть static!", clazz.getSimpleName(), method.getName());
                    }
                }
            }
        } catch (Throwable t) {
            LOGGER.debug("Не удалось просканировать методы класса {}", clazz.getName());
        }
    }

    private static @NotNull List<Class<?>> getClasses(@NotNull String packageName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        List<Class<?>> classes = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            try {
                if ("file".equals(protocol)) {
                    dirs.add(new File(resource.toURI()));
                } else if ("jar".equals(protocol)) {
                    String resourcePath = resource.getPath();
                    String jarPathString = resourcePath.substring(0, resourcePath.indexOf("!"));
                    URI jarUri = new URI(jarPathString);
                    File jarFile = new File(jarUri);

                    try (JarFile jar = new JarFile(jarFile)) {
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            String name = entries.nextElement().getName();
                            if (name.startsWith(path) && name.endsWith(".class")) {
                                String className = name.replace('/', '.').substring(0, name.length() - 6);
                                try {
                                    classes.add(Class.forName(className));
                                } catch (Throwable ignored) {}
                            }
                        }
                    }
                }
            } catch (URISyntaxException e) {
                LOGGER.error("Ошибка синтаксиса пути URL: {}", resource, e);
            }
        }

        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }


    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) return classes;
        File[] files = directory.listFiles();
        if (files == null) return classes;

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                try {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                } catch (Throwable ignored) {}
            }
        }
        return classes;
    }
}
