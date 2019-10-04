package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.repository.FieldTypeRepository;
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
public class FieldTypeServiceTest {

    @Mock
    private FieldTypeRepository fieldTypeRepository;

    @InjectMocks
    private FieldTypeServiceImpl fieldTypeService;

    private FieldType fieldType;

    @Before
    public void init() {
        this.fieldType = new FieldType();
        fieldType.setId(1);
        fieldType.setName("Input1");
        fieldType.setPredefined(true);

        List<FieldValue> fieldValues = new ArrayList<>();
        FieldValue fieldValue = new FieldValue();
        fieldValue.setId(1);
        fieldValue.setFieldType(fieldType);
        fieldValue.setValue("TestFieldValue");
        fieldValues.add(fieldValue);

        fieldType.setFieldValues(fieldValues);

        List<FieldType> fieldTypes = new ArrayList<>();
        fieldTypes.add(fieldType);

        when(fieldTypeRepository.findById(1)).thenReturn(Optional.of(fieldType));
        when(fieldTypeRepository.findAll()).thenReturn(fieldTypes);
    }

    @Test
    public void getFieldTypeTestShouldReturnFieldType() {
        Optional<FieldType> fieldTypeOptional = fieldTypeService.get(1);

        if(fieldTypeOptional.isPresent()) {
            assertEquals(fieldType, fieldTypeOptional.get());
        }
    }

    @Test
    public void getAllFieldTypesTestShouldReturnListOfFieldTypes() {
        List<FieldType> fieldTypeList = new ArrayList<>();
        fieldTypeList.add(fieldType);

        List<FieldType> fieldTypes = fieldTypeService.get();

        assertEquals(fieldTypeList, fieldTypes);
    }
}