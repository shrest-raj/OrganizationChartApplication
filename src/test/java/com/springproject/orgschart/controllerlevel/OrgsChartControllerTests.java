package com.springproject.orgschart.controllerlevel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.orgschart.controller.EmployeeController;
import com.springproject.orgschart.dto.EmployeeDTO;
import com.springproject.orgschart.dto.OrganizationChartDTO;
import com.springproject.orgschart.service.EmployeeServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrgsChartControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeServiceImplementation employeeServiceImplementation;
    @InjectMocks
    private EmployeeController employeeController;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Unit test for getAllEmployees method.
     * It verifies that all employees are retrieved successfully.
     */
    @Test
    public void testGetAllEmployees() throws Exception {
        List<EmployeeDTO> mockEmployees = Arrays.asList(
                new EmployeeDTO(1, "MS DHONI", "Director", null),
                new EmployeeDTO(2, "Virat Kohli", "Manager", 1L),
                new EmployeeDTO(3, "Rohit Sharma", "Lead", 2L)
        );
        when(employeeServiceImplementation.getAllEmployeesSortedByName()).thenReturn(mockEmployees);

        ResultActions result = mockMvc.perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("MS DHONI")))
                .andExpect(jsonPath("$[0].jobTitle", is("Director")))
                .andExpect(jsonPath("$[0].managerId", nullValue()))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Virat Kohli")))
                .andExpect(jsonPath("$[1].jobTitle", is("Manager")))
                .andExpect(jsonPath("$[1].managerId", is(1)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Rohit Sharma")))
                .andExpect(jsonPath("$[2].jobTitle", is("Lead")))
                .andExpect(jsonPath("$[2].managerId", is(2)));
    }

    /**
     * Positive unit test for getEmployeeOrganizationChart method.
     * It verifies that the organization chart for an employee is retrieved successfully.
     */
    @Test
    public void testGetEmployeeOrganizationChart() throws Exception {
        Long employeeId = 2L;
        EmployeeDTO employee = new EmployeeDTO(2, "Virat Kohli", "Manager", 1L);
        EmployeeDTO manager = new EmployeeDTO(1, "MS DHONI", "Director", null);
        List<EmployeeDTO> colleagues = Arrays.asList(
                new EmployeeDTO(3, "Ravindra Jadeja", "Manager", 1L),
                new EmployeeDTO(3, "Shrest Raj", "Manager", 1L)
        );
        List<EmployeeDTO> directReports = Arrays.asList(
                new EmployeeDTO(3, "Rohit Sharma", "Lead", 2L),
                new EmployeeDTO(3, "Shivam Dube", "Lead", 2L)
        );
        OrganizationChartDTO expectedChart = new OrganizationChartDTO(employee, manager, colleagues, directReports);
        when(employeeServiceImplementation.getEmployeeOrganizationChart(employeeId)).thenReturn(expectedChart);

        mockMvc.perform(get("/api/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ResponseEntity<OrganizationChartDTO> response = employeeController.getEmployeeOrganizationChart(employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedChart, response.getBody());
    }

    /**
     * Unit test for addEmployee method.
     * It verifies that a new employee can be added successfully.
     */
    @Test
    public void testAddEmployee() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO("Sachin Tendulkar", "Director", null);
        EmployeeDTO createdEmployee = new EmployeeDTO(1, "Sachin Tendulkar", "Director", null);

        when(employeeServiceImplementation.addEmployee(employeeDTO)).thenReturn(createdEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Sachin Tendulkar\",\"jobTitle\":\"Director\",\"managerId\":null}"))
                .andExpect(status().isCreated());

        ResponseEntity<EmployeeDTO> response = employeeController.addEmployee(employeeDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdEmployee, response.getBody());

    }

    /**
     * unit test for updateEmployee method (successful replace).
     * It verifies that an existing employee's details can be replaced successfully.
     */
    @Test
    public void testUpdateEmployeeReplaceSuccessful() throws Exception {
        Long employeeId = 3L;
        EmployeeDTO employeeDTO = new EmployeeDTO(3, "Sachin Tendulkar", "Intern", 2L, true);
        when(employeeServiceImplementation.employeeExists(employeeId)).thenReturn(true);
        when(employeeServiceImplementation.replaceEmployee(employeeId, employeeDTO)).thenReturn(true);
        mockMvc.perform(put("/api/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3,\"name\":\"Sachin Tendulkar\",\"jobTitle\":\"Intern\",\"managerId\":2,\"replace\":true}"))
                .andExpect(status().isOk());

        // Verify the response
        ResponseEntity<String> response = employeeController.updateEmployee(employeeId, employeeDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee updated successfully.", response.getBody());
    }

    /**
     * Unit test for updateEmployee method (failed replace).
     * It tests the scenario when the replacement of the employee fails.
     * This can happen when the combination of employee ID, job title, and manager ID is not valid according to the hierarchy.
     */
    @Test
    public void testUpdateEmployee_ReplaceFailed() throws Exception {
        Long employeeId = 3L;
        EmployeeDTO employeeDTO = new EmployeeDTO("Sachin Tendulkar", "Director", null, true);
        when(employeeServiceImplementation.employeeExists(employeeId)).thenReturn(true);
        when(employeeServiceImplementation.replaceEmployee(employeeId, employeeDTO)).thenReturn(false);
        mockMvc.perform(put("/api/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sachin Tendulkar\",\"jobTitle\":\"Manager\",\"managerId\":1,\"replace\":true}"))
                .andExpect(status().isBadRequest());

        // Verify the response
        ResponseEntity<String> response = employeeController.updateEmployee(employeeId, employeeDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        System.out.println(response.getBody());
        assertEquals("The replacement of the employee failed. \nPlease verify if the combination of the employee ID, job title, and manager ID is valid according to the hierarchy.PLease keep in mind that to replace a employee the level must be same as the existing employee", response.getBody());
    }

    /**
     * Unit test for updateEmployee method (employee not found).
     * It tests the scenario when the employee to be updated is not found.
     */
    @Test
    public void testUpdateEmployee_EmployeeNotFound() throws Exception {
        Long employeeId = 88L;
        EmployeeDTO employeeDTO = new EmployeeDTO("Sachin Tendulkar", "Manager", null, false);
        when(employeeServiceImplementation.employeeExists(employeeId)).thenReturn(false);


        mockMvc.perform(put("/api/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"jobTitle\":\"Manager\",\"managerId\":1,\"replace\":true}"))
                .andExpect(status().isNotFound());

    }

    /**
     * Unit test for deleteEmployee method .
     * It tests the deletion of the employee with the mentioned employeeId.
     */
    @Test
    public void testDeleteEmployee_Success() {
        Long employeeId = 11L;
        when(employeeServiceImplementation.deleteEmployee(employeeId)).thenReturn(true);

        ResponseEntity<String> response = employeeController.deleteEmployee(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully.", response.getBody());
    }

    /**
     * Unit test for deleteEmployee method .
     * It tests the scenario of deletion of the director which is forbidden according to the logic.
     */
    @Test
    public void testDeleteEmployee_Forbidden() {
        Long employeeId = 1L;
        when(employeeServiceImplementation.deleteEmployee(employeeId)).thenReturn(false);

        ResponseEntity<String> response = employeeController.deleteEmployee(employeeId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Cannot delete Director with direct reports.", response.getBody());
    }


}
