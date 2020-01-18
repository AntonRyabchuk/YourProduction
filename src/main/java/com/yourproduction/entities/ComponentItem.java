package com.yourproduction.entities;

public class ComponentItem{
    private Integer id;
    private String  name;
    private Integer groupId;
    private String  measureUnit;
    private Integer quantity;
    private Integer pricePerUnit;

    // CONSTRUCTORS

    public ComponentItem() {
    }

    public ComponentItem(String name, Integer groupId, String measureUnit, Integer quantity, Integer pricePerUnit) {
        this.name = name;
        this.groupId = groupId;
        this.measureUnit = measureUnit;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public ComponentItem(Integer id, String name, Integer groupId, String measureUnit, Integer quantity, Integer pricePerUnit) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    @Override
    public String toString() {
        return "ComponentItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupId=" + groupId +
                ", measureUnit='" + measureUnit + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
