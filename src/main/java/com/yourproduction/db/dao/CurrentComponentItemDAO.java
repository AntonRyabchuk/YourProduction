package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentItem;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CurrentComponentItemDAO {

    private static final String currentComponentsTable = "current_components";

    public static void main(String[] args) throws SQLException {
        CurrentComponentItemDAO currCompItemDAO = new CurrentComponentItemDAO();

        System.out.println("testing get all:");
        List<ComponentItem> componentItemList = currCompItemDAO.getAll();
        componentItemList.forEach(System.out::println);

        System.out.println();

        System.out.println("testing getById(9):");
        ComponentItem testCompItem = currCompItemDAO.getById(9);
        System.out.println(testCompItem);

        System.out.println();

        System.out.println("testing update():");
        System.out.println(testCompItem);
        System.out.println("update quantity in object:");
        System.out.println(testCompItem);
        System.out.println(currCompItemDAO.updateQuantity(testCompItem, 10));
        System.out.println("check current components table:");
        System.out.println(currCompItemDAO.getById(testCompItem.getId()));

        System.out.println();

        System.out.println("testing create:");
        ComponentItem createdCompItem = new ComponentItem("apple", 1, "kg", 1, 1);
        System.out.println(createdCompItem);
        System.out.println(currCompItemDAO.create(createdCompItem));
        System.out.println(createdCompItem);

        System.out.println();

        System.out.println("testing delete(id):");
        System.out.println(currCompItemDAO.delete(createdCompItem.getId()));
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

    public boolean hasName(String name) throws SQLException {
        String sql = "SELECT * FROM " + currentComponentsTable + " WHERE name=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setString(1, name);

            ResultSet resultSet = prepareStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("CurrentComponentItemDAO hasName(String name)");
            e.printStackTrace();
            throw  e;
        }
    }

    public List<ComponentItem> getAll(){

        List<ComponentItem> componentItemList = new LinkedList<>();

        String sql = "SELECT * FROM " + currentComponentsTable + ";";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()){
                componentItemList.add(resultToComponentItem(resultSet));
            }

            return componentItemList;

        } catch (SQLException e) {
            System.err.println("Can`t get all component/s from " + currentComponentsTable);
            e.printStackTrace();
        }
        return null;
    }

    public ComponentItem getById(Integer id) {

        String sql = "SELECT * FROM " + currentComponentsTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setInt(1, id);

            ResultSet resultSet = prepareStatement.executeQuery();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityById() has more than one results or no results in " + currentComponentsTable);
                throw new SQLException();
            }

            return resultToComponentItem(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get current component by id from " + currentComponentsTable);
            e.printStackTrace();
        }
        return null;
    }

    public ComponentItem getByName(String name) throws SQLException {

        String sql = "SELECT * FROM " + currentComponentsTable + " WHERE name=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setString(1, name);

            ResultSet resultSet = prepareStatement.executeQuery();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getByName() has more than one results or no results in " + currentComponentsTable);
                throw new SQLException();
            }

            return resultToComponentItem(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get current component by id from " + currentComponentsTable);
            e.printStackTrace();
            throw e;
        }
    }

    public boolean updateQuantity(ComponentItem updComponentItem, Integer signedQuantity) throws SQLException {

        // Изменить можно только quantity. Замена на другой ComponentItem это delete - create
        // Сравним количество до и после в currentComponentsTable, в зависимости от результата делаем increase или reduce на складе
        // И изменяем в currentComponentsTable

        try {
            ComponentItemDAO componentItemDAO = new ComponentItemDAO();
            ComponentItem warehouseComponentItem = componentItemDAO.getByName(updComponentItem.getName());

            if(signedQuantity == 0){
                return true;
            } else if (signedQuantity > 0){
                // собираемся увеличить current, значит уменьшаем на столько же на складе
                componentItemDAO.reduceQuantityById(warehouseComponentItem.getId(), signedQuantity);
            } else {
                // уменьшаем current, значит возвращаем на склад
                componentItemDAO.increaseQuantityById(warehouseComponentItem.getId(), signedQuantity);
            }
        } catch (SQLException | NullPointerException e){
            System.err.println("can`t reduce or increase quantity in components table");
            e.printStackTrace();
            throw e;
        }

        Integer quantityInCurrent = this.getByName(updComponentItem.getName()).getQuantity();

        String sql = "UPDATE " + currentComponentsTable + " SET quantity=? WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, quantityInCurrent + signedQuantity);
            prepStatement.setInt(2, updComponentItem.getId());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t update component in " + currentComponentsTable);
            e.printStackTrace();
            throw e;
        }
    }

    public boolean delete(Integer id) {

        ComponentItem currentComponentItem = this.getById(id);

        ComponentItemDAO componentItemDAO = new ComponentItemDAO();
        ComponentItem warehouseComponentItem = componentItemDAO.getByName(currentComponentItem.getName());

        try {
            // возвращаем на склад
            if (!componentItemDAO.increaseQuantityById(warehouseComponentItem.getId(), currentComponentItem.getQuantity())){
                return false;
            }
        } catch (SQLException e){
            System.err.println("can`t increase quantity in components table");
            e.printStackTrace();
        }


        String sql = "DELETE FROM " + currentComponentsTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t delete " + currentComponentItem + " by id from " + currentComponentsTable);
            e.printStackTrace();
        }
        return false;
    }

    public boolean create(ComponentItem componentItem) throws SQLException {

        try {
            // убираем quantity components из склада
            ComponentItemDAO componentItemDAO = new ComponentItemDAO();
            ComponentItem warehouseCompItem = componentItemDAO.getByName(componentItem.getName());
            if(!componentItemDAO.reduceQuantityById(warehouseCompItem.getId(), componentItem.getQuantity())){
                // if на потом на всякий случай
                return false;
            }
        } catch (SQLException | NullPointerException e){
            System.err.println("can`t return compItem to components table");
            e.printStackTrace();
        }

        String sql = "INSERT INTO " + currentComponentsTable + " (name, group_id, measure_unit, quantity, price_per_unit) VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, componentItem.getName().toLowerCase());
            prepStatement.setInt(2, componentItem.getGroupId());
            prepStatement.setString(3, componentItem.getMeasureUnit().toLowerCase());
            prepStatement.setInt(4, componentItem.getQuantity());
            prepStatement.setInt(5, componentItem.getPricePerUnit());

            int affectedRows = prepStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating " + componentItem + " failed, no rows affected in " + currentComponentsTable);
            }

            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    componentItem.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating " + componentItem + " failed, no ID obtained in " + currentComponentsTable);
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("can`t create current component " + componentItem + " in " + currentComponentsTable);
        }
    }
}
