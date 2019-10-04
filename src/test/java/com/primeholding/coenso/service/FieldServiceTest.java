package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Field;
import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.repository.FieldRepository;
import com.primeholding.coenso.repository.TemplateFormRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FieldServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @InjectMocks
    private FieldServiceImpl fieldService;

    @Mock
    private TemplateFormRepository templateFormRepository;

    private Field field;

    @Before
    public void init() {
        FieldType fieldType = new FieldType();
        fieldType.setId(1);
        fieldType.setName("TestFieldType");
        fieldType.setPredefined(true);

        TemplateForm templateForm = new TemplateForm();
        templateForm.setId(1L);
        templateForm.setTitle("TestTemplateForm");
        templateForm.setFields(new ArrayList<>());

        this.field = new Field();
        field.setId(1);
        field.setName("Name");
        field.setOrder(1);
        field.setIsRequired(true);
        field.setTemplateForm(templateForm);

        List<FieldValue> fieldValues = new ArrayList<>();
        FieldValue fieldValue = new FieldValue();
        fieldValue.setId(1);
        fieldValue.setFieldType(fieldType);
        fieldValue.setField(field);
        fieldValue.setValue("TestFieldValue");
        fieldValues.add(fieldValue);

        field.setFieldValues(fieldValues);

        List<Field> fields = new ArrayList<>();
        fields.add(field);

        when(fieldRepository.findById(1)).thenReturn(Optional.of(field));
        when(fieldRepository.findAll()).thenReturn(fields);
        when(fieldRepository.save(field)).thenReturn(field);

        when(templateFormRepository.findById(1L)).thenReturn(Optional.of(templateForm));
    }

    @Test
    public void createFieldTestShouldReturnCreatedField() {
        Optional<Field> createdField = fieldService.create(field);

        if (createdField.isPresent()) {
            assertEquals(field, createdField.get());
        }
    }

    @Test
    public void updateFieldTestShouldReturnUpdatedField() {
        field.setName("NewFieldName");
        Optional<Field> updatedField = fieldService.update(field);

        if (updatedField.isPresent()) {
            assertEquals("NewFieldName", updatedField.get().getName());
        }
    }

    @Test
    public void getFieldTestShouldReturnField() {
        Optional<Field> fieldOptional = fieldService.get(1);

        if (fieldOptional.isPresent()) {
            assertEquals(field, fieldOptional.get());
        }
    }

    @Test
    public void getAllFieldsTestShouldReturnListOfFields() {
        List<Field> fieldList = new ArrayList<>();
        fieldList.add(field);

        List<Field> fields = fieldService.get();

        assertEquals(fieldList, fields);
    }
}