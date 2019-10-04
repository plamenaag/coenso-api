package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.model.FieldTypeGetModel;
import com.primeholding.coenso.service.FieldTypeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FieldTypeControllerTest {

    @Mock
    private FieldTypeService fieldTypeService;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    private FieldTypeController fieldTypeController;

    private FieldTypeGetModel fieldTypeGetModel;

    @Before
    public void init() {
        FieldType fieldType = new FieldType();
        fieldType.setId(1);
        fieldType.setName("Input");
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

        this.fieldTypeGetModel = new FieldTypeGetModel();
        fieldTypeGetModel.setId(fieldType.getId());
        fieldTypeGetModel.setName(fieldType.getName());

        when(fieldTypeService.get(1)).thenReturn(Optional.of(fieldType));
        when(fieldTypeService.get()).thenReturn(fieldTypes);

        when(modelMapper.map(fieldType, FieldTypeGetModel.class)).thenReturn(fieldTypeGetModel);
    }

    @Test
    public void getFieldTypeTestShouldReturnOkResponse() {
        HttpEntity expectedResponse = ResponseEntity.ok(this.fieldTypeGetModel);
        HttpEntity controllerResponse = fieldTypeController.get(1);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void getAllFieldTypesTestShouldReturnOkResponse() {
        List<FieldTypeGetModel> fieldTypeGetModels = new ArrayList<>();
        fieldTypeGetModels.add(this.fieldTypeGetModel);

        HttpEntity expectedResponse = ResponseEntity.ok(fieldTypeGetModels);
        HttpEntity controllerResponse = fieldTypeController.getAll();

        assertEquals(expectedResponse, controllerResponse);
    }
}