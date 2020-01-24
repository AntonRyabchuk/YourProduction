package com.yourproduction.db.repository;

import com.yourproduction.entities.ComponentItem;



public interface IComponentItemRepository extends ICRUDRepository<ComponentItem, Integer> {

    ComponentItem getByName(String name);
}
