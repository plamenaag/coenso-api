package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> get(Long id);

    List<Employee> get(Specification spec);

    List<Employee> get();

    Optional<Employee> create(Employee employee);

    Optional<Employee> update(Employee employee);

    void delete(Long id);

    Optional<Employee> patch(Employee employee);
}
