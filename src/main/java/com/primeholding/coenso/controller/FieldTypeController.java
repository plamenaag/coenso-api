package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.FieldType;
import com.primeholding.coenso.model.FieldTypeGetModel;
import com.primeholding.coenso.service.FieldTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fieldTypes")
@Api("CRUD endpoints for field type")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Unauthorized")
})
public class FieldTypeController {
    private FieldTypeService fieldTypeService;
    private ModelMapper modelMapper;


    @Autowired
    public FieldTypeController(FieldTypeService fieldTypeService, ModelMapper modelMapper) {
        this.fieldTypeService = fieldTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity get(@PathVariable("id") Integer id) {
        Optional<FieldType> fieldType = fieldTypeService.get(id);
        if (fieldType.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(fieldType.get(), FieldTypeGetModel.class));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public HttpEntity getAll() {
        List<FieldType> fieldTypes = fieldTypeService.get();
        List<FieldTypeGetModel> fieldTypeGetModels = new ArrayList<>();
        for (FieldType fieldType : fieldTypes) {
            fieldTypeGetModels.add(modelMapper.map(fieldType, FieldTypeGetModel.class));
        }

        return ResponseEntity.ok(fieldTypeGetModels);
    }
}
