package com.yourproduction.entities;

import java.util.List;

public class ComponentGroup{
    private List<ComponentGroup> components;
    private Integer id;
    private Integer parentId;
    private String  name;

    // GETTERS AND SETTERS

    public List<ComponentGroup> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentGroup> components) {
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

    // METHODS

    public void remove() {
        //remove from bd by dao class
    }

    public void changeParentGroup(Integer groupId) {
        this.parentId = groupId;
    }
}
