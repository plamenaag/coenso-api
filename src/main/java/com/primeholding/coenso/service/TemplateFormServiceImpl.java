package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.exception.DataConflictException;
import com.primeholding.coenso.repository.TemplateFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

@Service
public class TemplateFormServiceImpl implements TemplateFormService {
    private TemplateFormRepository templateFormRepository;

    @Autowired
    public TemplateFormServiceImpl(TemplateFormRepository templateFormRepository) {
        this.templateFormRepository = templateFormRepository;
    }


    @Override
    public Optional<TemplateForm> get(Long id) {
        return templateFormRepository.findById(id);
    }

    @Override
    public Page<TemplateForm> get(Pageable pageable, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        OffsetDateTime calOffset = OffsetDateTime.now(calendar.getTimeZone().toZoneId());
        Page<TemplateForm> page = templateFormRepository.findAll(pageable);

        long offset = calOffset.getOffset().getTotalSeconds();

        for (TemplateForm templateForm : page) {
            templateForm.setCreatedAt(templateForm.getCreatedAt().plus(offset, ChronoUnit.SECONDS));
            templateForm.setUpdatedAt(templateForm.getUpdatedAt().plus(offset, ChronoUnit.SECONDS));
        }

        return page;
    }

    @Override
    public Optional<TemplateForm> create(TemplateForm templateForm) {
        Optional<TemplateForm> templateFormOptional = templateFormRepository.findByTitle(templateForm.getTitle());
        if (templateFormOptional.isPresent())
            return Optional.empty();
        return Optional.of(templateFormRepository.save(templateForm));
    }

    @Override
    public Optional<TemplateForm> update(TemplateForm templateForm) {
        Optional<TemplateForm> optionalTemplateForm = templateFormRepository.findById(templateForm.getId());
        if (optionalTemplateForm.isPresent()) {
            Optional<TemplateForm> templateFormOptional = templateFormRepository.findByTitle(templateForm.getTitle());
            if (templateFormOptional.isPresent())
                throw new DataConflictException("Template form with that title already exists");
            optionalTemplateForm.get().setTitle(templateForm.getTitle());
            return Optional.of(templateFormRepository.save(optionalTemplateForm.get()));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        templateFormRepository.deleteById(id);
    }
}
