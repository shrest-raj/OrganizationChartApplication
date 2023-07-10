package com.springproject.orgschart.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "job_title")
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "manager_id") //multiple employees have same manager
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "level_id")//multiple employees can belong to the same level
    private Level level;

    // Default constructor
    public Employee() {
    }

    // Parameterized constructor
    public Employee(String name, String jobTitle, Employee manager, Level level) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.manager = manager;
        this.level = level;
    }

    public Employee(Long id, String name, String jobTitle, Employee manager, Level level) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.manager = manager;
        this.level = level;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Long getManagerId() {
        if (manager != null) {
            return manager.getId();
        }
        return null;
    }

    public void setManagerId(Long managerId) {
        if (managerId == null) {
            this.manager = null;
        } else {
            this.manager = new Employee();
            this.manager.setId(managerId);
        }
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", manager=" + manager +
                ", level=" + level +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(jobTitle, employee.jobTitle) && Objects.equals(manager, employee.manager) && Objects.equals(level, employee.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jobTitle, manager, level);
    }
}

