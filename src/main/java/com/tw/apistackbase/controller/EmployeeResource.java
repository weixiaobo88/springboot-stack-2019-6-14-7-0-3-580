package com.tw.apistackbase.controller;

import com.tw.apistackbase.controller.dto.Resource;
import com.tw.apistackbase.domain.employee.Employee;
import com.tw.apistackbase.domain.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

/**
 * Created by jxzhong on 18/08/2017.
 */
@RestController
@RequestMapping("/employees")
public class EmployeeResource {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeResource(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<Resource>> getAll() {
        List<Resource> resourceWithUrls = employeeRepository.getAll().stream()
                .map(todo -> toResource(todo))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resourceWithUrls);
    }

    @GetMapping("/{employee-id}")
    public HttpEntity<Resource> getEmployee(@PathVariable("employee-id") long id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (!employeeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return respondWithResource(employeeOptional.get(), OK);
    }

    @PostMapping(headers = {"Content-type=application/json"})
    public HttpEntity<Resource> saveEmployee(@RequestBody Employee employee) {
        employee.setId(employeeRepository.getAll().size() + 1);
        employeeRepository.add(employee);

        return respondWithResource(employee, CREATED);
    }

    @DeleteMapping("/{employee-id}")
    public ResponseEntity deleteOneEmployee(@PathVariable("employee-id") long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            employeeRepository.delete(employeeOptional.get());
            return new ResponseEntity<>(OK);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{employee-id}", headers = {"Content-type=application/json"})
    public HttpEntity<Resource> updateEmployee(@PathVariable("employee-id") long id, @RequestBody Employee newEmployee) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (!employeeOptional.isPresent()) {
            return new ResponseEntity<>(NOT_FOUND);
        } else if (newEmployee == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        employeeRepository.delete(employeeOptional.get());

        Employee mergedEmployee = employeeOptional.get().merge(newEmployee);
        employeeRepository.add(mergedEmployee);

        return respondWithResource(mergedEmployee, OK);
    }

    private Resource toResource(Employee employee) {
        return new Resource(employee);
    }

    private HttpEntity<Resource> respondWithResource(Employee employee, HttpStatus statusCode) {
        Resource resource = toResource(employee);

        return new ResponseEntity<>(resource, statusCode);
    }

}
