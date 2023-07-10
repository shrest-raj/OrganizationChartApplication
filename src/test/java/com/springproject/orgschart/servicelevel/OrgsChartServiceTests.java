package com.springproject.orgschart.servicelevel;

import com.springproject.orgschart.exceptions.*;
import com.springproject.orgschart.dao.EmployeeRepository;
import com.springproject.orgschart.dao.LevelRepository;
import com.springproject.orgschart.dto.EmployeeDTO;
import com.springproject.orgschart.dto.OrganizationChartDTO;
import com.springproject.orgschart.entity.Employee;
import com.springproject.orgschart.entity.Level;
import com.springproject.orgschart.service.EmployeeServiceImplementation;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the OrgsChartService.
 */
@SpringBootTest
class OrgsChartServiceTests {

    ApplicationContext context;
    EmployeeDTO employeeDTO;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private LevelRepository levelRepository;
    @Inject
    private EmployeeServiceImplementation employeeServiceImplementation;

    List<EmployeeDTO> employeeDTOs;

    @BeforeEach
    public void beforeEach() {
        employeeDTOs = Arrays.asList(
                new EmployeeDTO(2, "Suresh Raina", "Manager", 1L),
                new EmployeeDTO(3, "Mike Hussey", "QA", 2L),
                new EmployeeDTO(4, "Brendon Mcculum", "QA", 3L)
        );
    }

    /**
     * Unit test for getAllEmployeesSortedByName method.
     * It verifies that the list of employees is sorted in ascending order by name.
     * Also, it checks that DataNotFoundException is thrown when the list is empty.
     */

