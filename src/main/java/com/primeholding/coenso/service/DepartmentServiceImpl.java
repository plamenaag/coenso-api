package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Department;
import com.primeholding.coenso.exception.DataConflictException;
import com.primeholding.coenso.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public Page<Department> get(Specification spec, Pageable pageable) {
            return departmentRepository.findAll(spec,pageable);
    }

    @Override
    public List<Department> get() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> get(Integer departmentId) {
        return departmentRepository.findById(departmentId);
    }

    @Override
    public Optional<Department> get(String departmentName) {
        return departmentRepository.findByName(departmentName);
    }

    @Override
    public Optional<Department> create(Department department) {
        if (departmentRepository.findByName(department.getName()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(departmentRepository.save(department));
    }

    @Override
    public Optional<Department> update(Department department) {
        Optional<Department> foundDepartmentByName = departmentRepository.findByName(department.getName());
        if (foundDepartmentByName.isPresent() && !foundDepartmentByName.get().getId().equals(department.getId())) {
            return Optional.empty();
        }

        Optional<Department> departmentToUpdate = get(department.getId());

        if (departmentToUpdate.isPresent()) {
            departmentToUpdate.get().setName(department.getName());

            return Optional.of(departmentRepository.save(departmentToUpdate.get()));
        }

        return Optional.empty();
    }

    @Override
    public void delete(Integer departmentId) {
        Optional<Department> department = get(departmentId);
        if (department.isPresent()) {
            if (department.get().getEmployees() != null && !department.get().getEmployees().isEmpty()) {
                throw new DataConflictException("Department which is already related to a user can not be deleted!");
            }

            departmentRepository.delete(department.get());
        }
    }
}
