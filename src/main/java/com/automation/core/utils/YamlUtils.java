package com.automation.core.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Utility class for reading YAML files using SnakeYAML.
 */
public class YamlUtils {
    /**
     * Reads a YAML file and returns its contents as a Map.
     */
    public static Map<String, Object> readYamlAsMap(String filePath) throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            return yaml.load(input);
        }
    }

    /**
     * Reads a YAML file and maps it to a POJO of the given class.
     */
    public static <T> T readYamlAsPojo(String filePath, Class<T> clazz) throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml(new Constructor(clazz, new LoaderOptions()));
            return yaml.load(input);
        }
    }

    /**
     * Reads a YAML file and returns its contents as a generic Object.
     */
    public static Object readYaml(String filePath) throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            return yaml.load(input);
        }
    }

    /**
     * Gets a value from a YAML file by a dot-separated path (e.g., "parent.child.key").
     */
    public static Object getValueByPath(String filePath, String path) throws IOException {
        Map<String, Object> map = readYamlAsMap(filePath);
        if (map == null || path == null || path.isEmpty()) return null;
        String[] keys = path.split("\\.");
        Object value = map;
        for (String key : keys) {
            if (!(value instanceof Map)) return null;
            value = ((Map<?, ?>) value).get(key);
            if (value == null) return null;
        }
        return value;
    }

    /**
     * Gets a value from a YAML file by a dot-separated path and returns it as a String (or null if not found).
     */
    public static String getStringByPath(String filePath, String path) throws IOException {
        Object value = getValueByPath(filePath, path);
        return value != null ? value.toString() : null;
    }

    // Sample usage:
    // Map<String, Object> data = YamlUtils.readYamlAsMap("config.yaml");
    // MyConfig config = YamlUtils.readYamlAsPojo("config.yaml", MyConfig.class);
    // Object obj = YamlUtils.readYaml("config.yaml");
}
