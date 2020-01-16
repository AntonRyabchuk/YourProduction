package com.yourproduction.entities;

public class ComponentItem{
    private Integer id;
    private String  name;
    private String  groupName;
    private String  measureUnit;
    private Integer quantity;
    private Integer pricePerUnit;

    // CONSTRUCTORS

    public ComponentItem(Integer id, String name, String groupName, String measureUnit, Integer quantity, Integer pricePerUnit) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
        this.measureUnit = measureUnit;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    // GETTERS AND SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Integer pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    // METHODS

    public Integer getPrice() {
        return this.pricePerUnit * this.quantity;
    }

    public void remove() {
        //remove from database
        //потеребить дао класс
    }

    @Override
    public String toString() {
        return "ComponentItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupName='" + groupName + '\'' +
                ", measureUnit='" + measureUnit + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
