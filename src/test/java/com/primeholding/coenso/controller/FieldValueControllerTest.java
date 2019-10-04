package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Field;
import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.model.FieldValueGetModel;
import com.primeholding.coenso.model.FieldValuePostModel;
import com.primeholding.coenso.service.FieldValueService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FieldValueControllerTest {

    @Mock
    private FieldValueService fieldValueService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FieldValueController fieldValueController;

    private FieldValue fieldValue;

    private FieldValueGetModel fieldValueGetModel;

    private FieldValuePostModel fieldValuePostModel;

    @Before
    public void init() {
        TemplateForm templateForm = new TemplateForm();
        templateForm.setId(1L);
        templateForm.setTitle("TestTemplateForm");

        Field field = new Field();
        field.setTemplateForm(templateForm);
        field.setIsRequired(true);
        field.setOrder(1);
        field.setName("TestField");
        field.setId(1);

        FieldType fieldType = new FieldType();
        fieldType.setId(1);
        fieldType.setName("TestFieldType");
        fieldType.setPredefined(true);

        this.fieldValue = new FieldValue();
        fieldValue.setField(field);
        fieldValue.setValue("TestValue");
        fieldValue.setId(1);
        fieldValue.setFieldType(fieldType);

        List<FieldValue> fieldValues = new ArrayList<>();
        fieldValues.add(fieldValue);

        this.fieldValuePostModel = new FieldValuePostModel();
        fieldValuePostModel.setFieldTypeId(fieldValue.getFieldType().getId());
        fieldValuePostModel.setFieldId(fieldValue.getField().getId());
        fieldValuePostModel.setValue(fieldValue.getValue());

        this.fieldValueGetModel = new FieldValueGetModel();
        fieldValueGetModel.setId(fieldValue.getId());
        fieldValueGetModel.setValue(fieldValue.getValue());
        fieldValueGetModel.setFieldTypeId(fieldValue.getFieldType().getId());

        when(fieldValueService.create(fieldValue)).thenReturn(Optional.of(fieldValue));
        when(fieldValueService.update(fieldValue)).thenReturn(Optional.of(fieldValue));
        when(fieldValueService.get(1)).thenReturn(Optional.of(fieldValue));
        when(fieldValueService.get()).thenReturn(fieldValues);

        when(modelMapper.map(fieldValuePostModel, FieldValue.class)).thenReturn(fieldValue);
        when(modelMapper.map(fieldValue, FieldValueGetModel.class)).thenReturn(fieldValueGetModel);
    }

    @Test
    public void getFieldValueTestShouldReturnOkResponse() {
        HttpEntity expectedResponse = ResponseEntity.ok(this.fieldValueGetModel);
        HttpEntity controllerResponse = fieldValueController.get(1);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void getAllFieldValuesTestShouldReturnOkResponse() {
        List<FieldValueGetModel> fieldValueGetModels = new ArrayList<>();
        fieldValueGetModels.add(this.fieldValueGetModel);

        HttpEntity expectedResponse = ResponseEntity.ok(fieldValueGetModels);
        HttpEntity controllerResponse = fieldValueController.getAll();

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void createFieldValueTestShouldReturnCreatedResponse() {
        HttpEntity expectedResponse = ResponseEntity.created(URI.create("/api/v1/fieldValues/" + fieldValue.getId())).build();
        HttpEntity controllerResponse = fieldValueController.create(this.fieldValuePostModel);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void updateFieldValueTestShouldReturnOkResponse() {
        this.fieldValuePostModel.setValue("TestValue2");
        this.fieldValueGetModel.setValue("TestValue2");
        HttpEntity expectedResponse = ResponseEntity.ok(fieldValueGetModel);
        HttpEntity controllerResponse = fieldValueController.update(1, fieldValuePostModel);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void deleteFieldValueTestShouldReturnNoContentResponse() {
        HttpEntity expectedResponse = ResponseEntity.noContent().build();
        HttpEntity controllerResponse = fieldValueController.delete(1);

        assertEquals(expectedResponse, controllerResponse);
    }
}