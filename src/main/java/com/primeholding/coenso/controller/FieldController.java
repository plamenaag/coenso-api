package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Field;
import com.primeholding.coenso.model.FieldGetModel;
import com.primeholding.coenso.model.FieldPostModel;
import com.primeholding.coenso.service.FieldService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fields")
@Api("CRUD endpoints for field")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Unauthorized")
})
public class FieldController {
    private FieldService fieldService;
    private ModelMapper modelMapper;

    @Autowired
    public FieldController(FieldService fieldService, ModelMapper modelMapper) {
        this.fieldService = fieldService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity get(@PathVariable("id") Integer id) {
        Optional<Field> field = fieldService.get(id);
        if (field.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(field.get(), FieldGetModel.class));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public HttpEntity getAll() {
        List<Field> fields = fieldService.get();
        List<FieldGetModel> fieldGetModels = new ArrayList<>();
        for (Field field : fields) {
            fieldGetModels.add(modelMapper.map(field, FieldGetModel.class));
        }

        return ResponseEntity.ok(fieldGetModels);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Field name must be between 2 and 255, Field name must not be empty"),
            @ApiResponse(code = 409, message = "Field with that name already exists")
    })
    public HttpEntity create(@Valid @RequestBody FieldPostModel fieldPostModel) {
        Field field = modelMapper.map(fieldPostModel, Field.class);

        Optional<Field> savedField = fieldService.create(field);
        if (savedField.isPresent()) {
            return savedField.map(a ->
                    ResponseEntity.created(URI.create("/api/v1/fields/" + a.getId()))
                            .build())
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                            .build());
        }

        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Field name must be between 2 and 255, Field name must not be empty"),
            @ApiResponse(code = 404, message = "Field not found"),
            @ApiResponse(code = 409, message = "Field with that name already exists")
    })
    public HttpEntity update(@PathVariable(required = true) Integer id, @Valid @RequestBody FieldPostModel fieldPostModel) {
        Field field = modelMapper.map(fieldPostModel, Field.class);
        field.setId(id);

        Optional<Field> updatedField = fieldService.update(field);
        if (updatedField.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(updatedField.get(), FieldGetModel.class));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity delete(@PathVariable(required = true) Integer id) {
        if (fieldService.get(id).isPresent()) {
            fieldService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
