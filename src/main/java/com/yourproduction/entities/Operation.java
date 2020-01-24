package com.yourproduction.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class Operation {
    static final int maximumComponents = 10;

    private ComponentItem[] components;
    private Integer id;
    private Integer operationTypeId;
    private Date    operationDeadline;
    private String  task;
    private Boolean isComplete;

    // CONSTRUCTORS

    public Operation() {
    }

    public Operation(ComponentItem[] components, Integer operationTypeId, Date operationDeadline, String task, Boolean isComplete) {
        this.components = components;
        this.operationTypeId = operationTypeId;
        this.operationDeadline = operationDeadline;
        this.task = task;
        this.isComplete = isComplete;
    }

    public Operation(ComponentItem[] components, Integer id, Integer operationTypeId, Date operationDeadline, String task, Boolean isComplete) {
        this.components = components;
        this.id = id;
        this.operationTypeId = operationTypeId;
        this.operationDeadline = operationDeadline;
        this.task = task;
        this.isComplete = isComplete;
    }

    // GETTERS AND SETTERS

    public static int getMaximumComponents() {
        return maximumComponents;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ComponentItem[] getComponents() {
        return components;
    }

    public void setComponents(ComponentItem[] components) {
        this.components = components;
    }

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public Date getOperationDeadline() {
        return operationDeadline;
    }

    public void setOperationDeadline(Date operationDeadline) {
        this.operationDeadline = operationDeadline;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    // METHODS

    public Integer getPrice() {
        Integer summ = 0;
        for(ComponentItem component : components){
            summ += component.getPrice();
        }
        return summ;
    }

    @Override
    public String toString() {
        String compStrings = "";
        for (ComponentItem componentItem: this.components) {
            compStrings = compStrings + "\n    " + componentItem.toString();
        }
        return "Operation{" +
                "id=" + id +
                ", operationTypeId=" + operationTypeId +
                ", operationDeadline=" + operationDeadline +
                ", task='" + task + '\'' +
                ", isComplete=" + isComplete +
                ", \ncomponents=" + compStrings +
                '}';
    }

    public boolean addComponentItem(ComponentItem componentItem){
        //сначала дао, потом этот метод
        int componentsCount = this.getComponents().length;
        if(componentsCount > 9){
            return false;
        }
        this.getComponents()[componentsCount] = componentItem;
        return true;
    }
}
