package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Page<Department> get(Specification spec, Pageable pageable);

    List<Department> get();

    Optional<Department> get(Integer departmentId);

    Optional<Department> get(String departmentName);

    Optional<Department> create(Department department);

    Optional<Department> update(Department department);

    void delete(Integer departmentId);
}
