package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Account;
import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.model.TemplateFormPostModel;
import com.primeholding.coenso.security.InternalAccount;
import com.primeholding.coenso.service.AccountService;
import com.primeholding.coenso.service.TemplateFormService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemplateFormControllerTest {

    @Mock
    private TemplateFormService templateFormService;

    @Mock
    private AccountService accountService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TemplateFormController templateFormController;

    private TemplateForm templateForm;
    private TemplateFormPostModel templateFormPostModel;
    private InternalAccount internalAccount;

    @Before
    public void init() {
        Account account = new Account();
        account.setId(1);
        account.setFirstName("Ac");
        account.setLastName("Count");
        account.setEmail("mail@email.com");
        account.setPassword("password");

        templateForm = new TemplateForm();
        templateForm.setId(1l);
        templateForm.setTitle("Form title");
        templateForm.setUpdatedAt(Instant.parse("2019-05-05T12:00:00.00Z"));
        templateForm.setCreatedAt(Instant.parse("2019-05-05T12:00:00.00Z"));
        templateForm.setAccount(account);

        List<TemplateForm> templateForms = new ArrayList<>();
        templateForms.add(templateForm);
        Page<TemplateForm> templateFormPage = new PageImpl<>(templateForms);

        templateFormPostModel = new TemplateFormPostModel();
        templateFormPostModel.setTitle(templateForm.getTitle());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        internalAccount = InternalAccount.create(account);

        when(templateFormService.get(templateForm.getId())).thenReturn(Optional.of(templateForm));
        when(templateFormService.create(templateForm)).thenReturn(Optional.of(templateForm));
        when(templateFormService.update(templateForm)).thenReturn(Optional.of(templateForm));
        when(templateFormService.get(templateFormPage.getPageable(), Locale.getDefault())).thenReturn(templateFormPage);

        when(accountService.get(authentication.getName())).thenReturn(Optional.of(account));
        when(modelMapper.map(templateFormPostModel, TemplateForm.class)).thenReturn(templateForm);
    }

    @Test
    public void getTemplateFormShouldReturnOkResponse() {
        HttpEntity expectedResponse = ResponseEntity.ok(templateForm);
        HttpEntity controllerResponse = templateFormController.get(templateForm.getId(), new Locale(""));

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void getAllTemplateFormsShouldReturnOkResponse() {
        List<TemplateForm> templateForms = new ArrayList<>();
        templateForms.add(templateForm);
        Page<TemplateForm> page = new PageImpl<>(templateForms);

        HttpEntity expectedResponse = ResponseEntity.ok(page);
        HttpEntity controllerResponse = templateFormController.getAll(page.getPageable(), Locale.getDefault());

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void createTemplateFormShouldReturnCreatedResponse() {
        HttpEntity expectedResponse = ResponseEntity.created(URI.create("/api/v1/template-form/" + templateForm.getId()))
                .build();

        HttpEntity controllerResponse = templateFormController.create(templateFormPostModel);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void updateTemplateFormShouldReturnOkResponse() {
        templateForm.setTitle("new title");
        HttpEntity expectedResponse = ResponseEntity.ok(templateForm);

        HttpEntity controllerResponse = templateFormController.update(templateForm.getId(), templateFormPostModel, internalAccount);

        assertEquals(expectedResponse, controllerResponse);
    }

    @Test
    public void deleteTemplateFormShouldReturnNoContentResponse() {
        HttpEntity expectedResponse = ResponseEntity.noContent().build();

        HttpEntity controllerResponse = templateFormController.delete(templateForm.getId());

        assertEquals(expectedResponse, controllerResponse);
    }
}
