CREATE DATABASE IF NOT EXISTS EZ_Manage;
USE EZ_Manage;

DROP TABLE IF EXISTS employee_role;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS auth;
DROP TABLE IF EXISTS role;

-- Authentication Table
CREATE TABLE auth (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(68) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

-- Employee Profile Table
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auth_id BIGINT NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (auth_id) REFERENCES auth (id) ON DELETE CASCADE
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

INSERT INTO auth (username, password_hash, active) VALUES
    ('abdul', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1),
    ('yash', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1),
    ('pratibha', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1),
    ('avani', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1),
    ('anusha', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1),
    ('vikas', '$2a$10$NyshA8kmAzI3ubl3tfG5xeiqJEZfOcTViqIQoQlpQ6AtZNSzdmhU2', 1);

INSERT INTO employee (auth_id, first_name, last_name, email) VALUES
    (1, 'Abdul', 'Ahad', 'abdul@ahad'),
    (2, 'Yash', 'Prakash', 'yash@prakash'),
    (3, 'Pratibha', 'Sharma', 'pratibha@sharma'),
    (4, 'Avani', 'Gupta', 'avani@gupta'),
    (5, 'Anusha', 'Reddy', 'anusha@reddy'),
    (6, 'Vikas', 'Seervi', 'vikas@seervi');

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
    (6, 3), -- Vikas: ROLE_ADMIN
    (6, 2); -- Vikas: ROLE_MANAGER
