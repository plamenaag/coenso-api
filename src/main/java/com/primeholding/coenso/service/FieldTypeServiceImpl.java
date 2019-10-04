package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.repository.FieldTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldTypeServiceImpl implements FieldTypeService {

    private FieldTypeRepository fieldTypeRepository;

    @Autowired
    public FieldTypeServiceImpl(FieldTypeRepository fieldTypeRepository) {
        this.fieldTypeRepository = fieldTypeRepository;
    }

    @Override
    public Optional<FieldType> get(Integer id) {
        return fieldTypeRepository.findById(id);
    }

    @Override
    public List<FieldType> get() {
        return fieldTypeRepository.findAll();
    }
}
