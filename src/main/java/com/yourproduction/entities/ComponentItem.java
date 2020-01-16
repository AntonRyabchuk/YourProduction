package com.yourproduction.entities;

public class ComponentItem implements Composite{
    private Integer id;
    private Integer parentId;
    private String  name;
    private String  category;
    private String  measureUnit;
    private Integer quantity;
    private Integer pricePerUnit;

    public ComponentItem(Integer id, String name, String category, String measureUnit, Integer quantity, Integer pricePerUnit) {
        this.id = id;
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    // METHODS

    public Integer getPrice() {
        return this.pricePerUnit * this.quantity;
    }

    public void remove() {
        //remove from database
        //потеребить дао класс
    }

    public void changeParentGroup(Integer groupId) {
        this.parentId = groupId;
    }
}
