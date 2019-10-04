package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Department;
import com.primeholding.coenso.entity.Employee;
import com.primeholding.coenso.exception.DuplicateEmailException;
import com.primeholding.coenso.model.EmployeePatchModel;
import com.primeholding.coenso.model.EmployeePostModel;
import com.primeholding.coenso.repository.specification.CustomSpecificationsBuilder;
import com.primeholding.coenso.service.DepartmentService;
import com.primeholding.coenso.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
@Api("CRUD endpoints for employee")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Unauthorized")})
public class EmployeeController {

    public static final String ERROR = "error";
    private EmployeeService employeeService;
    private DepartmentService departmentService;
    private ModelMapper modelMapper;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Invalid email format; Data too long; Wrong name format"),
            @ApiResponse(code = 404, message = "Department with that id not found"),
            @ApiResponse(code = 409, message = "Email address already in use")
    })
    public HttpEntity create(@Valid @RequestBody EmployeePostModel employeePostModel) {
        Employee employee = modelMapper.map(employeePostModel, Employee.class);

        Optional<Department> department = departmentService.get(employeePostModel.getDepartmentId());

        if (department.isPresent()) {
            employee.setDepartment(department.get());
        } else {
            return ResponseEntity.notFound().build();
        }

        Optional<Employee> employeeOptional = employeeService.create(employee);

        return employeeOptional.map(a ->
                ResponseEntity.created(URI.create("/api/v1/employees/" + a.getId()))
                        .build())
                .orElseThrow(() -> new DuplicateEmailException("Email address already in use"));
    }

    @GetMapping
    public HttpEntity<List<Employee>> getAll(@RequestParam(value = "search", required = false) String search) {
        CustomSpecificationsBuilder<Employee> builder = new CustomSpecificationsBuilder<>(search);
        return ResponseEntity.ok(employeeService.get(builder.build()));
    }

    @GetMapping("/{id}")
    public HttpEntity get(@PathVariable("id") Long id) {
        Optional<Employee> employeeOptional = employeeService.get(id);
        if (employeeOptional.isPresent())
            return ResponseEntity.ok(employeeOptional.get());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity<Object> delete(@PathVariable("id") Long id) {
        try {
            employeeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid email format; Data too long; Wrong name format"),
            @ApiResponse(code = 404, message = "Department not found; Employee not found"),
            @ApiResponse(code = 409, message = "Email address already in use")
    })
    public HttpEntity update(@PathVariable("id") Long id, @Valid @RequestBody EmployeePostModel employeePostModel) {
        Employee employee = modelMapper.map(employeePostModel, Employee.class);
        employee.setId(id);

        Map<String, String> error = new HashMap<>();

        Optional<Department> department = departmentService.get(employeePostModel.getDepartmentId());
        if (department.isPresent()) {
            employee.setDepartment(department.get());
        } else {
            error.put(ERROR, "Department not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        Optional<Employee> employeeOptional = employeeService.update(employee);

        if (employeeOptional.isPresent())
            return ResponseEntity.ok(employeeOptional.get());
        error.put(ERROR, "Employee not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @PatchMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Name must not be blank; Name size must be between 2 and 40; Email must be a well-formed email address"),
            @ApiResponse(code = 404, message = "Department not found; Employee not found")
    })
    public HttpEntity patch(@PathVariable("id") Long id, @Valid @RequestBody EmployeePatchModel employeePatchModel) {
        Employee employee = modelMapper.map(employeePatchModel,Employee.class);
        Map<String, String> error = new HashMap<>();
        if(employeePatchModel.getDepartmentId() != null) {
            Optional<Department> department = departmentService.get(employeePatchModel.getDepartmentId());
            if (department.isPresent()) {
                employee.setDepartment(department.get());
            } else {
                error.put(ERROR, "Department not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        }
        employee.setId(id);
        Optional<Employee> employeeOptional = employeeService.patch(employee);
        if(employeeOptional.isPresent()) {
            return ResponseEntity.ok(employeeOptional.get());
        }
        error.put(ERROR, "Employee not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
