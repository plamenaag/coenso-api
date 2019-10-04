package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Department;
import com.primeholding.coenso.model.DepartmentGetModel;
import com.primeholding.coenso.model.DepartmentPostModel;
import com.primeholding.coenso.repository.specification.CustomSpecificationsBuilder;
import com.primeholding.coenso.service.DepartmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomSpecificationsBuilder<Department> departmentCustomSpecificationsBuilder;

    @InjectMocks
    private DepartmentController departmentController;

    private Department department;

    private DepartmentGetModel departmentGetModel;

    @Before
    public void init() {
        this.department = new Department();
        this.departmentGetModel = new DepartmentGetModel();
        DepartmentPostModel departmentPostModel = new DepartmentPostModel();
        department.setId(1);
        department.setName("Finance");

        List<Department> departments = new ArrayList<>();
        departments.add(department);

        departmentPostModel.setName(department.getName());
        Page<Department> page = new PageImpl<>(departments);

        when(departmentService.create(department)).thenReturn(Optional.of(department));
        when(departmentService.update(department)).thenReturn(Optional.of(department));
        when(departmentService.get(departmentCustomSpecificationsBuilder.build(), page.getPageable())).thenReturn(page);
        when(departmentService.get(1)).thenReturn(Optional.of(department));

        when(modelMapper.map(department, DepartmentPostModel.class)).thenReturn(departmentPostModel);
        when(modelMapper.map(departmentPostModel, Department.class)).thenReturn(department);
        when(modelMapper.map(department, DepartmentGetModel.class)).thenReturn(departmentGetModel);
    }

    @Test
    public void getDepartmentTestShouldReturnOkResponse() {
        HttpEntity expectedResponse = ResponseEntity.ok(departmentGetModel);
        HttpEntity controllerResponse = departmentController.get(1);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void getAllDepartmentsTestShouldReturnOkResponse() {
        List<DepartmentGetModel> departmentGetModels = new ArrayList<>();
        departmentGetModels.add(departmentGetModel);

        Page<DepartmentGetModel> page = new PageImpl<>(departmentGetModels);
        HttpEntity expectedResponse = ResponseEntity.ok(departmentGetModels);
        HttpEntity controllerResponse = departmentController.getAll("", page.getPageable());

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void createDepartmentTestShouldReturnCreatedResponse() {
        HttpEntity expectedResponse = ResponseEntity.created(URI.create("/api/v1/departments/" + department.getId())).build();
        HttpEntity controllerResponse = departmentController.create(modelMapper.map(department, DepartmentPostModel.class));

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void updateDepartmentTestShouldReturnOkResponse() {
        department.setName("Name");
        HttpEntity expectedResponse = ResponseEntity.ok(departmentGetModel);
        HttpEntity controllerResponse = departmentController.update(1, modelMapper.map(department, DepartmentPostModel.class));

        assertEquals(expectedResponse, controllerResponse);
    }


    @Test
    public void deleteDepartmentTestShouldReturnNoContentResponse() {
        HttpEntity expectedResponse = ResponseEntity.noContent().build();
        HttpEntity controllerResponse = departmentController.delete(1);

        assertEquals(expectedResponse, controllerResponse);
    }
}