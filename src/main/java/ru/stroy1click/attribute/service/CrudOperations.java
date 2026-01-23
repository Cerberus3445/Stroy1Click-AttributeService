package ru.stroy1click.attribute.service;

import java.util.List;

public interface CrudOperations<ID, T> {

    T get(ID id);

    List<T> getAll();

    T create(T dto);

    void update(ID id, T dto);

    void delete(ID id);
}
