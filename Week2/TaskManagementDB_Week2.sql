CREATE DATABASE TaskManagementDB_Week2
GO 

USE TaskManagementDB_Week2;
GO

IF OBJECT_ID('project_member', 'U') IS NOT NULL DROP TABLE project_member; 
IF OBJECT_ID('user_roles', 'U') IS NOT NULL DROP TABLE user_roles;
IF OBJECT_ID('task', 'U') IS NOT NULL DROP TABLE task;
IF OBJECT_ID('project', 'U') IS NOT NULL DROP TABLE project;
IF OBJECT_ID('roles', 'U') IS NOT NULL DROP TABLE roles;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
GO

CREATE TABLE roles (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
CREATE INDEX idx_users_username ON users(username);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_UserRoles_User FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT FK_UserRoles_Role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE project (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    start_date DATE DEFAULT GETDATE(),
    status VARCHAR(20) CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED'))
);

CREATE TABLE task (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(500),
    status VARCHAR(20) DEFAULT 'TODO' CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    deadline DATE NOT NULL,
    project_id BIGINT NOT NULL,
    user_id INT,
    CONSTRAINT FK_Task_Project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT FK_Task_User FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
CREATE INDEX idx_task_project ON task(project_id);
CREATE INDEX idx_task_user ON task(user_id);
CREATE INDEX idx_task_status ON task(status);

CREATE TABLE project_member (
    project_id BIGINT NOT NULL,
    user_id INT NOT NULL,
    joined_at DATE DEFAULT GETDATE(),
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT FK_Member_Project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT FK_Member_User FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
GO

SET IDENTITY_INSERT roles ON;

INSERT INTO roles (id, name) VALUES (1, 'MANAGER'), (2, 'USER');

SET IDENTITY_INSERT roles OFF;

SET IDENTITY_INSERT users ON;
INSERT INTO users (id, username, email, password) VALUES 
(1, 'manager_dat', 'dat@cty.com', '123456'),
(2, 'dev_hung', 'hung@cty.com', '123456'),
(3, 'dev_tuan', 'tuan@cty.com', '123456'),
(4, 'qa_lan', 'lan@cty.com', '123456'),
(5, 'ba_dung', 'dung@cty.com', '123456'),
(6, 'sep_tong', 'sep@gmail.com', '123'),
(7, 'nhanvien_quene', 'nv@gmail.com', '1234'),
(8, 'sep_xin_v2', 'sep2@gmail.com', '12345'),
(9, 'user2', 'user2@gmail.com', '123333'),
(11, 'userrrrr', 'userrrrrr@gmail.com', '1234567');
SET IDENTITY_INSERT users OFF;

INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1), (2, 2), (3, 2), (4, 2), (5, 2), (6, 1), (7, 2), (8, 1), (9, 2), (11, 2);

SET IDENTITY_INSERT project ON;
INSERT INTO project (id, name, description, start_date, status) VALUES 
(1, N'Dự án E-commerce', N'Web bán hàng Spring Boot', '2026-02-18', 'ACTIVE'),
(2, N'App Mobile Banking', N'Flutter & Firebase', '2026-02-18', 'ACTIVE'),
(3, N'Hệ thống HRM', N'Quản lý nhân sự', '2026-02-18', 'COMPLETED'),
(4, N'Dự Án Tuần 7', N'Test full quy trình', '2026-02-18', 'ACTIVE');
SET IDENTITY_INSERT project OFF;

SET IDENTITY_INSERT task ON;
INSERT INTO task (id, title, status, deadline, project_id, user_id, description) VALUES
(1, N'Thiết kế DB', 'DONE', '2026-02-01', 1, 1, NULL),
(2, N'Dựng khung Project', 'DONE', '2026-02-02', 1, 2, NULL),
(3, N'Code API Login', 'IN_PROGRESS', '2026-02-10', 1, 2, NULL),
(4, N'Code API Register', 'TODO', '2026-02-12', 1, 3, NULL),
(5, N'Thiết kế giao diện Admin', 'IN_PROGRESS', '2026-02-15', 1, 4, NULL),
(6, N'Tích hợp VNPay', 'TODO', '2026-02-20', 1, 2, NULL),
(7, N'Viết Unit Test', 'TODO', '2026-02-25', 1, 3, NULL),
(8, N'Deploy lên AWS', 'TODO', '2026-03-01', 1, 1, NULL),
(9, N'Họp kickoff', 'DONE', '2026-01-10', 2, 1, NULL),
(10, N'Vẽ màn hình Login', 'DONE', '2026-01-12', 2, 5, NULL),
(11, N'Code API chấm công', 'IN_PROGRESS', '2026-01-20', 2, 3, NULL),
(12, N'Fix bug GPS', 'TODO', '2026-01-25', 2, 5, NULL),
(13, N'Export báo cáo', 'TODO', '2026-01-30', 2, 3, NULL),
(14, N'Tối ưu performance', 'TODO', '2026-02-05', 2, 2, NULL),
(15, N'Review code tuần 1', 'DONE', '2026-02-01', 3, 1, NULL),
(16, N'Viết tài liệu HDSD', 'DONE', '2026-02-02', 3, 4, NULL),
(17, N'Backup database', 'IN_PROGRESS', '2026-02-03', 3, 1, NULL),
(18, N'Fix lỗi font chữ', 'TODO', '2026-02-04', 1, 4, NULL),
(19, N'Thêm chức năng quên MK', 'TODO', '2026-02-05', 1, 2, NULL),
(20, N'Nâng cấp version Spring', 'TODO', '2026-02-06', 1, 3, NULL),
(21, N'Design logo mới', 'TODO', '2026-02-07', 2, 5, NULL),
(22, N'Mua server mới', 'DONE', '2026-01-01', 3, 1, NULL),
(23, N'Cài đặt môi trường Dev', 'DONE', '2026-01-02', 1, 2, NULL),
(24, N'Training nhân viên mới', 'IN_PROGRESS', '2026-02-10', 3, 1, NULL),
(25, N'Kiểm kê tài sản', 'TODO', '2026-02-11', 3, 4, NULL),
(26, N'Tổ chức team building', 'TODO', '2026-02-20', 3, 1, NULL),
(27, N'Thanh toán tiền điện', 'DONE', '2026-01-15', 3, 1, NULL),
(28, N'Gặp khách hàng A', 'DONE', '2026-01-16', 2, 1, NULL),
(29, N'Demo sản phẩm lần 1', 'TODO', '2026-02-28', 1, 1, NULL),
(30, N'Nộp báo cáo tuần', 'TODO', '2026-01-25', 1, 2, NULL),
(31, N'Viết báo cáo tuần', 'TODO', '2026-02-20', 1, 2, N'Làm nhanh còn về');
SET IDENTITY_INSERT task OFF;

INSERT INTO project_member (project_id, user_id, joined_at) VALUES 
(1, 2, '2026-02-18'), (1, 3, '2026-02-18');
GO

SELECT u.username, r.name as role_name
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id;

SELECT * FROM task WHERE user_id = 2;

SELECT * FROM users