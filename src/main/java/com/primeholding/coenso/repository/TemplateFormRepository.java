package com.primeholding.coenso.repository;

import com.primeholding.coenso.entity.TemplateForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateFormRepository extends JpaRepository<TemplateForm, Long> {
    Optional<TemplateForm> findByTitle(String title);
}
