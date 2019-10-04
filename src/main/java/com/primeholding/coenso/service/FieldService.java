package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Field;
import java.util.List;
import java.util.Optional;

public interface FieldService {
    Optional<Field> get(Integer id);

    List<Field> get();

    Optional<Field> create(Field field);

    Optional<Field> update(Field field);

    void delete(Integer id);
}
