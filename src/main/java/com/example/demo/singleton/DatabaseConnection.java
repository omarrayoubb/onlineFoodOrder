package com.example.demo.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton Pattern Implementation for Database Connection Pool
 * 
 * Ensures only one instance of the database connection manager exists
 * throughout the application lifecycle, preventing resource exhaustion
 * under high load.
 */
public class DatabaseConnection {

    // Private static instance - the single instance of this class
    private static volatile DatabaseConnection instance;

    // Connection pool properties
    private Connection connection;
    private String url;
    private String username;
    private String password;
    private int maxConnections = 10;
    private int activeConnections = 0;

    /**
     * Private constructor to prevent instantiation from outside
     * This ensures only one instance can be created via getInstance()
     */
    private DatabaseConnection() {
        // Initialize connection properties from system properties or defaults
        this.url = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/postgres");
        this.username = System.getProperty("db.username", "postgres");
        this.password = System.getProperty("db.password", "");

        // Initialize the connection
        initializeConnection();
    }

    /**
     * Static method to get the single instance of DatabaseConnection
     * Uses double-checked locking for thread safety
     * 
     * @return The single instance of DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        // First check (no locking)
        if (instance == null) {
            // Synchronize only when instance is null
            synchronized (DatabaseConnection.class) {
                // Second check (with locking)
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize the database connection
     */
    private void initializeConnection() {
        try {
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);

            // Create initial connection
            connection = DriverManager.getConnection(url, props);
            System.out.println("Database connection initialized successfully");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            throw new RuntimeException("Database connection initialization failed", e);
        }
    }

    /**
     * Get a database connection from the pool
     * In a real implementation, this would manage a connection pool
     * 
     * @return Database connection
     * @throws SQLException if connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeConnection();
        }
        activeConnections++;
        return connection;
    }

    /**
     * Release a connection back to the pool
     * 
     * @param conn Connection to release
     */
    public void releaseConnection(Connection conn) {
        if (conn != null) {
            activeConnections--;
            // In a real pool, we'd return the connection to the pool
            // For this singleton demo, we keep the connection open
        }
    }

    /**
     * Close the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Get the number of active connections
     * 
     * @return Number of active connections
     */
    public int getActiveConnections() {
        return activeConnections;
    }

    /**
     * Get the maximum number of connections allowed
     * 
     * @return Maximum connections
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * Set the maximum number of connections
     * 
     * @param maxConnections Maximum connections
     */
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
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
