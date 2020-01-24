package com.yourproduction.db.repository;

import java.util.Collection;

public interface ICRUDRepository<T, ID extends Number> {

    T findById(ID id);

    Collection<T> getAll();

    T create(T entity);

    T update(T entity);

    void removeById(ID id);

    void remove(T entity);

}
