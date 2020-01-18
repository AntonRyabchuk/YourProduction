package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentGroup;
import com.yourproduction.entities.ComponentItem;
import com.yourproduction.entities.Operation;

import java.awt.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class OperationDAO {
    private final String operationTypesTable = "operation_types";
    private final String currentOperationTable = "current_operations";
    private final String currentComponentsTable = "current_components";
    private final String ComponentsTable = "current_operations";

    public static void main(String[] args) {
        OperationDAO operationDAO = new OperationDAO();
        operationDAO.getAll().forEach(System.out::println);

        System.out.println("testing getEntityById():");
        System.out.println(operationDAO.getById(1));

//        System.out.println("testing delete(id):");
//        System.out.println(operationDAO.delete(4));
//
        // Сначала написать создание currentComponent в бд
//        System.out.println("testing create():");
//        ComponentGroup componentGroup = new ComponentGroup(1, "ХЛеб");
//        System.out.println(operationDAO.create(componentGroup));

        // Сначала прописать апдейт currentComponent, тк его придется вызвать в теле метода
//        System.out.println("testing update():");
//        System.out.println(operationDAO.update(new Operation({new})));
    }

    private static Operation resultToOperation(ResultSet resultSet) throws SQLException {
        Operation operation = new Operation();
        operation.setId(resultSet.getInt("id"));
        operation.setOperationTypeId(resultSet.getInt("type_id"));
        operation.setOperationDeadline(resultSet.getDate("deadline"));
        operation.setTask(resultSet.getString("task"));
        operation.setComplete(resultSet.getBoolean("is_complete"));

        CurrentComponentItemDAO currentComponentItemDAO = new CurrentComponentItemDAO();
        int componentsCount = resultSet.getInt("components_count");
        ComponentItem[] components = new ComponentItem[componentsCount];

        for(int i = 0; i < componentsCount; i++){
            components[i] = currentComponentItemDAO.getById(resultSet.getInt("comp" + i));
        }
        operation.setComponents(components);

        return operation;
    }

    public List<Operation> getAll() {

        List<Operation> operationList = new LinkedList<>();

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + currentOperationTable + ";");

            while (resultSet.next()){
                operationList.add(resultToOperation(resultSet));
            }
            return operationList;

        } catch (SQLException e) {
            System.err.println("Can`t get all operations from " + currentOperationTable);
            e.printStackTrace();
        }
        return null;
    }

    public Operation getById(Integer id) {

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + currentOperationTable + " WHERE id=" + id + ";");

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityById() has more than one results or no results");
                throw new SQLException();
            }

            return resultToOperation(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get operation group by id in " + currentOperationTable);
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
//    public boolean create(ComponentGroup componentGroup) {
//
//        String sql = "INSERT INTO " + componentGroupTableName + " (name, parent_id) VALUES (?, ?);";
//
//        try (Connection connection = Database.getConnection();
//             PreparedStatement prepStatement = connection.prepareStatement(sql)) {
//
//            prepStatement.setString(1, componentGroup.getName().toLowerCase());
//            prepStatement.setInt(2, componentGroup.getParentId());
//
//            prepStatement.executeUpdate();
//
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("can`t create component group in " + componentGroupTableName);
//            e.printStackTrace();
//        }
//        return false;
//    }
}
