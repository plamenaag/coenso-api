package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Account;
import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.exception.TemplateFormNotFoundException;
import com.primeholding.coenso.model.TemplateFormPostModel;
import com.primeholding.coenso.security.InternalAccount;
import com.primeholding.coenso.service.AccountService;
import com.primeholding.coenso.service.TemplateFormService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api/v1/template-form")
@Api("CRUD endpoints for template form")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Unauthorized")
})
public class TemplateFormController {
    private TemplateFormService templateFormService;
    private AccountService accountService;
    private ModelMapper modelMapper;

    @Autowired
    public TemplateFormController(TemplateFormService templateFormService, AccountService accountService, ModelMapper modelMapper) {
        this.templateFormService = templateFormService;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    private boolean templateFormOwnership(InternalAccount internalAccount, Long templateId){
        Optional<TemplateForm> templateFormOptional = templateFormService.get(templateId);
        if (templateFormOptional.isPresent()) {
            return internalAccount.getId().equals(templateFormOptional.get().getAccount().getId());
        }
        throw new TemplateFormNotFoundException("template not found");
    }


    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Title size must be between 2 and 255, Title must not be blank"),
            @ApiResponse(code = 404, message = "Account not found"),
            @ApiResponse(code = 409, message = "Template form with that title already exists")
    })
    public HttpEntity create(
            @Valid @RequestBody TemplateFormPostModel templateFormPostModel
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> accountOptional = accountService.get(authentication.getName());

        if (accountOptional.isPresent()) {
            TemplateForm templateForm = modelMapper.map(templateFormPostModel, TemplateForm.class);
            templateForm.setAccount(accountOptional.get());

            Optional<TemplateForm> templateFormCreated = templateFormService.create(templateForm);
            if (templateFormCreated.isPresent())
                return ResponseEntity.created(URI.create("/api/v1/template-form/"
                        + templateFormCreated.get().getId())).build();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
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
    public HttpEntity getAll(
            @ApiIgnore("Ignored because swagger ui shows the wrong params") Pageable pageable,
            @ApiIgnore Locale locale
    ) {
        return ResponseEntity.ok(templateFormService.get(pageable, locale));
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity get(@PathVariable("id") Long id, @ApiIgnore Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        OffsetDateTime calOffset = OffsetDateTime.now(calendar.getTimeZone().toZoneId());

        long offset = calOffset.getOffset().getTotalSeconds();

        Optional<TemplateForm> templateFormOptional = templateFormService.get(id);
        if (templateFormOptional.isPresent()) {

            templateFormOptional.get().setCreatedAt(templateFormOptional.get().getCreatedAt().plus(offset, ChronoUnit.SECONDS));
            templateFormOptional.get().setUpdatedAt(templateFormOptional.get().getUpdatedAt().plus(offset, ChronoUnit.SECONDS));

            return ResponseEntity.ok(templateFormOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Title size must be between 2 and 255, Title must not be blank"),
            @ApiResponse(code = 404, message = "Template form not found"),
            @ApiResponse(code = 409, message = "Template form with that title already exists")
    })
    public HttpEntity update(@PathVariable("id") Long id,
                             @Valid @RequestBody TemplateFormPostModel templateFormPostModel,
                             @ApiIgnore @AuthenticationPrincipal InternalAccount internalAccount
    ) {
        try {
            if (!templateFormOwnership(internalAccount, id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (TemplateFormNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Template form not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        TemplateForm templateForm = modelMapper.map(templateFormPostModel, TemplateForm.class);
        templateForm.setId(id);

        Optional<TemplateForm> templateFormOptional = templateFormService.update(templateForm);
        if (templateFormOptional.isPresent()) {
            return ResponseEntity.ok(templateFormOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found")
    })
    public HttpEntity delete(@PathVariable("id") Long id) {
        try {
            templateFormService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
