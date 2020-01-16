package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentItem;

import java.sql.*;
import java.util.List;

public class ComponentItemDAO implements DAO<ComponentItem, Integer, String>{


    public static void main(String[] args) {
        ComponentItemDAO componentItemDAO = new ComponentItemDAO();
        System.out.println(componentItemDAO.getEntityById(1));
    }

    @Override
    public List<ComponentItem> getAll() {
        return null;
    }

    @Override
    public ComponentItem getEntityById(Integer id) {
        Integer index = null;
        String  name = null;
        String  groupName = null;
        String  measureUnit = null;
        Integer quantity = null;
        Integer pricePerUnit = null;

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM components WHERE id=" + id + ";");

            resultSet.next();
            index = resultSet.getInt("id");
            name = resultSet.getString("name");
            groupName = resultSet.getString("group_name");
            measureUnit = resultSet.getString("measure_unit");
            quantity = resultSet.getInt("quantity");
            pricePerUnit = resultSet.getInt("price_per_unit");

            if(resultSet.next()){
                System.err.println("getEntityById() has more than one results");
                throw new SQLException();
            }

            return new ComponentItem(index, name, groupName, measureUnit, quantity, pricePerUnit);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ComponentItem getEntityByName(String name){
        return null;
    }

    @Override
    public ComponentItem update(ComponentItem entity) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean create(ComponentItem entity) {
        int index = entity.getId();
        String  name = entity.getName();
        String  groupName = entity.getGroupName();
        String  measureUnit = entity.getMeasureUnit();
        int quantity = entity.getQuantity();
        int pricePerUnit = entity.getPricePerUnit();
        String sql = "INSERT INTO components (id, name, group_name, measure_unit, quantity, price_per_unit) VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            System.out.println(index + " " + name + " " + groupName + " " + measureUnit + " " + quantity + " " + pricePerUnit);

            prepStatement.setInt(1, index);
            prepStatement.setString(2, name);
            prepStatement.setString(3, groupName);
            prepStatement.setString(4, measureUnit);
            prepStatement.setInt(5, quantity);
            prepStatement.setInt(6, pricePerUnit);

            System.out.println(prepStatement);
            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Что-то пошло не так");
            e.printStackTrace();
        }
        return false;
    }
}
