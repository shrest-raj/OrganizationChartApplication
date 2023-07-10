package com.springproject.orgschart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class EmployeeDTO {
    private int id;
    private String name;
    private String jobTitle;
    private Long managerId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean replace;


    // Constructors
    public EmployeeDTO() {
    }

    public EmployeeDTO(String name, String jobTitle, Long managerId, Boolean replace) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerId = managerId;
        this.replace = replace;
    }

    public EmployeeDTO(int id, String name, String jobTitle, Long managerId) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerId = managerId;
    }

    public EmployeeDTO(int id, String name, String jobTitle, Long managerId, Boolean replace) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerId = managerId;
        this.replace = replace;
    }

    public EmployeeDTO(String name, String jobTitle, Long managerId) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerId = managerId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }


    public Boolean getReplace() {
        return replace;
    }

    public void setReplace(Boolean replace) {
        this.replace = replace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDTO that = (EmployeeDTO) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(jobTitle, that.jobTitle) && Objects.equals(managerId, that.managerId) && Objects.equals(replace, that.replace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jobTitle, managerId, replace);
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", managerId=" + managerId +
                ", replace=" + replace +
                '}';
    }
}

