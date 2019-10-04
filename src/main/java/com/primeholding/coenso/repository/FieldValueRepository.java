package com.primeholding.coenso.repository;

import com.primeholding.coenso.entity.FieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FieldValueRepository extends JpaRepository<FieldValue, Integer>, JpaSpecificationExecutor<FieldValue> {
    Optional<FieldValue> findByValueAndFieldId(String value, Integer id);
}
