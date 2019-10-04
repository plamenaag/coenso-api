package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Department;
import com.primeholding.coenso.entity.Employee;
import com.primeholding.coenso.model.EmployeePatchModel;
import com.primeholding.coenso.model.EmployeePostModel;
import com.primeholding.coenso.repository.specification.CustomSpecificationsBuilder;
import com.primeholding.coenso.service.DepartmentService;
import com.primeholding.coenso.service.EmployeeService;
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
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomSpecificationsBuilder<Employee> employeeCustomSpecificationsBuilder;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;
    private EmployeePatchModel employeePatchModel;

    @Before
    public void init() {
        Department department = new Department();
        department.setName("TestDepartment");
        department.setId(1);

        this.employee = new Employee();
        employee.setId(1l);
        employee.setEmail("mail@email.com");
        employee.setFirstName("Emp");
        employee.setLastName("Loyee");
        employee.setDepartment(department);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        EmployeePostModel employeePostModel = new EmployeePostModel();
        employeePostModel.setEmail(employee.getEmail());
        employeePostModel.setFirstName(employee.getFirstName());
        employeePostModel.setLastName(employee.getLastName());
        employeePostModel.setDepartmentId(department.getId());

        employeePatchModel = new EmployeePatchModel();
        employeePatchModel.setLastName("patchedLastName");

        when(employeeService.create(employee)).thenReturn(Optional.of(employee));
        when(employeeService.update(employee)).thenReturn(Optional.of(employee));
        when(employeeService.get(employeeCustomSpecificationsBuilder.build())).thenReturn(employees);
        when(employeeService.get(1l)).thenReturn(Optional.of(employee));

        when(departmentService.get(department.getId())).thenReturn(Optional.of(department));

        when(modelMapper.map(employee, EmployeePostModel.class)).thenReturn(employeePostModel);
        when(modelMapper.map(employeePostModel, Employee.class)).thenReturn(employee);

        Employee employeePatched = employee;
        employeePatched.setLastName(employeePatchModel.getLastName());

        when(employeeService.patch(employeePatched)).thenReturn(Optional.of(employeePatched));
        when(modelMapper.map(employeePatchModel, Employee.class)).thenReturn(employeePatched);
    }

    @Test
    public void getEmployeeTestShouldReturnOkResponse() {
        HttpEntity expectedResponse = ResponseEntity.ok(employee);
        HttpEntity controllerResponse = employeeController.get(1l);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void getAllEmployeesTestShouldReturnOkResponse() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        HttpEntity expectedResponse = ResponseEntity.ok(employees);
        HttpEntity controllerResponse = employeeController.getAll("");

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void createEmployeeTestShouldReturnCreatedResponse() {
        HttpEntity expectedResponse = ResponseEntity.created(URI.create("/api/v1/employees/" + employee.getId()))
                .build();
        HttpEntity controllerResponse = employeeController.create(modelMapper.map(employee, EmployeePostModel.class));

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void updateEmployeeTestShouldReturnOkResponse() {
        employee.setFirstName("Test");
        HttpEntity expectedResponse = ResponseEntity.ok(employee);
        HttpEntity controllerResponse = employeeController.update(1l, modelMapper.map(employee, EmployeePostModel.class));

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void deleteEmployeeTestShouldReturnNoContentResponse() {
        HttpEntity expectedResponse = ResponseEntity.noContent().build();
        HttpEntity controllerResponse = employeeController.delete(1l);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void patchEmployeeTestShouldReturnOkContentResponse() {

        Employee expectedEmployee = employee;
        expectedEmployee.setLastName(employeePatchModel.getLastName());
        HttpEntity expectedResponse = ResponseEntity.ok(expectedEmployee);

        HttpEntity controllerResponse = employeeController.patch(employee.getId(), employeePatchModel);

        assertEquals(expectedResponse, controllerResponse);
    }

}
