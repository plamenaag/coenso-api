package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Department;
import com.primeholding.coenso.model.DepartmentGetModel;
import com.primeholding.coenso.model.DepartmentPostModel;
import com.primeholding.coenso.repository.specification.CustomSpecificationsBuilder;
import com.primeholding.coenso.service.DepartmentService;
import com.primeholding.coenso.util.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/departments")
@Api("CRUD endpoints for department")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Unauthorized")})
public class DepartmentController {

    private DepartmentService departmentService;
    private ModelMapper modelMapper;

    @Autowired
    public DepartmentController(DepartmentService departmentService, ModelMapper modelMapper) {
        this.departmentService = departmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public HttpEntity get(@PathVariable("id") Integer id) {
        Optional<Department> department = departmentService.get(id);
        if (department.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(department.get(), DepartmentGetModel.class));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "10")
    })
    public HttpEntity getAll(@RequestParam(value = "search", required = false) String search,
                             @ApiIgnore("Ignored because swagger ui shows the wrong params") Pageable pageable) {
        CustomSpecificationsBuilder<Department> builder = new CustomSpecificationsBuilder<>(search);

        Page<Department> departments = departmentService.get(builder.build(), pageable);
        List<DepartmentGetModel> departmentGetModels = new ArrayList<>();
        for (Department department : departments) {
            departmentGetModels.add(modelMapper.map(department, DepartmentGetModel.class));
        }

        return ResponseEntity.ok(departmentGetModels);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Data too long; Wrong name format")
    })
    public HttpEntity create(@Valid @RequestBody DepartmentPostModel departmentPostModel) {
        if (!ValidationUtil.isNameValid(departmentPostModel.getName())) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Department department = modelMapper.map(departmentPostModel, Department.class);

        Optional<Department> savedDepartment = departmentService.create(department);
        if (savedDepartment.isPresent()) {
            return savedDepartment.map(a ->
                    ResponseEntity.created(URI.create("/api/v1/departments/" + a.getId()))
                            .build())
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                            .build());
        }

        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Data too long; Wrong name format")
    })
    public HttpEntity update(@PathVariable(required = true) Integer id, @Valid @RequestBody DepartmentPostModel departmentPostModel) {
        if (!ValidationUtil.isNameValid(departmentPostModel.getName())) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Department department = modelMapper.map(departmentPostModel, Department.class);
        department.setId(id);

        Optional<Department> updatedDepartment = departmentService.update(department);
        if (updatedDepartment.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(updatedDepartment.get(), DepartmentGetModel.class));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Department which is already related to a user can not be deleted!")
    })
    public HttpEntity delete(@PathVariable(required = true) Integer id) {
        if (departmentService.get(id).isPresent()) {
            departmentService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
