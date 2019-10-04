package com.primeholding.coenso.repository;

import com.primeholding.coenso.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Integer>, JpaSpecificationExecutor<Field> {
    Optional<Field> findByNameAndTemplateFormId(String name, Long templateFormId);
}
