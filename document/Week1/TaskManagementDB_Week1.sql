CREATE DATABASE TaskManagementDB
GO

USE TaskManagementDB;
GO

CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);
GO

CREATE TABLE project (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    start_date DATE,
    status VARCHAR(20)
);
GO

CREATE TABLE task (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(500),
    status VARCHAR(20) DEFAULT 'TODO',
    deadline DATE NOT NULL,
    project_id INT NOT NULL,
    user_id INT,
    CONSTRAINT FK_Task_Project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT FK_Task_User FOREIGN KEY (user_id) REFERENCES users(id)
);
GO

INSERT INTO users (username, email, password, role) VALUES 
('manager_dat', 'dat@company.com', '123456', 'MANAGER'),
('user_tuan', 'tuan@company.com', '123456', 'USER'),
('user_lan', 'lan@company.com', '123456', 'USER');

INSERT INTO project (name, description, start_date, status) VALUES 
(N'H? th?ng Qu?n lý Công vi?c', N'D? án th?c t?p Java Spring Boot', '2026-01-14', 'ACTIVE'),
(N'Website Bán Hàng', N'D? án th??ng m?i ?i?n t?', '2026-02-01', 'ACTIVE');

INSERT INTO task (title, description, status, deadline, project_id, user_id) VALUES 
(N'Thi?t k? Database', N'V? ERD và t?o b?ng SQL', 'IN_PROGRESS', '2026-01-20', 1, 2),
(N'Code API Login', N'Dùng JWT Spring Security', 'TODO', '2026-01-22', 1, 3),
(N'Phân tích yêu c?u', N'G?p khách hàng l?y requirement', 'TODO', '2026-02-10', 2, NULL);
GO