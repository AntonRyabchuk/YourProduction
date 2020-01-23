//package com.yourproduction.db.dao;
//
//import com.yourproduction.db.Database;
//import com.yourproduction.entities.Operation;
//import com.yourproduction.entities.Order;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//public class OrderDAO {
//
//    private final String orderTable = "current_orders";
//
//    public static void main(String[] args) {
//        OrderDAO orderDAO = new OrderDAO();
////        orderDAO.getAll().forEach(System.out::println);
////
////        System.out.println("testing getEntityById():");
////        System.out.println(orderDAO.getById(1));
//
////        System.out.println("testing delete(id):");
////        System.out.println(orderDAO.delete(4));
////
//        // Сначала написать создание currentComponent в бд
////        System.out.println("testing create():");
////        ComponentGroup componentGroup = new ComponentGroup(1, "ХЛеб");
////        System.out.println(orderDAO.create(componentGroup));
//
//        // Сначала прописать апдейт currentComponent, тк его придется вызвать в теле метода
////        System.out.println("testing update():");
////        System.out.println(orderDAO.update(new Operation({new})));
//    }
//
//    private static Order resultToOrder(ResultSet resultSet) throws SQLException {
//
//        Order order = new Order();
//        order.setId(resultSet.getInt("id"));
//
//        ClientDAO clientDAO = new ClientDAO();
//        order.setClient(clientDAO.getById(resultSet.getInt("client_id")));
//
//        order.setManager(resultSet.getString("manager"));
//        order.setCreated(resultSet.getDate("created"));
//        order.setOrderDeadline(resultSet.getDate("deadline"));
//        order.setComments(resultSet.getString("comments"));
//
//        OperationDAO operationDAO = new OperationDAO();
//        int operationCount = resultSet.getInt("operation_count");
//        Operation[] operations = new Operation[operationCount];
//
//        for(int i = 0; i < operationCount; i++){
//            operations[i] = operationDAO.getById(resultSet.getInt("op" + i));
//        }
//        order.setOperations(operations);
//
//        return order;
//    }
//
//    public List<Order> getAll() {
//
//        List<Order> orderList = new LinkedList<>();
//        String sql = "SELECT * FROM " + orderTable + ";";
//
//        try (Connection connection = Database.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()){
//                orderList.add(resultToOrder(resultSet));
//            }
//            return orderList;
//
//        } catch (SQLException e) {
//            System.err.println("Can`t get all orders from " + orderTable);
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Order getById(Integer id) {
//
//        String sql = "SELECT * FROM " + orderTable + " WHERE id=?;";
//
//        try (Connection connection = Database.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setInt(1, id);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            resultSet.next();
//            if(!resultSet.isLast()){
//                System.err.println("getOrderById() has more than one results or no results in " + orderTable);
//                throw new SQLException();
//            }
//
//            return resultToOrder(resultSet);
//
//        } catch (SQLException e) {
//            System.err.println("Can`t get order by id in " + orderTable);
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public boolean update(Order order) {
//        // изменение операций происходит отдельно по клику по ним
//        // добавление операции - отдельный метод add
//
//        String sql = "UPDATE " + orderTable + " SET client_id=?, manager=?, created=?, deadline=?, comments=?;";
//
//        try (Connection connection = Database.getConnection();
//             PreparedStatement prepStatement = connection.prepareStatement(sql)) {
//
//            prepStatement.setInt(1, order.getClient().getClientId());
//            prepStatement.setString(2, order.getManager());
//            prepStatement.setString(3, toSQLDate(order.getCreated()));
//            prepStatement.setString(4, toSQLDate(order.getOrderDeadline()));
//            prepStatement.setString(5, order.getComments());
//
//            prepStatement.executeUpdate();
//
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("Can`t update " + order + " in " + orderTable);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean addOperation(Order order, Operation addingOperation){
//
//        // addingOperation получит Id сайд эффектом в create(Operation operation)
//
//        int operationCount = order.getOperations().length;
//
//        // фронт не должен давать вставить более 10 операций
//        if(operationCount > 9) {
//            System.err.println("Operation limit reached in " + order);
//            return false;
//        }
//
//        try {
//            OperationDAO operationDAO = new OperationDAO();
//            operationDAO.create(addingOperation);
//
//            order.getOperations()[operationCount] = addingOperation;
//            return true;
//        } catch (SQLException e){
//            System.err.println("Can`t add " + addingOperation + " to " + order);
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    public boolean delete(Integer id) {
//        // сначала удаляем операции, затем order
//        OperationDAO operationDAO = new OperationDAO();
//        Order order = this.getById(id);
//        int operationCount = order.getOperations().length;
//        Integer[] operationsId = new Integer[operationCount];
//
//        for(int i = 0; i < operationCount; i++){
//            operationDAO.delete(operationsId[i]);
//        }
//
//        String sql = "DELETE FROM " + orderTable + " WHERE id=?;";
//
//        try (Connection connection = Database.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setInt(1, id);
//
//            statement.executeUpdate();
//
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("Can`t delete order by id=" + id + " in " + orderTable);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean create(Order order) {
//        /* сначала сохраняем в current_operations текущие operations
//         т.к. непонятно, какие их индексы сохранить в orderTable
//         в finally удаляем их обратно */
//
//        OperationDAO operationDAO = new OperationDAO();
//        Integer[] operationsId = new Integer[order.getOperations().length];
//
//        try {
//            for(int i = 0; i < order.getOperations().length; i++){
//                operationDAO.create(order.getOperations()[i]);
//                operationsId[i] = order.getOperations()[i].getId();
//            }
//        }  catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        String sql = "INSERT INTO " + orderTable +
//                " (client_id, manager, operation_count, op0, op1, op2, op3, op4, op5, op6, op7, op8, op9, created, deadline, comments) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
//
//        try (Connection connection = Database.getConnection();
//             PreparedStatement prepStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//
//            prepStatement.setInt(1, order.getClient().getClientId());
//            prepStatement.setString(2, order.getManager());
//            prepStatement.setInt(3, order.getOperations().length);
//
//            for(int i = 0; i < order.getOperations().length; i++){
//                prepStatement.setInt(4+i, operationsId[i]);
//            }
//
//            prepStatement.setString(14, toSQLDate(order.getCreated()));
//            prepStatement.setString(15, toSQLDate(order.getOrderDeadline()));
//            prepStatement.setString(16, order.getComments());
//
//            int affectedRows = prepStatement.executeUpdate();
//
//            if (affectedRows == 0) {
//                throw new SQLException("Creating " + order + " failed, no rows affected in " + orderTable);
//            }
//
//            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    order.setId(generatedKeys.getInt(1));
//                }
//                else {
//                    throw new SQLException("Creating " + order + " failed, no ID obtained in " + orderTable);
//                }
//            }
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("can`t create operation " + order + " in " + orderTable);
//            e.printStackTrace();
//        } finally {
//            System.err.println("finally");
//            for(int i = 0; i < order.getOperations().length; i++){
//                operationDAO.delete(operationsId[i]);
//            }
//        }
//        return false;
//    }
//
//    public static String toSQLDate (Date date){
//        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sdf.format(date);
//    }
//}
