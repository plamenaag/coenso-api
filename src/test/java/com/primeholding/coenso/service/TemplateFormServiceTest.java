package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.TemplateForm;
import com.primeholding.coenso.repository.TemplateFormRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemplateFormServiceTest {

    @Mock
    private TemplateFormRepository templateFormRepository;

    @InjectMocks
    private TemplateFormServiceImpl templateFormService;

    private TemplateForm templateForm;
    private Page<TemplateForm> templateFormPage;

    @Before
    public void init() {
        templateForm = new TemplateForm();
        templateForm.setTitle("Form title");
        templateForm.setId(1l);
        templateForm.setUpdatedAt(Instant.parse("2019-05-05T12:00:00.00Z"));
        templateForm.setCreatedAt(Instant.parse("2019-05-05T12:00:00.00Z"));

        List<TemplateForm> templateForms = new ArrayList<>();
        templateForms.add(templateForm);
        templateFormPage = new PageImpl<>(templateForms);

        when(templateFormRepository.save(templateForm)).thenReturn(templateForm);
        when(templateFormRepository.findById(templateForm.getId())).thenReturn(Optional.of(templateForm));
        when(templateFormRepository.findAll(templateFormPage.getPageable())).thenReturn(templateFormPage);
    }

    @Test
    public void createTemplateFormTestShouldReturnCreatedTemplateForm() {
        Optional<TemplateForm> templateFormCreated = templateFormService.create(templateForm);

        assertEquals(Optional.of(templateForm), templateFormCreated);
    }

    @Test
    public void getAllTemplateFormsShouldReturnListOfAllTemplateForms() {
        List<TemplateForm> templateFormList = new ArrayList<>();
        templateFormList.add(templateForm);
        Page<TemplateForm> expected = new PageImpl<>(templateFormList);

        Page<TemplateForm> templateForms = templateFormService.get(templateFormPage.getPageable(), Locale.getDefault());

        assertEquals(expected, templateForms);
    }

    @Test
    public void getTemplateFormByIdShouldReturnCorrespondingTemplateForm() {
        Optional<TemplateForm> templateFormOptional = templateFormService.get(templateForm.getId());

        if (templateFormOptional.isPresent()) {
            assertEquals(templateForm, templateFormOptional.get());
        }
    }

    @Test
    public void updateTemplateFormByIdShouldReturnUpdatedTemplateForm() {
        templateForm.setTitle("A brand new title");
        Optional<TemplateForm> templateFormOptional = templateFormService.update(templateForm);

        if (templateFormOptional.isPresent()) {
            assertEquals("A brand new title", templateFormOptional.get().getTitle());
        }
    }
}
