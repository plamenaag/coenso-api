package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Employee;
import com.primeholding.coenso.repository.EmployeeRepository;
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
@SuppressWarnings("Duplicates")
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @Before
    @SuppressWarnings("Duplicates")
    public void init() {
        this.employee = new Employee();
        employee.setId(1l);
        employee.setEmail("mail@email.com");
        employee.setFirstName("Emp");
        employee.setLastName("Loyee");

        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        when(employeeRepository.findById(1l)).thenReturn(Optional.of(employee));
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);
    }

    @Test
    public void createEmployeeTestShouldReturnCreatedEmployee() {
        Optional<Employee> createdEmployee = employeeService.create(employee);

        if (createdEmployee.isPresent()) {
            assertEquals(employee, createdEmployee.get());
        }
    }

    @Test
    public void updateEmployeeTestShouldReturnUpdatedEmployee() {
        employee.setLastName("NewLastName");
        Optional<Employee> updatedEmployee = employeeService.update(employee);

        if (updatedEmployee.isPresent()) {
            assertEquals("NewLastName", updatedEmployee.get().getLastName());
        }
    }

    @Test
    public void getEmployeeTestShouldReturnEmployee() {
        Optional<Employee> employeeOptional = employeeService.get(1l);

        if (employeeOptional.isPresent()) {
            assertEquals(employee, employeeOptional.get());
        }
    }

    @Test
    public void getAllEmployeesTestShouldReturnListOfEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);

        List<Employee> employees = employeeService.get();

        assertEquals(employeeList, employees);
    }

    @Test
    public void patchEmployeeTestShouldReturnPatchedEmployee() {
        Employee employeePatched = new Employee();
        employeePatched.setLastName("PatchedLastName");

        Optional<Employee> employeeOptional = employeeService.patch(employeePatched);
        if (employeeOptional.isPresent())
            assertEquals(employeePatched.getLastName(), employeeOptional.get().getLastName());
    }
}
