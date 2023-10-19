package dao.impl;

import common.Configuration;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Configuration config;

    @Inject
    public DBConnection(Configuration config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {

        Connection conn = DriverManager
                .getConnection(config.getProperty("urlDB"), config.getProperty("user_name"), config.getProperty("password"));
        return conn;
    }
}
