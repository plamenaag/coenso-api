package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.TemplateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Locale;
import java.util.Optional;

public interface TemplateFormService {
    Optional<TemplateForm> get(Long id);

    Page<TemplateForm> get(Pageable pageable, Locale locale);

    Optional<TemplateForm> create(TemplateForm templateForm);

    Optional<TemplateForm> update(TemplateForm templateForm);

    void delete(Long id);
}
