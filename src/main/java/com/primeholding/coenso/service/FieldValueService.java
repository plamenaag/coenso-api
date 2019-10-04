package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.FieldValue;
import java.util.List;
import java.util.Optional;

public interface FieldValueService {
    Optional<FieldValue> get(Integer id);

    List<FieldValue> get();

    Optional<FieldValue> create(FieldValue fieldValue);

    Optional<FieldValue> update(FieldValue fieldValue);

    void delete(Integer id);
}
