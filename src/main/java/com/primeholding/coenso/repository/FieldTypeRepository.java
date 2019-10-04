package com.primeholding.coenso.repository;

import com.primeholding.coenso.entity.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FieldTypeRepository extends JpaRepository<FieldType, Integer>, JpaSpecificationExecutor<FieldType> {
}
