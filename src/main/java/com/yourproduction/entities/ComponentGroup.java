package com.yourproduction.entities;

import java.util.List;

public class ComponentGroup implements Composite{
    private List<Composite> components;
    private Integer id;
    private Integer parentId;
    private String  name;
    private String  category;

    // GETTERS AND SETTERS

    public List<Composite> getComponents() {
        return components;
    }

    public void setComponents(List<Composite> components) {
        this.components = components;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    // METHODS

    public void remove() {
        //remove from bd by dao class
    }

    public void changeParentGroup(Integer groupId) {
        this.parentId = groupId;
    }
}
