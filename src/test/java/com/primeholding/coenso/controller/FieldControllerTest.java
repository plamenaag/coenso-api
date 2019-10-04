package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Field;
import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.model.FieldGetModel;
import com.primeholding.coenso.model.FieldPostModel;
import com.primeholding.coenso.service.FieldService;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FieldControllerTest {

    @Mock
    private FieldService fieldService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FieldController fieldController;

    private Field field;

    private FieldGetModel fieldGetModel;

    private FieldPostModel fieldPostModel;

    @Before
    public void init() {
        FieldType fieldType = new FieldType();
        fieldType.setId(1);
        fieldType.setName("TestFieldType");
        fieldType.setPredefined(true);

        TemplateForm templateForm = new TemplateForm();
        templateForm.setId(1L);
        templateForm.setTitle("TestTemplateForm");

        this.field = new Field();
        field.setTemplateForm(templateForm);
        field.setIsRequired(true);
        field.setOrder(1);
        field.setName("TestField");
        field.setId(1);

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

        this.fieldPostModel = new FieldPostModel();
        fieldPostModel.setTemplateFormId(field.getTemplateForm().getId());
        fieldPostModel.setIsRequired(field.getIsRequired());
        fieldPostModel.setName(field.getName());

        this.fieldGetModel = new FieldGetModel();
        fieldGetModel.setId(field.getId());
        fieldGetModel.setIsRequired(field.getIsRequired());
        fieldGetModel.setName(field.getName());
        fieldGetModel.setOrder(field.getOrder());

        when(fieldService.create(field)).thenReturn(Optional.of(field));
        when(fieldService.update(field)).thenReturn(Optional.of(field));
        when(fieldService.get(1)).thenReturn(Optional.of(field));
        when(fieldService.get()).thenReturn(fields);

        when(modelMapper.map(field, FieldPostModel.class)).thenReturn(fieldPostModel);
        when(modelMapper.map(fieldPostModel, Field.class)).thenReturn(field);

        when(modelMapper.map(field, FieldGetModel.class)).thenReturn(fieldGetModel);
    }

    @Test
    public void getFieldTestShouldReturnOkResponse() {
        HttpEntity expectedResponse = ResponseEntity.ok(this.fieldGetModel);
        HttpEntity controllerResponse = fieldController.get(1);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void getAllFieldsTestShouldReturnOkResponse() {
        List<FieldGetModel> fieldGetModels = new ArrayList<>();
        fieldGetModels.add(this.fieldGetModel);

        HttpEntity expectedResponse = ResponseEntity.ok(fieldGetModels);
        HttpEntity controllerResponse = fieldController.getAll();

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void createFieldTestShouldReturnCreatedResponse() {
        HttpEntity expectedResponse = ResponseEntity.created(URI.create("/api/v1/fields/" + field.getId())).build();
        HttpEntity controllerResponse = fieldController.create(this.fieldPostModel);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void updateFieldTestShouldReturnOkResponse() {
        this.field.setName("TestField2");
        this.fieldGetModel.setName("TestField2");
        HttpEntity expectedResponse = ResponseEntity.ok(fieldGetModel);
        HttpEntity controllerResponse = fieldController.update(1, modelMapper.map(field, FieldPostModel.class));

        assertEquals(expectedResponse, controllerResponse);
    }


    @Test
    public void deleteFieldTestShouldReturnNoContentResponse() {
        HttpEntity expectedResponse = ResponseEntity.noContent().build();
        HttpEntity controllerResponse = fieldController.delete(1);

        assertEquals(expectedResponse, controllerResponse);
    }
}