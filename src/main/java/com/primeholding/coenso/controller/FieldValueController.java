package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.model.FieldValueGetModel;
import com.primeholding.coenso.model.FieldValuePostModel;
import com.primeholding.coenso.service.FieldValueService;
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
@RequestMapping("/api/v1/fieldValues")
@Api("CRUD endpoints for field value")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Unauthorized")
})
public class FieldValueController {
    private FieldValueService fieldValueService;
    private ModelMapper modelMapper;

    @Autowired
    public FieldValueController(FieldValueService fieldValueService, ModelMapper modelMapper) {
        this.fieldValueService = fieldValueService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity get(@PathVariable("id") Integer id) {
        Optional<FieldValue> fieldValue = fieldValueService.get(id);
        if (fieldValue.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(fieldValue.get(), FieldValueGetModel.class));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public HttpEntity getAll() {
        List<FieldValue> fieldValues = fieldValueService.get();
        List<FieldValueGetModel> fieldValueGetModels = new ArrayList<>();
        for (FieldValue fieldValue : fieldValues) {
            fieldValueGetModels.add(modelMapper.map(fieldValue, FieldValueGetModel.class));
        }

        return ResponseEntity.ok(fieldValueGetModels);
    }


    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Field value must not be empty"),
            @ApiResponse(code = 409, message = "Field with that value already exists")
    })
    public HttpEntity create(@Valid @RequestBody FieldValuePostModel fieldValuePostModel) {
        if (fieldValuePostModel.getFieldId() == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        FieldValue fieldValue = modelMapper.map(fieldValuePostModel, FieldValue.class);

        Optional<FieldValue> savedFieldValue = fieldValueService.create(fieldValue);
        if (savedFieldValue.isPresent()) {
            return savedFieldValue.map(a ->
                    ResponseEntity.created(URI.create("/api/v1/fieldValues/" + a.getId()))
                            .build())
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                            .build());
        }

        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Field value must not be empty"),
            @ApiResponse(code = 404, message = "Field value not found"),
            @ApiResponse(code = 409, message = "Field with that value already exists")
    })
    public HttpEntity update(@PathVariable(required = true) Integer id, @Valid @RequestBody FieldValuePostModel fieldValuePostModel) {
        FieldValue fieldValue = modelMapper.map(fieldValuePostModel, FieldValue.class);
        fieldValue.setId(id);

        Optional<FieldValue> updatedFieldValue = fieldValueService.update(fieldValue);
        if (updatedFieldValue.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(updatedFieldValue.get(), FieldValueGetModel.class));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity delete(@PathVariable(required = true) Integer id) {
        if (fieldValueService.get(id).isPresent()) {
            fieldValueService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
