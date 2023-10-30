package com.Patikadev.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnector {
    private Connection connect = null;

    public static Connection getInstance() {
        DBconnector db = new DBconnector();
        return db.connetDB();
    }

    public Connection connetDB() {
        try {
            this.connect = DriverManager.getConnection(Config.DB_URL, Config.DB_USERNAME, Config.DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.connect;
    }
}
