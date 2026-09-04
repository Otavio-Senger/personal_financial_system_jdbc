package com.otaviosenger.financial.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionDatabase {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConnectionDatabase.class
                .getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Properties file not found!");
            }
            properties.load(input);
        } catch (IOException e){
            throw new RuntimeException("Error loading properties file!");
        }
    }

    public static Connection connect(){
        try {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            return DriverManager.getConnection(url, username, password);
        }catch (SQLException e){
            throw new RuntimeException("Database connection failed!", e);
        }
    }


}
