package com.primeholding.coenso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Set;

@Entity
@Setter
@Getter
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @Email(message = "Please provide a valid e-mail address")
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(columnDefinition = "varchar(20)")
    @JsonIgnore
    private String resetCode;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private Set<TemplateForm> templateForms;
}
