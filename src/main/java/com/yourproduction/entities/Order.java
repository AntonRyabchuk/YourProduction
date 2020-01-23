package com.yourproduction.entities;

import java.util.Arrays;
import java.util.Date;

public class Order {
    private static final int maximumOperations = 10;
    private Operation[] operations;
    private Integer id;
    private Client  client;
    private String  manager;
    private Date    created;
    private Date    orderDeadline;
    private String  comments;


    // GETTERS AND SETTERS

    public static int getMaximumOperations() {
        return maximumOperations;
    }

    public Operation[] getOperations() {
        return operations;
    }

    public void setOperations(Operation[] operations) {
        this.operations = operations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(Date orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    // METHODS

    public Integer getPrice() {
        Integer summ = 0;
        for (Operation operation : operations){
            summ += operation.getPrice();
        }
        return summ;
    }

    @Override
    public String toString() {
        return "Order{" +
                "operations=" + Arrays.toString(operations) +
                ", id=" + id +
                ", client=" + client +
                ", manager='" + manager + '\'' +
                ", created=" + created +
                ", orderDeadline=" + orderDeadline +
                ", comments='" + comments + '\'' +
                '}';
    }

    public boolean addOperation(Operation operation){
        //сначала дао, потом этот метод ??
        int componentsCount = this.getOperations().length;
        this.getOperations()[componentsCount] = operation;
        return true;
    }
}
