CREATE SCHEMA  IF NOT EXISTS `organization_table`;
USE `organization_table`;

DROP TABLE IF EXISTS level;

CREATE TABLE level (
  level_id INT PRIMARY KEY,
  designation VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
  employee_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  job_title VARCHAR(100) NOT NULL,
  manager_id INT,
  level_id INT,
  FOREIGN KEY (manager_id) REFERENCES employee(employee_id),
  FOREIGN KEY (level_id) REFERENCES level(level_id)
);

-- Inserting records into the level table
INSERT INTO level (level_id, designation) VALUES
(1, 'Director'),
(2, 'Manager'),
(3, 'Lead'),
(4, 'Developer, DevOps, QA'),
(5, 'Intern');

-- Inserting records into the employee table
INSERT INTO employee (name, job_title, manager_id, level_id) VALUES
('Amitabh Singh','Director',NULL,1),                                 --1
('Rahul Sharma', 'Manager', 1, 2),                                   --2
('Aarti Patel', 'Manager', 1, 2),                                    --3
('Neha Gupta', 'Lead', 2, 3),                                        --4
('Amit Singh', 'Lead', 2, 3),                                        --5
('Priya Verma', 'Developer', 3, 4),                                  --6
('Manish Reddy', 'Developer', 4, 4),                                 --7
('Deepak Sharma', 'Developer', 5, 4),                                --8
('Pooja Gupta', 'Developer', 4, 4),                                  --9
('Rajesh Kumar', 'DevOps', 5, 4),                                    --10
('Smita Sharma', 'DevOps', 5, 4),                                    --11
('Ankit Verma', 'QA', 4, 4),                                         --12
('Kavita Singh', 'QA', 5, 4),                                        --13
('Sanjay Patel', 'Intern', 7, 5),                                    --14
('Nisha Sharma', 'Intern', 7, 5),                                    --15
('Rakesh Kumar', 'Intern', 7, 5);                                    --16





