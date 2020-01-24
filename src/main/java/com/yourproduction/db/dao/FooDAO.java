package com.yourproduction.db.dao;

import com.yourproduction.db.repository.IComponentItemRepository;
import com.yourproduction.entities.ComponentItem;

import java.util.Collection;

public class FooDAO implements IComponentItemRepository {


    @Override
    public ComponentItem getByName(String name) {
        return null;
    }

    @Override
    public ComponentItem findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<ComponentItem> getAll() {
        return null;
    }

    @Override
    public ComponentItem create(ComponentItem entity) {
        return null;
    }

    @Override
    public ComponentItem update(ComponentItem entity) {
        return null;
    }

    @Override
    public void removeById(Integer integer) {

    }

    @Override
    public void remove(ComponentItem entity) {

    }
}
