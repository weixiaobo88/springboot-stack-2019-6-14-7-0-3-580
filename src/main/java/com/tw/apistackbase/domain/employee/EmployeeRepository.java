package com.tw.apistackbase.domain.employee;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EmployeeRepository {
    private Set<Employee> employees;

    public EmployeeRepository() {
        this.employees = new HashSet<>();
    }

    public List<Employee> getAll() {
        return employees.stream().collect(Collectors.toList());
    }

    public Optional<Employee> findById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findFirst();
    }

    public void delete(Employee employee) {
        employees.remove(employee);
    }

    public void add(Employee employee) {
        if (employee.getId() == 0) {
            employee.setId(employees.size() + 1);
        }
        employees.add(employee);
    }

    public void update(long id, Employee newEmployee) {
        Stream<Employee> employeeStream = employees.stream()
                .map(employee -> {
                    if (newEmployee.getId() == id) {
                        return newEmployee;
                    }
                    return employee;
                });
        employees = employeeStream.collect(Collectors.toSet());
    }

    private boolean exist(long id) {
        return employees.stream().anyMatch(employee -> id == employee.getId());
    }
}
