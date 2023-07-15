package com.springproject.orgschart.service;

import com.springproject.orgschart.exceptions.*;
import com.springproject.orgschart.dao.EmployeeRepository;
import com.springproject.orgschart.dao.LevelRepository;
import com.springproject.orgschart.dto.EmployeeDTO;
import com.springproject.orgschart.dto.OrganizationChartDTO;
import com.springproject.orgschart.entity.Employee;
import com.springproject.orgschart.entity.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;


import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.valueOf;

/**
 * The EmployeeServiceImplementation class is responsible for handling employee-related operations
 * by implementing the EmployeeService interface. It interacts with the EmployeeRepository and LevelRepository
 * to access employee data and level information from the database.
 *
 * @Service Indicates that this class is a Spring service component that provides business logic for handling employees.
 */

@Service
public class EmployeeServiceImplementation implements EmployeeService {

    /**
     * The EmployeeRepository instance used to access employee data from the database.
     */
    @Autowired
    private final EmployeeRepository employeeRepository;
    /**
     * The LevelRepository instance used to access level information from the database.
     */
    @Autowired
    private final LevelRepository levelRepository;

    /**
     * Constructs a new EmployeeServiceImplementation with the specified EmployeeRepository and LevelRepository.
     *
     * @param employeeRepository The employee repository to be used for accessing employee data.
     * @param levelRepository    The level repository to be used for accessing level information.
     */
    public EmployeeServiceImplementation(EmployeeRepository employeeRepository, LevelRepository levelRepository) {
        this.employeeRepository = employeeRepository;
        this.levelRepository = levelRepository;
    }

    /**
     * Retrieves a list of all employees sorted by name.
     *
     * @return The list of EmployeeDTO objects representing all employees, sorted by name.
     * @throws DataNotFoundException if the list of employees is empty.
     */
    @Override
    public List<EmployeeDTO> getAllEmployeesSortedByName() {
        List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        if (employees.isEmpty()) {
            throw new DataNotFoundException();
        }
        return mapToEmployeeDTOList(employees);
    }

    /**
     * Retrieves the organization chart for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return The OrganizationChartDTO containing the employee's information, manager, colleagues, and direct reports.
     */
    @Override
    public OrganizationChartDTO getEmployeeOrganizationChart(Long employeeId) {
//        if(employeeId) is not an long which lies between 1 to employee count throw an exception employee count is not defined hard code here itself
        long employeeCount = employeeRepository.count();

        // Check if employeeId is a valid long value between 1 and employee count, throw an exception if not
        if (employeeId == null || !isNumeric(employeeId) || employeeId < 1 || employeeId > employeeCount) {
            throw new EmployeeIdNotValidException();
        }


        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {

            return null;
        }
        Employee employee = optionalEmployee.get();
        EmployeeDTO employeeDTO = mapToEmployeeDTO(employee);
        EmployeeDTO managerDTO = null;
        if (employee.getManager() != null) {
            managerDTO = mapToEmployeeDTO(employee.getManager());
        }
        List<EmployeeDTO> colleagues = getColleagues(employee);
        List<EmployeeDTO> directReports = getDirectReports(employee);
        return new OrganizationChartDTO(employeeDTO, managerDTO, colleagues, directReports);


    }

    /**
     * Adds a new employee to the database.
     *
     * @param employeeDTO The EmployeeDTO object representing the new employee to be added.
     * @return The EmployeeDTO object representing the newly added employee.
     * @throws NameNotNullException         if the employee name is null or empty.
     * @throws InvalidManagerLevelException if the employee's manager level is invalid.
     * @throws DuplicateEmployeeException   if an employee with the same name already exists.
     * @throws MultipleDirectorException    if multiple directors are attempted to be added.
     * @throws InvalidManagerIdException    if the specified manager ID is invalid(exceeds the number of employees in the db).
     */
    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        if (Objects.equals(employeeDTO.getName(), null) || employeeDTO.getName().trim().isEmpty() || !isValidName(employeeDTO.getName()) || employeeDTO.getJobTitle() == null || employeeDTO.getJobTitle().trim().isEmpty() || !levelRepository.existsByDesignation(employeeDTO.getJobTitle()) || employeeDTO.getJobTitle().equalsIgnoreCase("director") || employeeDTO.getManagerId() > getTotalEmployeeCount() || isInvalidManagerLevel(employeeDTO) || employeeDTO.getManagerId() == null) {
            if (Objects.equals(employeeDTO.getName(), null) || employeeDTO.getName().trim().isEmpty() || !isValidName(employeeDTO.getName())) {
                throw new NameNotNullException();
            } else if (employeeDTO.getJobTitle() == null || employeeDTO.getJobTitle().trim().isEmpty() || !levelRepository.existsByDesignation(employeeDTO.getJobTitle())) {
                throw new InvalidJobTitleException();
            } else if ((employeeDTO.getManagerId() == null || employeeDTO.getJobTitle().equalsIgnoreCase("director")) && employeeRepository.existsByJobTitle("Director")) {
                throw new MultipleDirectorException();
            } else if (employeeDTO.getManagerId() > getTotalEmployeeCount()) {
                throw new InvalidManagerIdException();
            } else {
                throw new InvalidManagerLevelException();
            }
        }

