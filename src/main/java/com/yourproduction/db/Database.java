package com.yourproduction.db;

import java.sql.*;

public class Database {
    private static final String URL    = "jdbc:mysql://localhost:3306/productiondb";
    private static final String UNAME  = "root";
    private static final String UPASS  = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, UNAME, UPASS);
    }
}
