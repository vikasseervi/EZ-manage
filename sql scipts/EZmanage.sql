CREATE DATABASE IF NOT EXISTS `EZ_Manage`;
USE `EZ_Manage`;

DROP TABLE IF EXISTS employee_role;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS role;

-- Employee Table (auth fields merged into employee)
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(68),
    active BOOLEAN DEFAULT TRUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Roles Table
CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Employee Roles Mapping Table
CREATE TABLE employee_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
    UNIQUE KEY (employee_id, role_id)
);

-- Seed employees (username + auth fields merged). Order preserves IDs for role mapping.
-- Password "vikas" h
INSERT INTO employee (username, password_hash, active, first_name, last_name, email) VALUES
    ('abdul', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1, 'Abdul', 'Ahad', 'abdul@ahad'),
    ('yash', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1, 'Yash', 'Prakash', 'yash@prakash'),
    ('pratibha', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1, 'Pratibha', 'Sharma', 'pratibha@sharma'),
    ('avani', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1, 'Avani', 'Gupta', 'avani@gupta'),
    ('anusha', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1, 'Anusha', 'Reddy', 'anusha@reddy'),
    ('vikas', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1, 'Vikas', 'Seervi', 'vikas@seervi');

INSERT INTO role (role_name) VALUES
    ('ROLE_EMPLOYEE'),
    ('ROLE_MANAGER'),
    ('ROLE_ADMIN');

INSERT INTO employee_role (employee_id, role_id) VALUES
    (1, 1), -- Abdul: ROLE_EMPLOYEE
    (2, 1), -- Yash: ROLE_EMPLOYEE
    (3, 1), -- Pratibha: ROLE_EMPLOYEE
    (4, 2), -- Avani: ROLE_MANAGER
    (4, 1), -- Avani: ROLE_EMPLOYEE
    (5, 2), -- Anusha: ROLE_MANAGER
    (5, 1), -- Anusha: ROLE_EMPLOYEE
    (6, 1), -- Vikas: ROLE_EMPLOYEE
    (6, 3), -- Vikas: ROLE_ADMIN
    (6, 2); -- Vikas: ROLE_MANAGER