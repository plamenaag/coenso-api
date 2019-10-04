package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.FieldType;
import java.util.List;
import java.util.Optional;

public interface FieldTypeService {
    Optional<FieldType> get(Integer id);

    List<FieldType> get();
}
