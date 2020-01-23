package com.yourproduction.entities;

public class Client {
    private Integer clientId;
    private String  clientName;

    // CONSTRUCTORS

    public Client() {
    }

    public Client(String clientName) {
        this.clientName = clientName;
    }

    public Client(Integer clientId, String clientName) {
        this.clientId = clientId;
        this.clientName = clientName;
    }

    // GETTERS AND SETTERS

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    // METHODS


    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}
