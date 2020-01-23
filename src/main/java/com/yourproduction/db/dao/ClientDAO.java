package com.yourproduction.db.dao;

import com.yourproduction.db.Database;
import com.yourproduction.entities.Client;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ClientDAO {

    private final String clientsTable = "clients";

    public static void main(String[] args) {

        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.getAll();
        clients.forEach(System.out::println);

        System.out.println();

        System.out.println("first client getById():");
        Client firstClient = clients.get(0);
        System.out.println(clientDAO.getById(firstClient.getClientId()));

        System.out.println();

        System.out.println("testing update:");
        firstClient.setClientName("AO MMM");
        System.out.println(clientDAO.update(firstClient));
        System.out.println(firstClient);

        System.out.println();

        System.out.println("testing create:");
        Client createdClient = new Client("Dom 2");
        System.out.print(clientDAO.create(createdClient) + "  ");
        System.out.println(createdClient);

        System.out.println();

        System.out.println("testing deleteById");
        System.out.println(clientDAO.deleteById(createdClient.getClientId()));
    }

    private static Client resultToClient(ResultSet resultSet) throws SQLException {

        Client client = new Client();
        client.setClientId(resultSet.getInt("id"));
        client.setClientName(resultSet.getString("name"));

        return client;
    }

    public List<Client> getAll() {

        List<Client> clientList = new LinkedList<>();
        String sql = "SELECT * FROM " + clientsTable + ";";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                clientList.add(resultToClient(resultSet));
            }
            return clientList;

        } catch (SQLException e) {
            System.err.println("Can`t get all clients from " + clientsTable);
            e.printStackTrace();
        }
        return null;
    }

    public Client getById(Integer id) {

        String sql = "SELECT * FROM " + clientsTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if(!resultSet.isLast()){
                System.err.println("getClientById() has more than one results or no results in " + clientsTable);
                throw new SQLException();
            }

            return resultToClient(resultSet);

        } catch (SQLException e) {
            System.err.println("Can`t get client by id in " + clientsTable);
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(Client client) {

        String sql = "UPDATE " + clientsTable + " SET name=? WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, client.getClientName());
            prepStatement.setInt(2, client.getClientId());

            prepStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t update " + client + " in " + clientsTable);
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(Integer id) {

        String sql = "DELETE FROM " + clientsTable + " WHERE id=?;";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("Can`t delete client by id=" + id + " in " + clientsTable);
            e.printStackTrace();
        }
        return false;
    }

    public boolean create(Client client) {

        String sql = "INSERT INTO " + clientsTable + " (name) VALUES (?);";

        try (Connection connection = Database.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, client.getClientName());

            int affectedRows = prepStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating " + client + " failed, no rows affected in " + clientsTable);
            }

            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setClientId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating " + client + " failed, no ID obtained in " + clientsTable);
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("can`t create client " + client + " in " + clientsTable);
            e.printStackTrace();
        }
        return false;
    }
}
