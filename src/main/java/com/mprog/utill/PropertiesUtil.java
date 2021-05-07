package com.mprog.utill;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    public static final Properties PROPERTIES = new Properties();

    static{
        loadProperties();
    }

    private PropertiesUtil(){
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
