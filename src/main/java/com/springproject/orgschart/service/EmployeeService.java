package com.springproject.orgschart.service;

import com.springproject.orgschart.dto.EmployeeDTO;
import com.springproject.orgschart.dto.OrganizationChartDTO;

import java.util.List;

public interface EmployeeService {
    public List<EmployeeDTO> getAllEmployeesSortedByName();

    public OrganizationChartDTO getEmployeeOrganizationChart(Long employeeId);

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO);

    public boolean deleteEmployee(Long EmployeeId);

}
