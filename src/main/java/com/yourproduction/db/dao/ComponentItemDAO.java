package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentItem;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ComponentItemDAO {

    private static final String componentsTable = "components";

    public static void main(String[] args) {
        ComponentItemDAO componentItemDAO = new ComponentItemDAO();
        componentItemDAO.getAllOfGroup(2).forEach(System.out::println);

        System.out.println("testing getEntityById():");
        System.out.println(componentItemDAO.getById(2));

        System.out.println("testing getEntityByName():");
        System.out.println(componentItemDAO.getByName("FF"));

        System.out.println("testing delete(id):");
        System.out.println(componentItemDAO.delete(11));

        System.out.println("testing create():");
        ComponentItem componentItem = new ComponentItem("www", 2, "gr", 500, 5);
        System.out.println(componentItemDAO.create(componentItem));

        System.out.println("testing update():");
        System.out.println(componentItemDAO.update(new ComponentItem(3, "u", 2, "литр", 25, 5)));
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

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + componentsTable + " WHERE group_id=\"" + group_id + "\";");

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

        try (Connection connection = Database.getConnection();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + componentsTable + " WHERE id=" + id + ";");
            ComponentItem componentItem = new ComponentItem();

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
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + componentsTable + " WHERE name=\"" + name.toLowerCase() + "\";");

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

    public boolean delete(Integer id) {
        try (Connection connection = Database.getConnection();
                Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM " + componentsTable + " WHERE id=" + id + ";");

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
                PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, componentItem.getName().toLowerCase());
            prepStatement.setInt(2, componentItem.getGroupId());
            prepStatement.setString(3, componentItem.getMeasureUnit().toLowerCase());
            prepStatement.setInt(4, componentItem.getQuantity());
            prepStatement.setInt(5, componentItem.getPricePerUnit());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("can`t create component in " + componentsTable);
            e.printStackTrace();
        }
        return false;
    }
}
