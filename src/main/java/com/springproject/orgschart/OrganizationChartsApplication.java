package com.springproject.orgschart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* The class OrgschartApplication serves as a Spring Boot Application that facilitates automatic configuration
* and initiates component scanning. It also acts as the entry point for the Employee Management System application.
*
* @author Shrest Raj
* @version 1.0
*/

@SpringBootApplication
public class OrganizationChartsApplication {
    /**
    * The main method serves as the entry point for the OrgsChartApplication
    *
    *@param args The command line arguments
    */

    public static void main(String[] args) {
        SpringApplication.run(OrganizationChartsApplication.class, args);
    }

}
