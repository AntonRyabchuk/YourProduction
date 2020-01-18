package com.yourproduction.entities;

import java.util.List;

public class ComponentGroup{
    private Integer id;
    private Integer parentId;
    private String  name;

    // CONSTRUCTORS

    public ComponentGroup() {}

    public ComponentGroup(Integer parentId, String name) {
        this.parentId = parentId;
        this.name = name;
    }

    public ComponentGroup(Integer id, Integer parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    // GETTERS AND SETTERS

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

    public void changeParentGroup(Integer groupId) {
        this.parentId = groupId;
    }

    @Override
    public String toString() {
        return "ComponentGroup{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }
}
