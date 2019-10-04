package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Field;
import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.exception.TemplateFormNotFoundException;
import com.primeholding.coenso.repository.FieldRepository;
import com.primeholding.coenso.repository.TemplateFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService {

    private FieldRepository fieldRepository;
    private TemplateFormRepository templateFormRepository;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository, TemplateFormRepository templateFormRepository) {
        this.fieldRepository = fieldRepository;
        this.templateFormRepository = templateFormRepository;
    }

    @Override
    public Optional<Field> get(Integer id) {
        return fieldRepository.findById(id);
    }

    @Override
    public List<Field> get() {
        return fieldRepository.findAll();
    }

    @Override
    public Optional<Field> create(Field field) {
        if (field.getTemplateForm() == null || field.getTemplateForm().getId() == null){
            throw new TemplateFormNotFoundException("Invalid template form");
        }

        if(fieldRepository.findByNameAndTemplateFormId(field.getName(),field.getTemplateForm().getId()).isPresent()) {
            return Optional.empty();
        }

        Optional<TemplateForm> templateForm = templateFormRepository.findById(field.getTemplateForm().getId());
        if (!templateForm.isPresent()) {
            throw new TemplateFormNotFoundException("Invalid template form");
        }

        field.setOrder(templateForm.get().getFields().stream().mapToInt(Field::getOrder).max().orElse(0) + 1);

        return Optional.of(fieldRepository.save(field));

    }

    @Override
    public Optional<Field> update(Field field) {
        if (field.getName() == null || field.getName().isEmpty()) {
            return Optional.empty();
        }
        Optional<Field> fieldToUpdate = get(field.getId());

        if (!fieldToUpdate.isPresent()) {
            return Optional.empty();
        }

        Optional<Field> foundFieldByName = fieldRepository.findByNameAndTemplateFormId(field.getName(), fieldToUpdate.get().getTemplateForm().getId());
        if (foundFieldByName.isPresent() && !foundFieldByName.get().getId().equals(field.getId())) {
            return Optional.empty();
        }

            fieldToUpdate.get().setName(field.getName());
            fieldToUpdate.get().setIsRequired(field.getIsRequired());
            fieldToUpdate.get().setFieldValues(field.getFieldValues());

            return Optional.of(fieldRepository.save(fieldToUpdate.get()));
    }

    @Override
    public void delete(Integer id) {
        fieldRepository.deleteById(id);
    }
}
