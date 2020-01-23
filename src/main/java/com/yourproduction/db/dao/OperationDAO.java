package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.ComponentItem;
import com.yourproduction.entities.Operation;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OperationDAO {
    private final String currentOperationTable = "current_operations";

    public static void main(String[] args) throws SQLException {
        OperationDAO operationDAO = new OperationDAO();
//
//        System.out.println("testing getAll:");
//        operationDAO.getAll().forEach(System.out::println);
//
//        System.out.println();
//
//        System.out.println("testing getById(1):");
//        Operation operation1 = operationDAO.getById(1);
//        System.out.println(operation1);
//
//        System.out.println();
//
//        System.out.println("testing update:");
//        operation1.setComplete(true);
//        operation1.setTask("более подругому");
//        System.out.println(operationDAO.update(operation1) + "  " + operation1);
//
//        System.out.println();
//
//        System.out.println("testing create:");
//        ComponentItem[] componentItems = {new ComponentItem("ff", 1, "литр", 1, 5),
//                                          new ComponentItem("ff", 1, "литр", 1, 10)};
//        Operation createdOperation = new Operation(componentItems, 1, new Date(), "дорохо бохато", false);
//        System.out.println(operationDAO.create(createdOperation));
//
//        System.out.println();

//        System.out.println("testing addCurrentComponent(Operation, Component)");
//        CurrentComponentItemDAO currentComponentItemDAO = new CurrentComponentItemDAO();
//        Operation addCurrCompOperationTest = operationDAO.getById(1);
//        ComponentItem addingComponentItem = new ComponentItem("jj", 1, "gr", 100, 5);
//        operationDAO.addCurrentComponent(addCurrCompOperationTest, addingComponentItem);


        System.out.println("testing delete(id):");
        System.out.println(operationDAO.delete(4));

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
        String sql = "SELECT * FROM " + currentOperationTable + ";";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

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

        String sql = "SELECT * FROM " + currentOperationTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getEntityById() has more than one results or no results");
                throw new SQLException();
            }

            return resultToOperation(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get operation by id in " + currentOperationTable);
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(Operation operation) {
        // изменение компонентов происходит отдельно по клику по ним
        // добавление компонента - отдельный метод add currentComponent

        String sql = "UPDATE " + currentOperationTable + " SET type_id=?, deadline=?, task=?, is_complete=? WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, operation.getOperationTypeId());
            prepStatement.setString(2, toSQLDate(operation.getOperationDeadline()));
            prepStatement.setString(3, operation.getTask());
            prepStatement.setInt(4, operation.getComplete() ? 1 : 2);
            prepStatement.setInt(5, operation.getId());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t update " + operation + " in " + currentOperationTable);
            e.printStackTrace();
        }
        return false;
    }

    public boolean addCurrentComponent(Operation operation, ComponentItem addingComponent){

        // addingComponent получит Id сайд эффектом в create(ComponentItem componentItem) в current таблице

        int compCount = operation.getComponents().length;

        // в идеале фронт не должен давать вставить более 10 компонентов
        if(compCount > 9) {
            System.err.println("Component limit reached in " + operation);
            return false;
        }

        try {
            CurrentComponentItemDAO currentComponentItemDAO = new CurrentComponentItemDAO();

            if(currentComponentItemDAO.hasName(addingComponent.getName())){
                ComponentItem currComp = currentComponentItemDAO.getByName(addingComponent.getName());
                currentComponentItemDAO.updateQuantity(currComp, addingComponent.getQuantity());
                addingComponent.setId(currComp.getId());
            } else {
                currentComponentItemDAO.create(addingComponent);
            }

            String sql = "UPDATE " + currentOperationTable + " SET components_count=?, comp" + compCount + "=? WHERE id=?;";

            try (Connection connection = Database.getConnection();
                 PreparedStatement prepStatement = connection.prepareStatement(sql)) {

                prepStatement.setInt(1, compCount + 1);
                prepStatement.setInt(2, addingComponent.getId());
                prepStatement.setInt(3, operation.getId());

                prepStatement.executeUpdate();

                return true;

            } catch (SQLException e) {
                System.err.println("Can`t addCurrentComponent " + addingComponent + " in " + currentOperationTable);
                e.printStackTrace();
            }

            return true;
        } catch (SQLException e){
            System.err.println("Can`t add " + addingComponent + " to " + operation);
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(Integer id) {
        // сначала удаляем компоненты операции, затем саму операцию
        CurrentComponentItemDAO currentComponentItemDAO = new CurrentComponentItemDAO();
        Operation operation = this.getById(id);
        int componentsCount = operation.getComponents().length;

        for(int i = componentsCount - 1; i >= 0; i--){
            currentComponentItemDAO.delete(operation.getComponents()[i].getId());
        }

        String sql = "DELETE FROM " + currentOperationTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t delete operation by id=" + id + " in " + currentOperationTable);
            e.printStackTrace();
        }
        return false;
    }

    public boolean create(Operation operation) throws SQLException {
        /* сначала сохраняем в currentComponentsTable текущие currentComponents
         т.к. непонятно, какие их индексы сохранить в currentOperationTable
         в catch убираем их обратно */

        CurrentComponentItemDAO currentComponentItemDAO = new CurrentComponentItemDAO();

        Integer[] componentsId = new Integer[operation.getComponents().length];

                // получить id компонентов
                // собрать id компонентов в массив и проапдейтить таблицу current components
        try {
            for(int i = 0; i < operation.getComponents().length; i++){
                // update
                if(currentComponentItemDAO.hasName(operation.getComponents()[i].getName())){
                    Integer quantity = operation.getComponents()[i].getQuantity();
                    operation.getComponents()[i] = currentComponentItemDAO.getByName(operation.getComponents()[i].getName());
                    currentComponentItemDAO.updateQuantity(operation.getComponents()[i], quantity);
                // create
                } else {
                    currentComponentItemDAO.create(operation.getComponents()[i]);
                }
                componentsId[i] = operation.getComponents()[i].getId();
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO " + currentOperationTable +
                //     1         2      3         4               5          6      7      8      9      10     11     12     13     14     15
                " (type_id, deadline, task, is_complete, components_count, comp0, comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp9) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            prepStatement.setInt(1, operation.getOperationTypeId());
            prepStatement.setString(2, toSQLDate(operation.getOperationDeadline()));
            prepStatement.setString(3, operation.getTask());
            prepStatement.setInt(4, operation.getComplete() ? 1 : 0);
            prepStatement.setInt(5, operation.getComponents().length);

            for(int i = 0; i < Operation.getMaximumComponents(); i++){
                if(i < operation.getComponents().length){
                    prepStatement.setInt(6+i, componentsId[i]);
                } else{
                    prepStatement.setObject(6+i, null);
                }
            }

            int affectedRows = prepStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating " + operation + " failed, no rows affected in " + currentOperationTable);
            }

            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    operation.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating " + operation + " failed, no ID obtained in " + currentOperationTable);
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("can`t create operation, return components to warehouse");
            for(int i = 0; i < operation.getComponents().length; i++){
                currentComponentItemDAO.delete(componentsId[i]);
            }
            System.err.println("can`t create operation " + operation + " in " + currentOperationTable);
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public static String toSQLDate (Date date){
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
