package com.yourproduction.db.dao;

import java.util.List;

public interface DAO<E, K, L> {
    List<E> getAll();
    E getEntityById(K id);
    E getEntityByName(L name);
    E update(E entity);
    boolean delete(K id);
    boolean create(E entity);
}
