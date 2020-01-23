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
        System.out.println("testing getAll:");
        componentGroupDAO.getAll().forEach(System.out::println);

        System.out.println();

        System.out.println("testing getById:");
        System.out.println(componentGroupDAO.getById(1));

        System.out.println();

        System.out.println("testing getByName:");
        System.out.println(componentGroupDAO.getByName("food"));

        System.out.println();

        System.out.println("testing create:");
        ComponentGroup createdCompGroup = new ComponentGroup(1, "ХЛеб");
        System.out.println(createdCompGroup);
        System.out.print(componentGroupDAO.create(createdCompGroup) + "  ");
        System.out.println(createdCompGroup);

        System.out.println();

        System.out.println("testing deleteById:");
        System.out.println(componentGroupDAO.deleteById(createdCompGroup.getId()));

        System.out.println();

        System.out.println("testing update:");
        ComponentGroup updCompGroup = new ComponentGroup(3, 1, "sugaR");
        System.out.println(componentGroupDAO.update(updCompGroup));
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
        String sql = "SELECT * FROM " + componentGroupTableName + ";";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                componentGroupList.add(resultToComponentGroup(resultSet));
            }
            return componentGroupList;

        } catch (SQLException e) {
            System.err.println("Can`t get all groups from " + componentGroupTableName);
            e.printStackTrace();
        }
        return null;
    }

    public ComponentGroup getById(Integer id) {

        String sql = "SELECT * FROM " + componentGroupTableName + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityById() has more than one results or no results in " + componentGroupTableName);
                throw new SQLException();
            }

            return resultToComponentGroup(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get component group by id from " + componentGroupTableName);
            e.printStackTrace();
        }
        return null;
    }

    public ComponentGroup getByName(String name) {

        String sql = "SELECT * FROM " + componentGroupTableName + " WHERE name=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

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

    public boolean deleteById(Integer id) {

        String sql = "DELETE FROM " + componentGroupTableName + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

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
             PreparedStatement prepStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, componentGroup.getName().toLowerCase());
            prepStatement.setInt(2, componentGroup.getParentId());

            int affectedRows = prepStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating " + componentGroup + " failed, no rows affected.");
            }

            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    componentGroup.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating " + componentGroup + " failed, no ID obtained.");
                }
            }

            return true;

        } catch (SQLException e) {
            System.err.println("can`t create component group in " + componentGroupTableName);
            e.printStackTrace();
        }
        return false;
    }
}
