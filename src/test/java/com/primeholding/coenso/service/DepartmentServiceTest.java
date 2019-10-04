package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Department;
import com.primeholding.coenso.repository.DepartmentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;

    private Department duplicateNameDepartment;

    @Before
    public void init() {
        this.department = new Department();
        department.setId(1);
        department.setName("Finance");
        this.duplicateNameDepartment = new Department();
        duplicateNameDepartment.setName("Finance");

        List<Department> departments = new ArrayList<>();
        departments.add(department);

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(departmentRepository.findAll()).thenReturn(departments);

        when(departmentRepository.findByName(department.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);
    }

    @Test
    public void createDepartmentTestShouldReturnCreatedDepartment() {
        Optional<Department> createdDepartment = departmentService.create(department);

        if (createdDepartment.isPresent()) {
            assertEquals(department, createdDepartment.get());
        }
    }

    @Test
    public void createDuplicateDepartmentTestShouldReturnOptionalEmpty() {
        Optional<Department> createdDepartment = departmentService.create(department);

        if (createdDepartment.isPresent()) {
            assertEquals(department, createdDepartment.get());
        }

        Optional<Department> duplicateDepartment = departmentService.create(duplicateNameDepartment);

        assertEquals(duplicateDepartment, Optional.empty());
    }

    @Test
    public void updateDepartmentTestShouldReturnUpdatedDepartment() {
        department.setName("NewName");
        Optional<Department> updatedDepartment = departmentService.update(department);

        if (updatedDepartment.isPresent()) {
            assertEquals("NewName", updatedDepartment.get().getName());
        }
    }

    @Test
    public void getDepartmentTestShouldReturnDepartment() {
        Optional<Department> departmentOptional = departmentService.get(1);

        if (departmentOptional.isPresent()) {
            assertEquals(department, departmentOptional.get());
        }
    }

    @Test
    public void getAllDepartmentsTestShouldReturnListOfDepartments() {
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(department);

        List<Department> departments = departmentService.get();

        assertEquals(departmentList, departments);
    }
}