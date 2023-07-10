package com.springproject.orgschart.dto;

import java.util.List;
import java.util.Objects;

public class OrganizationChartDTO {
    private EmployeeDTO employee;
    private EmployeeDTO manager;
    private List<EmployeeDTO> colleagues;
    private List<EmployeeDTO> directReports;

    public OrganizationChartDTO(EmployeeDTO employee, EmployeeDTO manager, List<EmployeeDTO> colleagues, List<EmployeeDTO> directReports) {
        this.employee = employee;
        this.manager = manager;
        this.colleagues = colleagues;
        this.directReports = directReports;
    }


    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public EmployeeDTO getManager() {
        return manager;
    }

    public void setManager(EmployeeDTO manager) {
        this.manager = manager;
    }

    public List<EmployeeDTO> getColleagues() {
        return colleagues;
    }

    public void setColleagues(List<EmployeeDTO> colleagues) {
        this.colleagues = colleagues;
    }

    public List<EmployeeDTO> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<EmployeeDTO> directReports) {
        this.directReports = directReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationChartDTO that = (OrganizationChartDTO) o;
        return Objects.equals(employee, that.employee) && Objects.equals(manager, that.manager) && Objects.equals(colleagues, that.colleagues) && Objects.equals(directReports, that.directReports);
    }


    @Override
    public int hashCode() {
        return Objects.hash(employee, manager, colleagues, directReports);
    }
}
