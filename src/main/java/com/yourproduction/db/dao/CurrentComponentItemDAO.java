package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentItem;

import java.sql.*;

public class CurrentComponentItemDAO {

    private static final String currentComponentsTable = "current_components";

    public static void main(String[] args) {
        CurrentComponentItemDAO currentComponentItemDAO = new CurrentComponentItemDAO();
//        componentItemDAO.getAll().forEach(System.out::println);

        System.out.println("testing getEntityById():");
        System.out.println(currentComponentItemDAO.getById(2));

//        System.out.println("testing delete(id):");
//        System.out.println(operationDAO.delete(4));

        System.out.println("testing create():");
        ComponentItem componentItem = new ComponentItem("leg", 1, "kg", 2, 50);
        System.out.println(currentComponentItemDAO.create(componentItem));

//        System.out.println("testing update():");
//        System.out.println(operationDAO.update(new Operation({new})));
    }

    private static ComponentItem resultToComponentItem(ResultSet resultSet) throws SQLException {
        ComponentItem componentItem = new ComponentItem();
        componentItem.setId(resultSet.getInt("id"));
        componentItem.setName(resultSet.getString("name"));
        componentItem.setGroupId(resultSet.getInt("group_id"));
        componentItem.setMeasureUnit(resultSet.getString("measure_unit"));
        componentItem.setQuantity(resultSet.getInt("quantity"));
        componentItem.setPricePerUnit(resultSet.getInt("price_per_unit"));
        return componentItem;
    }

    public ComponentItem getById(Integer id) {

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + currentComponentsTable + " WHERE id=" + id + ";");

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityById() has more than one results or no results");
                throw new SQLException();
            }

            return resultToComponentItem(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get component group by id");
            e.printStackTrace();
        }
        return null;
    }

//    public boolean update(ComponentGroup componentGroup) {
//
//        String sql = "UPDATE " + componentGroupTableName + " SET name=?, parent_id=? WHERE id=?;";
//
//        try (Connection connection = Database.getConnection();
//             PreparedStatement prepStatement = connection.prepareStatement(sql)) {
//
//            prepStatement.setString(1, componentGroup.getName().toLowerCase());
//            prepStatement.setInt(2, componentGroup.getParentId());
//            prepStatement.setInt(3, componentGroup.getId());
//
//            prepStatement.executeUpdate();
//
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("Can`t update component group in " + componentGroupTableName);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean delete(Integer id) {
//        try (Connection connection = Database.getConnection();
//             Statement statement = connection.createStatement()) {
//
//            statement.executeUpdate("DELETE FROM " + componentGroupTableName + " WHERE id=" + id + ";");
//
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("Can`t delete component group by id in " + componentGroupTableName);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
    public boolean create(ComponentItem componentItem) {

        String sql = "INSERT INTO " + currentComponentsTable + " (name, group_id, measure_unit, quantity, price_per_unit) VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, componentItem.getName().toLowerCase());
            prepStatement.setInt(2, componentItem.getGroupId());
            prepStatement.setString(3, componentItem.getMeasureUnit().toLowerCase());
            prepStatement.setInt(4, componentItem.getQuantity());
            prepStatement.setInt(5, componentItem.getPricePerUnit());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("can`t create current component in " + currentComponentsTable);
            e.printStackTrace();
        }
        return false;
    }
}
