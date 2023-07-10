package com.springproject.orgschart.dao;

import com.springproject.orgschart.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByName(String name);

    boolean existsByJobTitle(String jobTitle);

    List<Employee> findByManager(Employee employee);

}