    @Test
    public void testGetAllEmployeesSortedByName() {

        List<Employee> employees = employeeDTOs.stream()
                .map(dto -> {
                    Employee manager = null;
                    if (dto.getManagerId() != null) {
                        // Retrieve the manager object using the managerId
                        manager = employeeRepository.findById(dto.getManagerId()).orElse(null);
                    }
                    return new Employee((long) dto.getId(), dto.getName(), dto.getJobTitle(), manager, null);
                })
                .collect(Collectors.toList());

        Collections.sort(employees, Comparator.comparing(Employee::getName));

        when(employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "name")))
                .thenReturn(employees);

        List<EmployeeDTO> sortedEmployeeDTOs = employeeServiceImplementation.getAllEmployeesSortedByName();

        List<Employee> sortedEmployees = sortedEmployeeDTOs.stream()
                .map(dto -> {
                    Employee manager = null;
                    if (dto.getManagerId() != null) {
                        // Retrieve the manager object using the managerId
                        manager = employeeRepository.findById(dto.getManagerId()).orElse(null);
                    }
                    return new Employee((long) dto.getId(), dto.getName(), dto.getJobTitle(), manager, null);
                })
                .collect(Collectors.toList());

        Assertions.assertIterableEquals(employees, sortedEmployees);
        verify(employeeRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));

        // Verify DataNotFoundException is thrown when the list is empty
        when(employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "name")))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(DataNotFoundException.class, () -> employeeServiceImplementation.getAllEmployeesSortedByName());
    }

    /**
     * Unit test for getEmployeeOrganizationChart method.
     * It tests the organization chart retrieval for an employee.
     * It verifies that the returned OrganizationChartDTO contains the expected employee, manager, colleagues, and direct reports.
     * Additionally, it checks that an empty colleagues list is returned when the employee has no colleagues.
     * It also verifies that a null OrganizationChartDTO is returned when the employee is not found.
     *
     * @param employeeId The ID of the employee.
     */
    @Test
    public void testGetEmployeeOrganizationChart() {
        // Mock employee data
        Level leadLevel = new Level(3, "Lead");
        Employee manager = new Employee(1L, "MS Dhoni", "Lead", null, leadLevel);
        Level developerLevel = new Level(4, "Developer");
        Employee employee = new Employee(2L, "Suresh Raina", "Developer", manager, developerLevel);
        List<Employee> colleagues = Arrays.asList(
                new Employee(3L, "Yuvraj Singh", "QA", manager, developerLevel),
                new Employee(4L, "Ravindra Jadeja", "Developer", manager, developerLevel)
        );
        Level internLevel = new Level(4, "Intern");
        List<Employee> directReports = Collections.singletonList(
                new Employee(5L, "Ashok Dinda", "Intern", employee, internLevel)
        );

        List<Employee> allEmployees = Arrays.asList(
                new Employee(1L, "MS Dhoni", "Lead", null, leadLevel),
                new Employee(2L, "Suresh Raina", "Developer", manager, developerLevel),
                new Employee(3L, "Yuvraj Singh", "QA", manager, developerLevel),
                new Employee(4L, "Ravindra Jadeja", "Developer", manager, developerLevel),
                new Employee(5L, "Ashok Dinda", "Intern", employee, internLevel)
        );

        // Mock repository methods
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(employeeRepository.findByManager(manager)).thenReturn(colleagues);
        when(employeeRepository.findByManager(employee)).thenReturn(directReports);
        when(employeeRepository.findAll()).thenReturn(allEmployees);


        // Expected DTOs
        EmployeeDTO employeeDTO = new EmployeeDTO(2, "Suresh Raina", "Developer", 1L, null);
        EmployeeDTO managerDTO = new EmployeeDTO(1, "MS Dhoni", "Lead", null, null);
        List<EmployeeDTO> colleagueDTOs = colleagues.stream()
                .map(colleague -> new EmployeeDTO(Math.toIntExact(colleague.getId()), colleague.getName(), colleague.getJobTitle(), colleague.getManagerId(), null))
                .collect(Collectors.toList());
        List<EmployeeDTO> directReportDTOs = directReports.stream()
                .map(report -> new EmployeeDTO(Math.toIntExact(report.getId()), report.getName(), report.getJobTitle(), report.getManagerId(), null))
                .collect(Collectors.toList());

        // Call the method to be tested
        OrganizationChartDTO orgChartDTO = employeeServiceImplementation.getEmployeeOrganizationChart(2L);
        OrganizationChartDTO orgChartDTOTest = employeeServiceImplementation.getEmployeeOrganizationChart(1L);//to test for no colleagues case

        // Verify the returned DTO is correct
        assertEquals(employeeDTO, orgChartDTO.getEmployee());
        assertEquals(managerDTO, orgChartDTO.getManager());
        assertEquals(colleagueDTOs, orgChartDTO.getColleagues());
        assertEquals(directReportDTOs, orgChartDTO.getDirectReports());

        assertTrue(orgChartDTOTest.getColleagues().isEmpty());//for no colleagues case

        when(employeeRepository.findById(100L)).thenReturn(Optional.empty());
        OrganizationChartDTO orgChartDTOInvalid = employeeServiceImplementation.getEmployeeOrganizationChart(100L);
        assertNull(orgChartDTOInvalid);

    }

    /**
     * Unit test for addEmployee method.
     * It verifies that a new employee can be added successfully.
     * Additionally, it tests for various exceptions thrown when invalid employee data is provided.
     * It checks for NameNotNullException, MultipleDirectorException, InvalidManagerIdException, and DuplicateEmployeeException.
     */
    @Test
    public void testAddEmployee() {
        EmployeeDTO managerDTO = new EmployeeDTO(1, "MS DHONI", "Lead", null);
        EmployeeDTO employeeDTO = new EmployeeDTO(2, "Rituraj Gaikwad", "QA", 1L);
        Level leadLevel = new Level(3, "Lead");
        Level qaLevel = new Level(4, "QA");

        Employee manager = new Employee();
        manager.setId((long) managerDTO.getId());
        manager.setName(managerDTO.getName());
        manager.setJobTitle(managerDTO.getJobTitle());
        manager.setManager(null);

        List<Employee> allEmployees = Arrays.asList(
                new Employee(1L, "MS Dhoni", "Lead", null, leadLevel),
                new Employee(2L, "Rituraj Gaikwad", "QA", manager, qaLevel)
        );

        when(employeeRepository.findAll()).thenReturn(allEmployees);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(levelRepository.findByDesignation("Developer, DevOps, QA")).thenReturn(qaLevel);
        when(levelRepository.findByDesignation("Lead")).thenReturn(leadLevel);
        when(employeeRepository.existsByName(Mockito.anyString())).thenReturn(false);
        when(employeeRepository.existsByJobTitle("Director")).thenReturn(true);//for throwing multiple director exception
        Employee employee = new Employee();
        employee.setId((long) employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setJobTitle(employeeDTO.getJobTitle());
        employee.setManager(manager);
        when(employeeRepository.save(employee)).thenReturn(employee);
        EmployeeDTO result = employeeServiceImplementation.addEmployee(employeeDTO);
        assertNotNull(result);
        assertEquals(employeeDTO, result);
        EmployeeDTO test1 = new EmployeeDTO(5, "", "Intern", 4L);
        assertThrows(NameNotNullException.class, () -> {
            employeeServiceImplementation.addEmployee(test1);
        });
        EmployeeDTO test2 = new EmployeeDTO(1, "xyz", "Director", null);
        assertThrows(MultipleDirectorException.class, () -> {
            employeeServiceImplementation.addEmployee(test2);
        });
        EmployeeDTO test3 = new EmployeeDTO(4, "abc", "Lead", 199L);
        assertThrows(InvalidManagerIdException.class, () -> employeeServiceImplementation.addEmployee(test3));

        EmployeeDTO test4 = new EmployeeDTO(5, "Rituraj Gaikwad", "QA", 1L);
        when(employeeRepository.existsByName("Rituraj Gaikwad")).thenReturn(true);
        assertThrows(DuplicateEmployeeException.class, () -> employeeServiceImplementation.addEmployee(test4));

    }

    /**
     * Unit test for deleteEmployee method.
     * It verifies that an existing employee can be deleted successfully.
     * Additionally, it tests for cases where deletion is not possible, such as when the employee does not exist or is a director.
     * It checks for EmployeeNotFoundException in the case of a non-existent employee.
     */

    @Test
    public void testDeleteEmployee() {

        Long employeeId = 2L;
        Level manangerLevel = new Level(2, "Manager");
        Level directorLevel = new Level(1, "Director");
        Level leadLevel = new Level(3, "Lead");
        Employee director = new Employee(1L, "Sunil Gavaskar", "Director", null, directorLevel);
        Employee manager = new Employee(employeeId, "Virender Sehwag", "Manager", director, manangerLevel);
        Employee employee = new Employee(3L, "Sachin Tendulkar", "lead", manager, leadLevel);

        List<Employee> allEmployees = Arrays.asList(
                new Employee(1L, "Sunil Gavaskar", "Director", null, directorLevel),
                new Employee(employeeId, "Virender Sehwag", "Manager", director, manangerLevel),
                new Employee(3L, "Sachin Tendulkar", "lead", manager, leadLevel)
        );
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(director));
        when(employeeRepository.findAll()).thenReturn(allEmployees);
        when(employeeRepository.save(employee)).thenReturn(employee);
        Mockito.doNothing().when(employeeRepository).delete(manager);

        boolean result = employeeServiceImplementation.deleteEmployee(employeeId);
        assertTrue(result);
        boolean test1 = employeeServiceImplementation.deleteEmployee(1L);
        assertFalse(test1);
        assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImplementation.deleteEmployee(88L));

    }

    /**
     * Unit test for replaceEmployee method.
     * It verifies that an existing employee can be replaced with a new employee successfully.
     * It also tests for scenarios where replacement is not possible, such as when the manager or employee does not exist.
     * Additionally, it checks for NameNotNullException when the replacement employee's name is empty.
     */
    @Test
    public void testReplaceEmployee() {
        long employeeId = 3L;
        EmployeeDTO employeeDTO = new EmployeeDTO(Math.toIntExact(employeeId), "Virat Kohli", "Lead", 2L);
        EmployeeDTO directorDTO = new EmployeeDTO(1, "Chetan Sharma", "Director", null);
        EmployeeDTO faultyEmployeeDTO = new EmployeeDTO(Math.toIntExact(employeeId), "", "Lead", 2L);
        EmployeeDTO faultyDirectorDTO = new EmployeeDTO(1, "Madan Lal", "QA", 88L);

        Level manangerLevel = new Level(2, "Manager");
        Level directorLevel = new Level(1, "Director");
        Level leadLevel = new Level(3, "Lead");

        Employee director = new Employee(1L, "Ravi Shashtri", "Director", null, directorLevel);
        Employee manager = new Employee(2L, "Kapil Dev", "Manager", director, manangerLevel);
        Employee existingEmployee = new Employee(3L, "MS Dhoni", "Lead", manager, leadLevel);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(director));
        when(employeeRepository.findById(45L)).thenReturn(Optional.empty());
        when(levelRepository.findByDesignation("Lead")).thenReturn(leadLevel);
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
        boolean result = employeeServiceImplementation.replaceEmployee(3L, employeeDTO);
        assertTrue(result);

        boolean test1 = employeeServiceImplementation.replaceEmployee(1L, directorDTO);
        assertTrue(test1);
        boolean test2 = employeeServiceImplementation.replaceEmployee(1L, faultyDirectorDTO);
        assertFalse(test2);
        assertThrows(NameNotNullException.class, () -> employeeServiceImplementation.replaceEmployee(3L, faultyEmployeeDTO));
        boolean test3 = employeeServiceImplementation.replaceEmployee(45L, faultyEmployeeDTO);
        assertFalse(test3);

    }

    /**
     * Unit test for updateEmployeeDetails method.
     * It verifies that an existing employee's details can be updated successfully.
     * Additionally, it tests for various exceptions thrown when invalid employee data is provided.
     * It checks for NameNotNullException, NameAmbiguityException, and DirectorReplacementException.
     *
     * @param employeeId The ID of the employee to update.
     *                   * @param employeeDTO      The new details of the employee.
     *                   * @param faultyEmployeeDTO The employee DTO with faulty data (empty name).
     */

    @Test
    public void testUpdateEmployee() {
        long employeeId = 3L;
        EmployeeDTO employeeDTO = new EmployeeDTO(Math.toIntExact(employeeId), "Virat Kohli", "Manager", 1L);
        EmployeeDTO faultyEmployeeDTO = new EmployeeDTO(5, "", "Lead", 2L);

        Level manangerLevel = new Level(2, "Manager");
        Level directorLevel = new Level(1, "Director");
        Level leadLevel = new Level(3, "Lead");
        Employee director = new Employee(1L, "Ravi Shashtri", "Director", null, directorLevel);
        Employee manager = new Employee(2L, "Kapil Dev", "Manager", director, manangerLevel);
        Employee existingEmployee = new Employee(3L, "Virat Kohli", "Lead", manager, leadLevel);
        Employee faultyEmployee = new Employee(5L, "", "Intern", director, leadLevel);


        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(faultyEmployee));
        when(levelRepository.findByDesignation("Manager")).thenReturn(manangerLevel);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(director));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
        when(employeeRepository.findById(45L)).thenReturn(Optional.empty());
        boolean result = employeeServiceImplementation.updateEmployeeDetails(employeeId, employeeDTO);
        assertTrue(result);
        boolean test1 = employeeServiceImplementation.updateEmployeeDetails(45L, employeeDTO);
        assertFalse(test1);
        assertThrows(NameNotNullException.class, () -> employeeServiceImplementation.updateEmployeeDetails((long) faultyEmployeeDTO.getId(), faultyEmployeeDTO));
        assertThrows(NameAmbiguityException.class, () -> employeeServiceImplementation.updateEmployeeDetails(5L, employeeDTO));
        assertThrows(DirectorReplacementException.class, () -> employeeServiceImplementation.updateEmployeeDetails(1L, employeeDTO));


    }
}
