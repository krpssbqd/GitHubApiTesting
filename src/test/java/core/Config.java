package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("config/env.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
