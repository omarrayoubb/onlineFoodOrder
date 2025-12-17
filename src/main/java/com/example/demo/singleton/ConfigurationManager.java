package com.example.demo.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton Pattern Implementation for Configuration Manager
 * 
 * Ensures only one instance exists to manage application configuration,
 * preventing multiple configuration loads and ensuring consistency.
 */
public class ConfigurationManager {

    // Private static instance - the single instance of this class
    private static volatile ConfigurationManager instance;

    // Configuration storage
    private Map<String, String> configMap;

    /**
     * Private constructor to prevent instantiation from outside
     * Loads configuration on first creation
     */
    private ConfigurationManager() {
        this.configMap = new HashMap<>();
        loadConfiguration();
    }

    /**
     * Static method to get the single instance of ConfigurationManager
     * Uses double-checked locking for thread safety
     * 
     * @return The single instance of ConfigurationManager
     */
    public static ConfigurationManager getInstance() {
        // First check (no locking)
        if (instance == null) {
            // Synchronize only when instance is null
            synchronized (ConfigurationManager.class) {
                // Second check (with locking)
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    /**
     * Load configuration from various sources
     */
    private void loadConfiguration() {
        // Load from system properties
        configMap.put("app.name", System.getProperty("app.name", "demo"));
        configMap.put("db.url", System.getProperty("db.url", "jdbc:postgresql://localhost:5432/postgres"));
        configMap.put("db.username", System.getProperty("db.username", "postgres"));
        configMap.put("db.password", System.getProperty("db.password", ""));
        configMap.put("jwt.secret", System.getProperty("jwt.secret", "default-secret-key"));
        configMap.put("jwt.expiration", System.getProperty("jwt.expiration", "18000")); // 5 hours in seconds

        System.out.println("Configuration loaded successfully");
    }

    /**
     * Get a configuration value by key
     * 
     * @param key Configuration key
     * @return Configuration value, or null if not found
     */
    public String getConfig(String key) {
        return configMap.get(key);
    }

    /**
     * Get a configuration value with a default value
     * 
     * @param key          Configuration key
     * @param defaultValue Default value if key not found
     * @return Configuration value or default
     */
    public String getConfig(String key, String defaultValue) {
        return configMap.getOrDefault(key, defaultValue);
    }

    /**
     * Set a configuration value
     * 
     * @param key   Configuration key
     * @param value Configuration value
     */
    public void setConfig(String key, String value) {
        configMap.put(key, value);
    }

    /**
     * Get all configuration as a map
     * 
     * @return Copy of the configuration map
     */
    public Map<String, String> getAllConfig() {
        return new HashMap<>(configMap);
    }

    /**
     * Reload configuration
     */
    public void reloadConfiguration() {
        configMap.clear();
        loadConfiguration();
    }

    /**
     * Check if a configuration key exists
     * 
     * @param key Configuration key
     * @return true if key exists, false otherwise
     */
    public boolean hasConfig(String key) {
        return configMap.containsKey(key);
    }

    /**
     * Get database URL
     * 
     * @return Database URL
     */
    public String getDatabaseUrl() {
        return getConfig("db.url");
    }

    /**
     * Get database username
     * 
     * @return Database username
     */
    public String getDatabaseUsername() {
        return getConfig("db.username");
    }

    /**
     * Get database password
     * 
     * @return Database password
     */
    public String getDatabasePassword() {
        return getConfig("db.password");
    }

    /**
     * Get JWT secret key
     * 
     * @return JWT secret key
     */
    public String getJwtSecret() {
        return getConfig("jwt.secret");
    }

    /**
     * Get JWT expiration time in seconds
     * 
     * @return JWT expiration time
     */
    public long getJwtExpiration() {
        return Long.parseLong(getConfig("jwt.expiration", "18000"));
    }

    /**
     * Prevent cloning (another way to ensure singleton)
     * 
     * @return Never returns, throws exception
     * @throws CloneNotSupportedException Always thrown
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned");
    }

    /**
     * Prevent deserialization from creating new instances
     * 
     * @return The existing instance
     */
    protected Object readResolve() {
        return getInstance();
    }
}
