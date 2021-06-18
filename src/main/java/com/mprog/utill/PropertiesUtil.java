package com.mprog.utill;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {
    public static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    @SneakyThrows
    private static void loadProperties() {
        try (var resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resourceAsStream);

        }
    }

}