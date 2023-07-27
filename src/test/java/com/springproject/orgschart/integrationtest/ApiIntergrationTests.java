package com.springproject.orgschart.integrationtest;

import com.springproject.orgschart.dto.EmployeeDTO;
import com.springproject.orgschart.dto.OrganizationChartDTO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//while bootstraping the app context run with a random port
public class ApiIntergrationTests {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @BeforeAll
    @Sql({"/data.sql", "/import_data.sql"})
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/employees");
        System.out.println("Test method '" + testInfo.getTestMethod() + "' is being executed.");
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        System.out.println("Test method " + testInfo.getTestMethod() + " is completed.");
    }


    @Test
    public void testAddEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO("Shrest Raj", "Intern", 12L);
        EmployeeDTO response = restTemplate.postForObject(baseUrl, employeeDTO, EmployeeDTO.class);
        assert response != null;
        assertEquals("Shrest Raj", response.getName());
        assertEquals("Intern", response.getJobTitle());
        List<EmployeeDTO> employeeDTOList = restTemplate.getForObject(baseUrl, List.class);
        assertEquals(17, employeeDTOList.size());
    }

    @Test
    public void testGetEmployee() {
        List<EmployeeDTO> employeeDTOList = restTemplate.getForObject(baseUrl, List.class);
        assertEquals(16, employeeDTOList.size());
    }

    @Test
    public void testGetEmployeeById() {
        // Assuming there's an existing employee with ID 1 in the database
        int employeeId = 4;
        String getEmployeeUrl = baseUrl + "/" + employeeId;
        OrganizationChartDTO organizationChartDTO = restTemplate.getForObject(getEmployeeUrl, OrganizationChartDTO.class);
        assertNotNull(organizationChartDTO);
        assertEquals("Neha Gupta", organizationChartDTO.getEmployee().getName());
    }

    @Test
    public void testDeleteEmployeeById() {
        List<EmployeeDTO> employeeDTOList = restTemplate.getForObject(baseUrl, List.class);
        assert employeeDTOList != null;
        assertEquals(16, employeeDTOList.size());
        int employeeId = 9;
        String deleteEmployeeUrl = baseUrl + "/" + employeeId;
        restTemplate.delete(deleteEmployeeUrl);
        List<EmployeeDTO> employeeDTOListNew = restTemplate.getForObject(baseUrl, List.class);
        assert employeeDTOListNew != null;
        assertEquals(15, employeeDTOListNew.size());
    }

    @Test
    public void testUpdateEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO("MS Dhoni", "DevOps", 5L, false);
        int employeeId = 10;
        String putEmployeeUrl = baseUrl + "/" + employeeId;
        restTemplate.put(putEmployeeUrl, EmployeeDTO.class);
        String getEmployeeUrl = baseUrl + "/" + employeeId;
        OrganizationChartDTO organizationChartDTO = restTemplate.getForObject(getEmployeeUrl, OrganizationChartDTO.class);
        assertNotNull(organizationChartDTO);
        assertEquals("MS Dhoni", organizationChartDTO.getEmployee().getName());
    }

}
