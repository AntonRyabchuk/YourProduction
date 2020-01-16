package com.yourproduction.db;

import java.sql.*;

public class Test {
    private static final String URL    = "jdbc:mysql://localhost:3306/productiondb";
    private static final String UNAME  = "root";
    private static final String UPASS  = "root";

    public static void main(String[] args) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL, UNAME, UPASS);
            Statement statement = connection.createStatement()) {

            // statement.execute("");   любой запрос
            ResultSet resultSet = statement.executeQuery("SELECT * FROM components;");

            while (resultSet.next()){
                int id = resultSet.getInt("component_id");
                String category = resultSet.getString("category");
                String unit = resultSet.getString("measure_unit");
                String name = resultSet.getString("name");
                int parentId = resultSet.getInt("parent_id");
                int pricePerUnit = resultSet.getInt("price_per_unit");
                int quantity = resultSet.getInt("quantity");

                System.out.println(id + " " + category + " " + unit + " " + name + " " + parentId + " " + pricePerUnit + " " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
