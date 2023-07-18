
# Organization Charts : Streamlined Employee Tracking and Management System

This is a user-friendly Spring Boot application that offers a REST API implementation for various operations in an Employee Management System.


## Table Of Contents
- [Introduction](#introduction)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Deployment](#deployment)
- [Project Documentation](#project-documentation)
- [Acknowledgements](#acknowledgements)


## Introduction
The Organization Charts project is a user-friendly Spring Boot application that streamlines employee tracking and management in organizations.

It offers REST API endpoints for efficient operations on employee information and organizational hierarchy. 

The application brings all employee data together, including their roles, reporting structure, and information about colleagues. Through the REST API, users can easily access, modify, add, and remove employee details, with built-in rules ensuring proper hierarchy constraints and restrictions.

The application offers the flexibility of using either MySQL or H2 (in-memory) databases, allowing users to choose their preferred option for data persistence.


## Installation

To install the Organization Charts application, please follow the steps below:

- Ensure that you have Java Development Kit (JDK) installed on your system. The recommended version is JDK 17 or above.

- Download or clone the project repository from the designated source.
  - ```git clone https://github.com/shrest-raj/OrganizationChartApplication```
  - This command will clone this application in your local machine.
- Open a terminal or command prompt and navigate to the project directory.

- If you plan to use MySQL as the database, ensure that you have MySQL installed and running on your system. Create a new database for the application if needed.

- If you prefer to use the H2 in-memory database, no additional configuration is required.

- Open the application.properties file in the project's resources directory.

- Update the database configuration properties accordingly, in the spring active profiles you can set the database you prefer .

- Build the project using Maven by executing the following command: 

    - ```mvn clean install```

- Once the build process completes successfully, you can run the application using the following command:

    - ```mvn spring-boot:run```

- The application will start, and you should see the console output indicating that the server is running.

- Open a web browser and navigate to http://localhost:9091 (port number can be configured in the application.properties file) to access the application.

## Usage

The Organization Charts application provides a set of REST API endpoints for managing employee information and organizational hierarchy. Here's how you can use the application:

- **Get all employees**:
    - Endpoint: **`GET /employees`**
    - This endpoint retrieves a list of all employees in the organization.

- **Get organization chart for an employee:**
    - Endpoint: **`GET /employees/{employeeId}/organization-chart`**
    - Replace **`{employeeId}`** in the URL with the unique identifier of the employee.
    - This endpoint returns the organization chart for the specified employee. It includes employee information, their manager's details, colleague information (excluding the employee), and all employees reporting to the employee.
- **Add an employee:**

    - Endpoint: **`POST /employees`**
    - Send a POST request to this endpoint with a JSON payload containing the employee details.
    - The payload should include the employee's name, job title, and manager's ID (if applicable).
- **Update an employee or replace with another employee:**

    - Endpoint: **`PUT /employees/{employeeId}`**
    - Replace **`{employeeId}`** in the URL with the unique identifier of the employee.
    - Send a PUT request to this endpoint with a JSON payload containing the updated employee details.
    - Include the "replace": true flag in the payload to replace the employee with a different employee or the "replace": true flag to just change the details of the employee.

- **Remove an employee:**

    - Endpoint: **`DELETE /employees/{employeeId}`**
    - Replace {employeeId} in the URL with the unique identifier of the employee.
    - This endpoint removes the specified employee from the organization. If any employees were reporting to the removed employee, they will now report directly to the removed employee's manager.




## API Documentation

#### GET /api/employees

Returns a list of all the employees

Request
```
GET /api/employees
```

Response
``` json
[
    {
        "id": 3,
        "name": "Aarti Patel",
        "jobTitle": "Manager",
        "managerId": 1
    },
    {
        "id": 5,
        "name": "Amit Singh",
        "jobTitle": "Lead",
        "managerId": 2
    },
    {
        "id": 1,
        "name": "Amitabh Singh",
        "jobTitle": "Director",
        "managerId": null
    },
    {
        "id": 12,
        "name": "Ankit Verma",
        "jobTitle": "QA",
        "managerId": 4
    },
    {
        "id": 8,
        "name": "Deepak Sharma",
        "jobTitle": "Developer",
        "managerId": 5
    },
    {
        "id": 13,
        "name": "Kavita Singh",
        "jobTitle": "QA",
        "managerId": 5
    },
    {
        "id": 7,
        "name": "Manish Reddy",
        "jobTitle": "Developer",
        "managerId": 4
    },
    {
        "id": 4,
        "name": "Neha Gupta",
        "jobTitle": "Lead",
        "managerId": 2
    },
    {
        "id": 15,
        "name": "Nisha Sharma",
        "jobTitle": "Intern",
        "managerId": 7
    },
    {
        "id": 9,
        "name": "Pooja Gupta",
        "jobTitle": "Developer",
        "managerId": 4
    },
    {
        "id": 6,
        "name": "Priya Verma",
        "jobTitle": "Developer",
        "managerId": 3
    },
    {
        "id": 2,
        "name": "Rahul Sharma",
        "jobTitle": "Manager",
        "managerId": 1
    },
    {
        "id": 10,
        "name": "Rajesh Kumar",
        "jobTitle": "DevOps",
        "managerId": 5
    },
    {
        "id": 16,
        "name": "Rakesh Kumar",
        "jobTitle": "Intern",
        "managerId": 7
    },
    {
        "id": 14,
        "name": "Sanjay Patel",
        "jobTitle": "Intern",
        "managerId": 7
    },
    {
        "id": 11,
        "name": "Smita Sharma",
        "jobTitle": "DevOps",
        "managerId": 5
    }
]

```

### GET /api/employees/{employeeId}

Returns a list of detailed employee chart for the given employee

Request
```
GET /api/employees/11
```

Response
```json
{
    "employee": {
        "id": 11,
        "name": "Smita Sharma",
        "jobTitle": "DevOps",
        "managerId": 5
    },
    "manager": {
        "id": 5,
        "name": "Amit Singh",
        "jobTitle": "Lead",
        "managerId": 2
    },
    "colleagues": [
        {
            "id": 8,
            "name": "Deepak Sharma",
            "jobTitle": "Developer",
            "managerId": 5
        },
        {
            "id": 10,
            "name": "Rajesh Kumar",
            "jobTitle": "DevOps",
            "managerId": 5
        },
        {
            "id": 13,
            "name": "Kavita Singh",
            "jobTitle": "QA",
            "managerId": 5
        }
    ],
    "directReports": []
}
```



### POST /api/employees

Adds a new employee to the organization

Body
```json
{
  "name": "String Required - Employee Name",
  "jobTitle": "String Required - Employee Designation",
  "managerId": "Integer  - Manager Employee ID"
}
```

Request
```
POST /api/employees
body: {
    "name" : "Shrest",
    "jobTitle" : "Intern",
    "managerId" : "12"
}
```

Response

```json
{
    "id": 17,
    "name": "Shrest",
    "jobTitle": "Intern",
    "managerId": 12
}
```

#### PUT /api/employees/${employeeId}

Updates or replaces an existing employee depending upon the value of the replace flag

Body
```json
{
  "name": "String Required - Employee Name",
  "jobTitle": "String Required - Employee Designation",
  "managerId": "Integer - Manager Employee ID",
  "replace": "Boolean Optional - Replace old employee with current employee(TRUE) or just change the details of the employee "
}
```

Request
```
PUT /api/employees/2
body: {
    "name" : "Ankit Verma",
    "jobTitle" : "Developer",
    "managerId" : "5",
    "replace" : false
}
```

Response
```
Employee updated successfully.
```

### DELETE /api/employees/${employeeId}

Deletes an employee having employee id as employeeId

Request
```
DELETE /api/employees/10
```

Response
```
Employee deleted successfully.
```

## Database Schema

#### Level
- level_id: int (Primary Key)
- designation: String

#### Employee
- employee_id: Long (Primary Key)
- name: String
- job_title: String
- manager_id: Long
- level_id: int (Foreign key)

### Table Instances for both the tables( Level and Employee )

Level

| level_id | designation |
| -------- | ----------- |
|    1     |   Director  |
|    2     |   Manager   |
|    3     |   Lead      |
|    4     |   Developer, DevOps, QA |
|    5     |   Intern    |

Employee

| employee_id | name   | manager_id | job_title | level_id |
| ----------- | --------------- | ---------- | --------- | ------ |
|      1      |     Amitabh Singh       |     null    | Director  |    1   |
|      2      |     Rahul Sharma   |      1     | Manger    |    2   |
|      3      |     Aarti Patel        |      1     | Manager     |    2  |
|      4      | Neha Gupta |      2    | Lead  |    3   |
|      5      |    Amit Singh |      2     | Lead    |    3  |
|      6      |     Priya Verma     |      3  | Developer  |   4   |
|      7      |    Manish Reddy     |      4     | Developer |   4   |
|      8      |    Deepak Sharma   |      5    | Developer      |    4   |
|      9      |   Pooja Gupta   |      4    | Developer    |    4   |
|     10      |   Rajesh Kumar |      5  | DevOps |    4   |
|     11      |  Smita Sharma  |      5    | DevOps|    4   |
|     12      |   Ankit Verma   |      4   | QA |    4   |
|     13      |   Kavita Singh |      5    | QA |    4   |
|     14      |   Sanjay Patel   |      7   | Intern |    5 |
|     15      |   Nisha Sharma  |      7     |Intern |    5   |
|     16      |   Rakesh Kumar  |      7     | Intern |    5  |


## Testing
To run the tests for the application, follow these steps:
- Make sure you have all the necessary dependencies and configurations set up.

- Open the command line or terminal and navigate to the project directory.

- Execute the command to run the tests, such as:
    - `mvn test -Dtest=”TestClassName”`
This command will trigger the execution of the unit tests for the mentioned class, validating different parts of the application.

## Deployment
To deploy my Spring Boot application, I utilized Docker and Kubernetes.Here are the steps to follow:-
-  Create a [DockerFile](https://github.com/shrest-raj/OrganizationChartApplication/blob/master/Dockerfile) to build a Docker image of the application.
    - `docker build -t <image-name> .`
-  Verify the successful creation of the Docker image:
    - `docker images`
-  Start Minikube cluster(install minikube using `brew install minikube`):
    - `minikube start`
-  Set the Docker environment to use the Minikube Docker daemon:
    -  `eval $(minikube docker-env)`
-  Create a Persistent Volume Claim (PVC) for data persistence of the database, Deployment for the database using the PVC and Service for the database:[db-deployment.yaml](https://github.com/shrest-raj/OrganizationChartApplication/blob/master/db-deployment.yaml)
    -   `kubectl apply -f db-deployment.yaml`
-  Create a Deployment for the application and specify the image also Expose the ports of the application Deployment using a Service:[app-deployment.yaml](https://github.com/shrest-raj/OrganizationChartApplication/blob/master/app-deployment.yaml)
    -   `kubectl apply -f app-deployment.yaml`
-  Verify the successful deployment of the application and database:
    -   `kubectl get all`
-  Access the application by hitting the assigned URL.
    -   http://localhost:30001/api/employees using docker desktop context
    -   http://192.168.49.2:30001/api/employees using minikube context (minikube ip command will give yout the ip and the port number can be identified from the service)
## Project Documentation

- For JavaDocs documentation navigate to [documentation](https://github.com/shrest-raj/OrganizationChartApplication/tree/master/documentation).It provides documentation for each and every class, their constructors, methods, etc.
- For Swagger API documentation navigate to [swagger-ui](http://localhost:9091/swagger-ui/index.html#/).It provides explicit details and the payload format for all the API endpoints.

## Acknowledgements

 - [Spring Boot Tutorials By Chad Darby](https://www.udemy.com/user/chaddarby2/)
 - [Awesome README](https://readme.so/)
