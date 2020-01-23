package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentGroup;
import com.yourproduction.entities.ComponentItem;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ComponentItemDAO {

    private static final String componentsTable = "components";

    public static void main(String[] args) throws SQLException{
        ComponentItemDAO componentItemDAO = new ComponentItemDAO();
        System.out.println("testing getAllOfGroup(group_id = 2):");
        componentItemDAO.getAllOfGroup(2).forEach(System.out::println);

        System.out.println();

        System.out.println("testing getById(3):");
        System.out.println(componentItemDAO.getById(3));

        System.out.println();

        System.out.println("testing increaseQuantity(id=3, 10):");
        System.out.println(componentItemDAO.increaseQuantityById(3,  10));
        System.out.println(componentItemDAO.getById(3));

        System.out.println();

        System.out.println("testing reduceQuantity(id=3, 5):");
        System.out.println(componentItemDAO.reduceQuantityById(3, 5));
        System.out.println(componentItemDAO.getById(3));

        System.out.println();

        System.out.println("testing getByName(FF):");
        System.out.println(componentItemDAO.getByName("FF"));

        System.out.println();

        System.out.println("testing update:");
        ComponentItem updCompItem = new ComponentItem(3, "u", 2, "литр", 25, 5);
        System.out.println(componentItemDAO.update(updCompItem));
        System.out.println(updCompItem);

        System.out.println();

        System.out.println("testing create:");
        ComponentItem createdCompItem = new ComponentItem("absNewItem", 1, "gr", 500, 5);
        System.out.println(createdCompItem);
        System.out.println(componentItemDAO.create(createdCompItem));
        System.out.println(createdCompItem);

        System.out.println();

        System.out.println("testing deleteById:");
        System.out.println(componentItemDAO.deleteById(createdCompItem.getId()));
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

    public List<ComponentItem> getAllOfGroup(Integer group_id) {

        List<ComponentItem> componentList = new LinkedList<>();
        String sql = "SELECT * FROM " + componentsTable + " WHERE group_id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, group_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                componentList.add(resultToComponentItem(resultSet));
            }
            return componentList;

        } catch (SQLException e) {
            System.err.println("Can`t get component/s by all of group from " + componentsTable);
            e.printStackTrace();
        }
        return null;
    }

    public ComponentItem getById(Integer id) {

        String sql = "SELECT * FROM " + componentsTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityById() has more than one results in " + componentsTable);
                throw new SQLException();
            }

            return resultToComponentItem(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get component by id from " + componentsTable);
            e.printStackTrace();
        }
        return null;
    }

    public ComponentItem getByName(String name) {

        String sql = "SELECT * FROM " + componentsTable + " WHERE name=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityByName() has more than one results or no one in " + componentsTable);
                throw new SQLException();
            }

            return resultToComponentItem(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get component by name from " + componentsTable);
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(ComponentItem componentItem) {

        String sql = "UPDATE " + componentsTable + " SET name=?, group_id=?, measure_unit=?, quantity=?, price_per_unit=? WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, componentItem.getName().toLowerCase());
            prepStatement.setInt(2, componentItem.getGroupId());
            prepStatement.setString(3, componentItem.getMeasureUnit());
            prepStatement.setInt(4, componentItem.getQuantity());
            prepStatement.setInt(5, componentItem.getPricePerUnit());
            prepStatement.setInt(6, componentItem.getId());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t update component in " + componentsTable);
            e.printStackTrace();
        }
        return false;
    }

    public boolean increaseQuantityById(Integer id, Integer quantity) throws SQLException {

        quantity = Math.abs(quantity);

        String sql = "UPDATE " + componentsTable + " SET quantity=quantity+? WHERE id=?";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, quantity);
            prepStatement.setInt(2, id);
            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t increase quantity by " + quantity + " for id " + id + " in " + componentsTable);
            e.printStackTrace();
            throw e;
        }
    }

    public boolean reduceQuantityById(Integer id, Integer quantity) throws SQLException {

        quantity = Math.abs(quantity);

        String sql = "UPDATE " + componentsTable + " SET quantity=quantity-? WHERE id=?";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, quantity);
            prepStatement.setInt(2, id);
            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t reduce quantity by " + quantity + " for id " + id + " in " + componentsTable);
            e.printStackTrace();
            throw e;
        }
    }

    public boolean deleteById(Integer id) {

        String sql = "DELETE FROM " + componentsTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
                PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, id);
            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t delete component by id in " + componentsTable);
            e.printStackTrace();
        }
        return false;
    }

    public boolean create(ComponentItem componentItem) {

        String sql = "INSERT INTO " + componentsTable + " (name, group_id, measure_unit, quantity, price_per_unit) VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, componentItem.getName().toLowerCase());
            prepStatement.setInt(2, componentItem.getGroupId());
            prepStatement.setString(3, componentItem.getMeasureUnit().toLowerCase());
            prepStatement.setInt(4, componentItem.getQuantity());
            prepStatement.setInt(5, componentItem.getPricePerUnit());

            int affectedRows = prepStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating " + componentItem + " failed, no rows affected.");
            }

            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    componentItem.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating " + componentItem + " failed, no ID obtained.");
                }
            }

            return true;

        } catch (SQLException e) {
            System.err.println("can`t create component in " + componentsTable);
            e.printStackTrace();
        }
        return false;
    }
}
