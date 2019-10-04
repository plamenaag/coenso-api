package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Field;
import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.repository.FieldValueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FieldValueServiceTest {

    @Mock
    private FieldValueRepository fieldValueRepository;

    @InjectMocks
    private FieldValueServiceImpl fieldValueService;

    private FieldValue fieldValue;

    @Before
    public void init() {
        FieldType fieldType = new FieldType();
        fieldType.setId(1);
        fieldType.setName("TestFieldType");
        fieldType.setPredefined(true);

        Field field = new Field();
        field.setId(1);
        field.setName("Name");
        field.setOrder(1);
        field.setIsRequired(true);

        fieldValue = new FieldValue();
        fieldValue.setId(1);
        fieldValue.setValue("TestFieldValue");
        fieldValue.setField(field);
        fieldValue.setFieldType(fieldType);

        List<FieldValue> fieldValues = new ArrayList<>();
        fieldValues.add(fieldValue);
        field.setFieldValues(fieldValues);

        when(fieldValueRepository.findById(1)).thenReturn(Optional.of(fieldValue));
        when(fieldValueRepository.findAll()).thenReturn(fieldValues);
        when(fieldValueRepository.save(fieldValue)).thenReturn(fieldValue);
        when(fieldValueRepository.findByValueAndFieldId(fieldValue.getValue(), field.getId())).thenReturn(Optional.of(fieldValue));
    }

    @Test
    public void createFieldValueTestShouldReturnCreatedFieldValue() {
        Optional<FieldValue> createdFieldValue = fieldValueService.create(fieldValue);

        if (createdFieldValue.isPresent()) {
            assertEquals(fieldValue, createdFieldValue.get());
        }
    }

    @Test
    public void updateFieldValueTestShouldReturnUpdatedFieldValue() {
        fieldValue.setValue("NewFieldValue");
        Optional<FieldValue> updatedFieldValue = fieldValueService.update(fieldValue);

        if (updatedFieldValue.isPresent()) {
            assertEquals("NewFieldValue", updatedFieldValue.get().getValue());
        }
    }

    @Test
    public void getFieldTestShouldReturnFieldValue() {
        Optional<FieldValue> fieldValueOptional = fieldValueService.get(1);

        if (fieldValueOptional.isPresent()) {
            assertEquals(fieldValue, fieldValueOptional.get());
        }
    }

    @Test
    public void getAllFieldValuesTestShouldReturnListOfFieldValues() {
        List<FieldValue> fieldValueList = new ArrayList<>();
        fieldValueList.add(fieldValue);

        List<FieldValue> fieldValues = fieldValueService.get();

        assertEquals(fieldValueList, fieldValues);
    }
}