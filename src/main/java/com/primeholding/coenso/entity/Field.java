package com.primeholding.coenso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(name = "field", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "template_form_id"})})
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "`order`", nullable = false)
    private Integer order;

    @Column(nullable = false)
    private boolean isRequired;

    @OneToMany(mappedBy = "field")
    private List<FieldValue> fieldValues;

    @ManyToOne
    @JoinColumn(name = "template_form_id")
    private TemplateForm templateForm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean required) {
        isRequired = required;
    }

    public List<FieldValue> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<FieldValue> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public TemplateForm getTemplateForm() {
        return templateForm;
    }

    public void setTemplateForm(TemplateForm templateForm) {
        this.templateForm = templateForm;
    }
}
