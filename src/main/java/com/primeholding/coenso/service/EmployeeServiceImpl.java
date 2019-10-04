package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Employee;
import com.primeholding.coenso.exception.DuplicateEmailException;
import com.primeholding.coenso.exception.WrongCredentialsException;
import com.primeholding.coenso.repository.EmployeeRepository;
import com.primeholding.coenso.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Optional<Employee> get(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> get() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> get(Specification spec) {
        return employeeRepository.findAll(spec);
    }

    @Override
    public Optional<Employee> create(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if (employeeOptional.isPresent())
            return Optional.empty();

        if (!ValidationUtil.isNameValid(employee.getFirstName()) || !ValidationUtil.isNameValid(employee.getLastName())) {
            throw new WrongCredentialsException("Wrong name format");
        }

        Employee employeeSaved = employeeRepository.save(employee);
        return Optional.of(employeeSaved);
    }

    @Override
    public Optional<Employee> update(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        Optional<Employee> employeeOptionalTest = employeeRepository.findByEmail(employee.getEmail());

        if (employeeOptionalTest.isPresent()) {
            Employee employeeTest = employeeOptionalTest.get();
            if (!employeeTest.getId().equals(employee.getId())) {
                throw new DuplicateEmailException("Email address already in use");
            }
        }

        if (employeeOptional.isPresent()) {
            if (!ValidationUtil.isNameValid(employee.getFirstName()) || !ValidationUtil.isNameValid(employee.getLastName())) {
                throw new WrongCredentialsException("Wrong name format");
            }

            Employee employeeSaved = employeeRepository.save(employee);
            return Optional.of(employeeSaved);
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> patch(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        if (employeeOptional.isPresent()) {
            if (ValidationUtil.isNameValid(employee.getFirstName())) {
                employeeOptional.get().setFirstName(employee.getFirstName());
            }
            if (ValidationUtil.isNameValid(employee.getLastName())) {
                employeeOptional.get().setLastName(employee.getLastName());
            }
            if (employee.getDepartment() != null) {
                employeeOptional.get().setDepartment(employee.getDepartment());
            }

            if (employee.getEmail() != null && !employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
                    employeeOptional.get().setEmail(employee.getEmail());
            }
            employeeRepository.save(employeeOptional.get());
            return employeeOptional;
        }
        return Optional.empty();
    }
}
