package com.springproject.orgschart;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
@OpenAPIDefinition(
        info = @Info(
                title = "Organization Chart Application",
                version = "1.0.0",
                description = "The Organization Charts project is a user-friendly Spring Boot application that streamlines employee tracking and management in organizations. \n ",
                termsOfService = "runcodenow",
                contact = @Contact(
                        name = "Mr Shrest Raj",
                        email = "shrestrajofficial@gmail.com"
                ),
                license = @License(
                        name = "License",
                        url = "runcodenow"

                )
        )
)
public class OrganizationChartsApplication {
    /**
     * The main method serves as the entry point for the OrgsChartApplication
     *
     * @param args The command line arguments
     */

    public static void main(String[] args) {
        SpringApplication.run(OrganizationChartsApplication.class, args);
    }

}
