package com.yourproduction.entities;

import java.util.Date;
import java.util.List;

public class Order {
    private List<Operation> operationList;
    private Integer id;
    private String  manager;
    private String  client;
    private String  comments;
    private Date    created;
    private Date    orderDeadline;


    public Integer getPrice() {
        Integer summ = 0;
        for (Operation operation : operationList){
            summ += operation.getPrice();
        }
        return summ;
    }
}
