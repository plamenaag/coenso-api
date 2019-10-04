package com.primeholding.coenso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.primeholding.coenso.model.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
public class TemplateForm extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255)", nullable = false, unique = true)
    private String title;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "templateForm")
    @JsonIgnore
    private List<Field> fields;
}
