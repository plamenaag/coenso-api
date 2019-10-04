package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.repository.FieldValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldValueServiceImpl implements FieldValueService {

    private FieldValueRepository fieldValueRepository;

    @Autowired
    public FieldValueServiceImpl(FieldValueRepository fieldValueRepository) {
        this.fieldValueRepository = fieldValueRepository;
    }

    @Override
    public Optional<FieldValue> get(Integer id) {
        return fieldValueRepository.findById(id);
    }

    @Override
    public List<FieldValue> get() {
        return fieldValueRepository.findAll();
    }

    @Override
    public Optional<FieldValue> create(FieldValue fieldValue) {
        if (fieldValue.getValue() == null || fieldValue.getValue().isEmpty() || fieldValue.getFieldType() == null
                || fieldValue.getFieldType().getId() == null
                || fieldValue.getField() == null || fieldValue.getField().getId() == null
                || fieldValueRepository.findByValueAndFieldId(fieldValue.getValue(), fieldValue.getField().getId()).isPresent()) {
            return Optional.empty();
        }

        return Optional.of(fieldValueRepository.save(fieldValue));
    }

    @Override
    public Optional<FieldValue> update(FieldValue fieldValue) {

        if (fieldValue.getValue() == null || fieldValue.getValue().isEmpty()) {
            return Optional.empty();
        }

        Optional<FieldValue> fieldValueToUpdate = get(fieldValue.getId());
        if ((fieldValueToUpdate.isPresent())) {

            Optional<FieldValue> foundFieldByValue =
                    fieldValueRepository.findByValueAndFieldId(fieldValue.getValue(), fieldValueToUpdate.get().getField().getId());

            if (foundFieldByValue.isPresent() && !foundFieldByValue.get().getId().equals(fieldValue.getId())) {
                return Optional.empty();
            }


            fieldValueToUpdate.get().setValue(fieldValue.getValue());
            fieldValueToUpdate.get().setFieldType(fieldValue.getFieldType());

            return Optional.of(fieldValueRepository.save(fieldValueToUpdate.get()));
        }

        return Optional.empty();
    }

    @Override
    public void delete(Integer id) {
        fieldValueRepository.deleteById(id);
    }
}
