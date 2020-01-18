package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentGroup;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ComponentGroupDAO {

    private final String componentGroupTableName = "component_group";

    public static void main(String[] args) {
        ComponentGroupDAO componentGroupDAO = new ComponentGroupDAO();
        componentGroupDAO.getAll().forEach(System.out::println);

        System.out.println("testing getEntityById():");
        System.out.println(componentGroupDAO.getById(1));

        System.out.println("testing getEntityByName():");
        System.out.println(componentGroupDAO.getByName("food"));

        System.out.println("testing delete(id):");
        System.out.println(componentGroupDAO.delete(4));

//        System.out.println("testing create():");
//        ComponentGroup componentGroup = new ComponentGroup(1, "ХЛеб");
//        System.out.println(componentGroupDAO.create(componentGroup));

        System.out.println("testing update():");
        System.out.println(componentGroupDAO.update(new ComponentGroup(3, 1, "CHeeSe")));
    }

    private static ComponentGroup resultToComponentGroup(ResultSet resultSet) throws SQLException {
        ComponentGroup componentGroup = new ComponentGroup();
        componentGroup.setId(resultSet.getInt("id"));
        componentGroup.setParentId(resultSet.getInt("parent_id"));
        componentGroup.setName(resultSet.getString("name"));
        return componentGroup;
    }

    public List<ComponentGroup> getAll() {

        List<ComponentGroup> componentGroupList = new LinkedList<>();

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + componentGroupTableName + ";");

            while (resultSet.next()){
                componentGroupList.add(resultToComponentGroup(resultSet));
            }
            return componentGroupList;

        } catch (SQLException e) {
            System.err.println("Can`t get all groups");
            e.printStackTrace();
        }
        return null;
    }

    public ComponentGroup getById(Integer id) {

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + componentGroupTableName + " WHERE id=" + id + ";");

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityById() has more than one results or no results");
                throw new SQLException();
            }

            return resultToComponentGroup(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get component group by id");
            e.printStackTrace();
        }
        return null;
    }

    public ComponentGroup getByName(String name) {
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + componentGroupTableName + " WHERE name=\"" + name + "\";");

            ComponentGroup componentGroup = new ComponentGroup();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityByName() has more than one results or no one");
                throw new SQLException();
            }

            return resultToComponentGroup(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get component group by name in " + componentGroupTableName);
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(ComponentGroup componentGroup) {

        String sql = "UPDATE " + componentGroupTableName + " SET name=?, parent_id=? WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, componentGroup.getName().toLowerCase());
            prepStatement.setInt(2, componentGroup.getParentId());
            prepStatement.setInt(3, componentGroup.getId());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t update component group in " + componentGroupTableName);
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Integer id) {
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM " + componentGroupTableName + " WHERE id=" + id + ";");

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t delete component group by id in " + componentGroupTableName);
            e.printStackTrace();
        }
        return false;
    }

    public boolean create(ComponentGroup componentGroup) {

        String sql = "INSERT INTO " + componentGroupTableName + " (name, parent_id) VALUES (?, ?);";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, componentGroup.getName().toLowerCase());
            prepStatement.setInt(2, componentGroup.getParentId());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("can`t create component group in " + componentGroupTableName);
            e.printStackTrace();
        }
        return false;
    }
}
