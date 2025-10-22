package com.automation.core.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Thread-safe context for sharing data between step definitions.
 * Each thread maintains its own isolated context for parallel execution.
 */
public final class ScenarioContext {

    // ThreadLocal storing a per-thread Map for context values
    private static final ThreadLocal<Map<String, Object>> THREAD_CONTEXT = ThreadLocal.withInitial(HashMap::new);

    // prevent instantiation
    private ScenarioContext() {}

    /**
     * Store data in context with a key. Key must not be null.
     */
    public static void set(String key, Object value) {
        Objects.requireNonNull(key, "key cannot be null");
        THREAD_CONTEXT.get().put(key, value);
    }

    /**
     * Put value only if absent and return the previous value (or null).
     */
    @SuppressWarnings("unused")
    public static Object putIfAbsent(String key, Object value) {
        Objects.requireNonNull(key, "key cannot be null");
        Map<String, Object> map = THREAD_CONTEXT.get();
        return map.putIfAbsent(key, value);
    }

    /**
     * Retrieve data from context by key.
     */
    public static Object get(String key) {
        Objects.requireNonNull(key, "key cannot be null");
        return THREAD_CONTEXT.get().get(key);
    }

    /**
     * Retrieve data with type casting. Returns null when value is absent.
     * Throws ClassCastException if the stored value cannot be cast to the requested type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> type) {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        Object value = THREAD_CONTEXT.get().get(key);
        if (value == null) {
            return null;
        }
        if (!type.isInstance(value)) {
            throw new ClassCastException("Value for key '" + key + "' is not of type " + type.getName()
                    + ". Actual: " + value.getClass().getName());
        }
        return (T) value;
    }

    /**
     * Convenience: get String value (calls toString() when non-null).
     */
    public static String getString(String key) {
        return Optional.ofNullable(get(key)).map(Object::toString).orElse(null);
    }

    /**
     * Convenience: get Boolean (returns null if absent). Accepts Boolean or String values.
     */
    public static Boolean getBoolean(String key) {
        Object v = get(key);
        if (v == null) return null;
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof String) return Boolean.valueOf((String) v);
        throw new ClassCastException("Value for key '" + key + "' cannot be converted to Boolean. Actual: " + v.getClass().getName());
    }

    /**
     * Convenience: get boolean with default to avoid NullPointerException from auto-unboxing.
     */
    public static boolean getBooleanOrDefault(String key, boolean defaultValue) {
        Boolean b = getBoolean(key);
        if (b == null) return defaultValue;
        return b; // return Boolean which will be unboxed; avoids explicit booleanValue() call
    }

    /**
     * Check if key exists in context.
     */
    public static boolean contains(String key) {
        Objects.requireNonNull(key, "key cannot be null");
        return THREAD_CONTEXT.get().containsKey(key);
    }

    /**
     * Remove data from context by key.
     */
    @SuppressWarnings("unused")
    public static void remove(String key) {
        Objects.requireNonNull(key, "key cannot be null");
        THREAD_CONTEXT.get().remove(key);
    }

    /**
     * Clear all data from context for current thread.
     */
    public static void clear() {
        THREAD_CONTEXT.get().clear();
    }

    /**
     * Get a copy of all context data for the current thread. The returned map is unmodifiable.
     */
    public static Map<String, Object> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(THREAD_CONTEXT.get()));
    }

    /**
     * Remove ThreadLocal to prevent memory leaks (call at the end of a scenario/thread).
     */
    public static void reset() {
        THREAD_CONTEXT.remove();
    }
}
