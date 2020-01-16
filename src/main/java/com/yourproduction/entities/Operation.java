package com.yourproduction.entities;

import java.util.Date;
import java.util.List;

public class Operation {
    private List<ComponentItem> componentList;
    private String  type;
    private Date    operationDeadline;
    private String  comments;
    private boolean isComplete;

    public Integer getPrice() {
        Integer summ = 0;
        for(ComponentItem component : componentList){
            summ += component.getPrice();
        }
        return summ;
    }
}
