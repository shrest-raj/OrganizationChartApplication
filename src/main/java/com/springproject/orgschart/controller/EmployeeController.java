package com.springproject.orgschart.controller;

import com.springproject.orgschart.exceptions.EmployeeNotFoundException;
import com.springproject.orgschart.dto.EmployeeDTO;
import com.springproject.orgschart.dto.OrganizationChartDTO;
import com.springproject.orgschart.service.EmployeeServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The EmployeeController class handles HTTP requests related to employee management.
 * It provides endpoints for retrieving, adding, updating, and deleting employees.
 *
 * @RestController Indicates that this class serves as a RESTful controller
 * @RequestMapping("/api") Specifies the base URL path for all requests handled by this controller.
 */
@RestController
@RequestMapping("/api")//applied to class level to map request path
public class EmployeeController {
    @Autowired//automatic dependency injection is enabled
    private EmployeeServiceImplementation employeeServiceImpl;

    /**
     * Constructs a new EmployeeController with the specified EmployeeServiceImplementation.
     *
     * @param employeeServiceImpl The employee service implementation to be used for handling employee-related operations.
     */
    public EmployeeController(EmployeeServiceImplementation employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    /**
     * Retrieves a list of all employees sorted by name.
     *
     * @return ResponseEntity containing the list of EmployeeDTO objects and HTTP status 200 (OK).
     */
    @GetMapping("/employees")
//applied to method level to map request path and the response entity will fully configure the http response
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesSortedByName() {
        List<EmployeeDTO> employees = employeeServiceImpl.getAllEmployeesSortedByName();
        return ResponseEntity.ok(employees);
    }

    /**
     * Retrieves the organization chart for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return ResponseEntity containing the OrganizationChartDTO for the employee and HTTP status 200 (OK).
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<OrganizationChartDTO> getEmployeeOrganizationChart(@PathVariable Long employeeId) {
        OrganizationChartDTO organizationChart = employeeServiceImpl.getEmployeeOrganizationChart(employeeId);
        return ResponseEntity.ok(organizationChart);
    }

    /**
     * Adds a new employee.
     *
     * @param employeeDTO The EmployeeDTO object representing the new employee to be added.
     * @return ResponseEntity containing the created EmployeeDTO and HTTP status 201 (Created).
     */
    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeServiceImpl.addEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    /**
     * Updates an existing employee's details.
     *
     * @param employeeId  The ID of the employee to be updated.
     * @param employeeDTO The EmployeeDTO object containing the updated employee details.
     * @return ResponseEntity with a success message and HTTP status 200 (OK) if the update is successful,
     * or an error message and HTTP status 400 (Bad Request) if the update fails.
     * @throws EmployeeNotFoundException if the employee with the specified ID is not found.
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        if (!employeeServiceImpl.employeeExists(employeeId)) {
            throw new EmployeeNotFoundException();
        }

        if (Boolean.TRUE.equals(employeeDTO.getReplace())) {
            boolean replacementSuccessful = employeeServiceImpl.replaceEmployee(employeeId, employeeDTO);
            if (!replacementSuccessful) {
                return ResponseEntity.badRequest().body("The replacement of the employee failed. \nPlease verify if the combination of the employee ID, job title, and manager ID is valid according to the hierarchy.PLease keep in mind that to replace a employee the level must be same as the existing employee");
            }
        } else {
            boolean updateSuccessful = employeeServiceImpl.updateEmployeeDetails(employeeId, employeeDTO);
            if (!updateSuccessful) {
                return ResponseEntity.badRequest().body("Failed to update the employee details. Check whether the employee id is valid or not or also check whether the jobtitle and the manager id are valid and not null");
            }
        }

        return ResponseEntity.ok("Employee updated successfully.");
    }

    /**
     * Deletes an employee.
     *
     * @param employeeId The ID of the employee to be deleted.
     * @return ResponseEntity with a success message and HTTP status 200 (OK) if the deletion is successful,
     * or an error message and HTTP status 403 (Forbidden) if the deletion is not allowed.
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) {
        boolean deleted = employeeServiceImpl.deleteEmployee(employeeId);
        if (deleted) {
            return ResponseEntity.ok("Employee deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot delete Director with direct reports.");
        }
    }
}