        if (employeeRepository.existsByName(employeeDTO.getName())) {
            throw new DuplicateEmployeeException();
        }

        // Check if there is already a director in the database
        if (employeeDTO.getJobTitle().equals("Director") && employeeRepository.existsByJobTitle("director")) {
            throw new MultipleDirectorException();
        }

        Employee employee = mapToEmployee(employeeDTO);
        Level levelEmployee = levelRepository.findByDesignation(employee.getJobTitle());
        employee.setLevel(levelEmployee);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToEmployeeDTO(savedEmployee);
    }

    /**
     * Replaces the details of an existing employee with new details.
     *
     * @param employeeId  The ID of the employee to be replaced.
     * @param employeeDTO The updated EmployeeDTO object representing the employee's new details.
     * @return true if the employee is successfully replaced, false otherwise.
     */

    public boolean replaceEmployee(Long employeeId, EmployeeDTO employeeDTO) {

        long employeeCount = employeeRepository.count();

        if (employeeId == null || !isNumeric(employeeId) || employeeId < 1 || employeeId > employeeCount) {
            throw new EmployeeIdNotValidException();
        } else if (Objects.equals(employeeDTO.getName(), null) || employeeDTO.getName().trim().isEmpty() || !isValidName(employeeDTO.getName())) {
            throw new NameNotNullException();
        } else if (employeeDTO.getJobTitle() == null || employeeDTO.getJobTitle().trim().isEmpty() || !levelRepository.existsByDesignation(employeeDTO.getJobTitle())) {
            throw new InvalidJobTitleException();
        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {
            return false;
        }
        if (Objects.equals(employeeDTO.getName(), "")) {
            throw new NameNotNullException();
        }

        Employee existingEmployee = optionalEmployee.get();
        if (employeeId == 1) {
            if ("Director".equals(employeeDTO.getJobTitle()) && employeeDTO.getManagerId() == null) {
                existingEmployee.setName(employeeDTO.getName());
                existingEmployee.setJobTitle(employeeDTO.getJobTitle());
                existingEmployee.setManagerId(employeeDTO.getManagerId());
                employeeRepository.save(existingEmployee);
                return true;
            } else {
                return false;
            }
        }

        String employeeJobTitle = convertJobTitle(employeeDTO.getJobTitle());
        Optional<Level> optionalLevel = Optional.ofNullable(levelRepository.findByDesignation(employeeJobTitle));
        if (optionalLevel.isEmpty()) {
            return false;
        }

        Level level = optionalLevel.get();

        if (level.getLevelId() != existingEmployee.getLevel().getLevelId() ||
                !employeeDTO.getManagerId().equals(existingEmployee.getManagerId())) {
            return false;
        }

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setJobTitle(employeeDTO.getJobTitle());
        existingEmployee.setManagerId(employeeDTO.getManagerId());

        employeeRepository.save(existingEmployee);

        return true;
    }

    /**
     * Updates the details of an existing employee with new details.
     *
     * @param employeeId  The ID of the employee to be replaced.
     * @param employeeDTO The updated EmployeeDTO object representing the employee's new details.
     * @return true if the employee is successfully updated, false otherwise.
     */
    public boolean updateEmployeeDetails(Long employeeId, EmployeeDTO employeeDTO) {
        // Retrieve the existing employee
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {
            return false;
        }
        if (Objects.equals(employeeDTO.getName(), "")) {
            throw new NameNotNullException();
        }
        if (employeeId == 1 || Objects.equals(employeeDTO.getJobTitle(), "Director")) {
            throw new DirectorReplacementException();
        }
        if (!Objects.equals(employeeDTO.getName(), optionalEmployee.get().getName())) {
            throw new NameAmbiguityException();
        }

        Employee existingEmployee = optionalEmployee.get();

        if (employeeDTO.getJobTitle() != null) {
            existingEmployee.setJobTitle(employeeDTO.getJobTitle());

        } else {
            return false;
        }
        if (employeeDTO.getManagerId() != null) {
            String employeeJobTitle = convertJobTitle(employeeDTO.getJobTitle());
            Optional<Level> optionalLevel = Optional.ofNullable(levelRepository.findByDesignation(employeeJobTitle));

            if (optionalLevel.isEmpty()) {
                return false;
            }

            Level level = optionalLevel.get();
            int employeeLevelId = level.getLevelId();

            Employee manager = employeeRepository.findById(employeeDTO.getManagerId()).orElse(null);

            if (manager == null) {
                return false;
            }

            Level managerLevel = manager.getLevel();
//            int levelDiff = Math.abs(managerLevel.getLevelId() - employeeLevelId);

            if (managerLevel.getLevelId() >= employeeLevelId) {
                return false;
            }

            existingEmployee.setManagerId(employeeDTO.getManagerId());
        } else {
            return false;
        }

        employeeRepository.save(existingEmployee);

        return true;
    }


    /**
     * Deletes an employee from the database.
     *
     * @param employeeId The ID of the employee to be deleted.
     * @return true if the employee is successfully deleted, false otherwise.
     * @throws EmployeeNotFoundException if the employee with the specified ID is not found.
     */

    @Override
    public boolean deleteEmployee(Long employeeId) {
        long employeeCount = employeeRepository.count();

        // Check if employeeId is a valid long value between 1 and employee count, throw an exception if not
        if (employeeId == null || !isNumeric(employeeId) || employeeId < 1 || employeeId > employeeCount) {
            throw new EmployeeIdNotValidException();
        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            if (employee.getJobTitle().equals("Director") && hasDirectReports(employee)) {
                return false;
            }

            List<Employee> directReports = getDirectReports(employeeId);
            if (!directReports.isEmpty()) {
                Employee manager = employee.getManager();
                for (Employee directReport : directReports) {
                    directReport.setManager(manager);
                    employeeRepository.save(directReport);
                }
            }
            employeeRepository.delete(employee);
            return true;
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    /**
     * Retrieves a list of direct reports for a given employee ID.
     *
     * @param employeeId The ID of the employee.
     * @return The list of Employee objects representing direct reports.
     */

    private List<Employee> getDirectReports(Long employeeId) {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> directReports = new ArrayList<>();
        for (Employee employee : allEmployees) {
            if (employee.getManager() != null && employee.getManager().getId().equals(employeeId)) {
                directReports.add(employee);
            }
        }
        return directReports;
    }

    /**
     * Checks if an employee has any direct reports.
     *
     * @param employee The employee.
     * @return true if the employee has direct reports, false otherwise.
     */
    private boolean hasDirectReports(Employee employee) {
        List<Employee> directReports = getDirectReports(employee.getId());
        return !directReports.isEmpty();
    }

    /**
     * Retrieves the total number of employees in the database.
     *
     * @return The total number of employees.
     */
    public int getTotalEmployeeCount() {
        List<Employee> employees = employeeRepository.findAll();
        int count = 0;
        for (Employee employee : employees) {
            count++;
        }
        return count;
    }

    /**
     * Retrieves a list of colleagues for a given employee.
     *
     * @param employee The employee.
     * @return The list of EmployeeDTO objects representing colleagues.
     */

    private List<EmployeeDTO> getColleagues(Employee employee) {
        if (employee.getManager() != null) {

            Employee manager = employee.getManager();
            List<EmployeeDTO> colleagues = getColleaguesByManagerId(manager);
            colleagues.remove(mapToEmployeeDTO(employee));
            return colleagues;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves a list of direct reports for a given employee.
     *
     * @param employee The employee.
     * @return The list of EmployeeDTO objects representing direct reports.
     */

    private List<EmployeeDTO> getDirectReports(Employee employee) {
        return getDirectReportsByManagerId(Math.toIntExact(employee.getId()));
    }

    /**
     * Retrieves a list of colleagues for a given manager.
     *
     * @param manager The manager employee.
     * @return The list of EmployeeDTO objects representing colleagues.
     */

    private List<EmployeeDTO> getColleaguesByManagerId(Employee manager) {
        List<EmployeeDTO> colleagues = new ArrayList<>();
        Long managerId = manager.getId();

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {

            if (Objects.equals(employee.getManagerId(), managerId)) {
                colleagues.add(mapToEmployeeDTO(employee));
            }
        }

        return colleagues;
    }

    /**
     * Retrieves a list of direct reports for a given manager ID.
     *
     * @param managerId The ID of the manager.
     * @return The list of EmployeeDTO objects representing direct reports.
     */
    private List<EmployeeDTO> getDirectReportsByManagerId(int managerId) {
        List<EmployeeDTO> directReports = new ArrayList<>();

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {

            if (employee.getManager() != null && employee.getManager().getId() == managerId) {
                directReports.add(mapToEmployeeDTO(employee));
            }
        }

        return directReports;
    }

    /**
     * Checks if the manager level specified in the EmployeeDTO object is invalid.
     *
     * @param employeeDTO The EmployeeDTO object.
     * @return true if the manager level is invalid, false otherwise.
     */

    private boolean isInvalidManagerLevel(EmployeeDTO employeeDTO) {
        Employee employee = mapToEmployee(employeeDTO);

        Optional<Employee> manager = employeeRepository.findById(employeeDTO.getManagerId());

        String employeeJobTitle = convertJobTitle(employee.getJobTitle());
        String managerJobTitle = convertJobTitle(manager.get().getJobTitle());
        Level levelEmployee = levelRepository.findByDesignation(employeeJobTitle);
        Level levelManager = levelRepository.findByDesignation(managerJobTitle);
        int levelDiff = Math.abs(levelEmployee.getLevelId() - levelManager.getLevelId());

        return levelManager.getLevelId() >= levelEmployee.getLevelId() || levelDiff != 1;
    }

    /**
     * Converts a job title string to a standardized format(because in level table we have same level for many designation).
     *
     * @param jobTitle The job title.
     * @return The standardized job title string.
     */

    private String convertJobTitle(String jobTitle) {
        if (Objects.equals(jobTitle, "Developer") || Objects.equals(jobTitle, "DevOps") || Objects.equals(jobTitle, "QA")) {
            return "Developer, DevOps, QA";
        }
        return jobTitle;
    }

    /**
     * Maps a list of Employee objects to a list of EmployeeDTO objects.
     *
     * @param employees The list of Employee objects to be mapped.
     * @return The list of EmployeeDTO objects.
     */
    private List<EmployeeDTO> mapToEmployeeDTOList(List<Employee> employees) {
        return employees.stream().map(this::mapToEmployeeDTO).collect(Collectors.toList());//references the mapToEmployeeDTO
    }

    /**
     * Maps an Employee object to an EmployeeDTO object.
     *
     * @param employee The Employee object to be mapped.
     * @return The EmployeeDTO object.
     */
    private EmployeeDTO mapToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(Math.toIntExact(employee.getId()));
        employeeDTO.setName(employee.getName());
        employeeDTO.setJobTitle(employee.getJobTitle());
        if (employee.getManager() != null) {
            employeeDTO.setManagerId(employee.getManager().getId());
        }
        return employeeDTO;
    }

    /**
     * Maps an EmployeeDTO object to an Employee object.
     *
     * @param employeeDTO The EmployeeDTO object to be mapped.
     * @return The Employee object.
     */
    private Employee mapToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(valueOf((long) employeeDTO.getId()));
        employee.setName(employeeDTO.getName());
        employee.setJobTitle(employeeDTO.getJobTitle());
        if (employeeDTO.getManagerId() != null) {
            Optional<Employee> optionalManager = employeeRepository.findById(employeeDTO.getManagerId());
            optionalManager.ifPresent(employee::setManager);
        }
        return employee;
    }

    /**
     * Checks if an employee with the given ID exists.
     *
     * @param employeeId The ID of the employee.
     * @return true if the employee exists, false otherwise.
     */
    public boolean employeeExists(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    private boolean isNumeric(Long value) {
        try {
            Long.parseLong(String.valueOf(value));

        } catch (NumberFormatException e) {
            return false;
        }
        return true;

    }

    private boolean isValidName(String name) {
        // Regular expression pattern to match valid name format
        String namePattern = "[A-Za-z]+";

        // Check if the name matches the pattern
        return Pattern.matches(namePattern, name);
    }


}
