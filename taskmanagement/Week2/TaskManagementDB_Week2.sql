CREATE DATABASE TaskManagementDB_Week2
GO 

USE TaskManagementDB_Week2;
GO

IF OBJECT_ID('project_member', 'U') IS NOT NULL DROP TABLE project_member; 
IF OBJECT_ID('task', 'U') IS NOT NULL DROP TABLE task;
IF OBJECT_ID('project', 'U') IS NOT NULL DROP TABLE project;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
GO

CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('MANAGER', 'USER'))
);
CREATE INDEX idx_users_username ON users(username);
GO

CREATE TABLE project (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    start_date DATE DEFAULT GETDATE(),
    status VARCHAR(20) CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED'))
);
GO

CREATE TABLE task (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(500),
    status VARCHAR(20) DEFAULT 'TODO' CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    deadline DATE NOT NULL,
    project_id INT NOT NULL,
    user_id INT,
    CONSTRAINT FK_Task_Project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT FK_Task_User FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
CREATE INDEX idx_task_project ON task(project_id);
CREATE INDEX idx_task_user ON task(user_id);
CREATE INDEX idx_task_status ON task(status);
GO

CREATE TABLE project_member (
    project_id INT,
    user_id INT,
    joined_at DATE DEFAULT GETDATE(), -- (Optional) Thêm ngày tham gia cho xịn
    PRIMARY KEY (project_id, user_id), -- Khóa chính kép: 1 người ko thể vào 1 dự án 2 lần
    CONSTRAINT FK_Member_Project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    CONSTRAINT FK_Member_User FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
GO

INSERT INTO users (username, email, password, role) VALUES 
('manager_dat', 'dat@cty.com', '123456', 'MANAGER'),
('dev_hung', 'hung@cty.com', '123456', 'USER'),
('dev_tuan', 'tuan@cty.com', '123456', 'USER'),
('qa_lan', 'lan@cty.com', '123456', 'USER'),
('ba_dung', 'dung@cty.com', '123456', 'USER');

INSERT INTO project (name, description, status) VALUES 
(N'Dự án E-commerce', N'Web bán hàng Spring Boot', 'ACTIVE'),
(N'App Mobile Banking', N'Flutter & Firebase', 'ACTIVE'),
(N'Hệ thống HRM', N'Quản lý nhân sự', 'COMPLETED');

INSERT INTO task (title, status, deadline, project_id, user_id) VALUES
(N'Thiết kế DB', 'DONE', '2026-02-01', 1, 1),
(N'Dựng khung Project', 'DONE', '2026-02-02', 1, 2),
(N'Code API Login', 'IN_PROGRESS', '2026-02-10', 1, 2),
(N'Code API Register', 'TODO', '2026-02-12', 1, 3),
(N'Thiết kế giao diện Admin', 'IN_PROGRESS', '2026-02-15', 1, 4),
(N'Tích hợp VNPay', 'TODO', '2026-02-20', 1, 2),
(N'Viết Unit Test', 'TODO', '2026-02-25', 1, 3),
(N'Deploy lên AWS', 'TODO', '2026-03-01', 1, 1),
(N'Họp kickoff', 'DONE', '2026-01-10', 2, 1),
(N'Vẽ màn hình Login', 'DONE', '2026-01-12', 2, 5),
(N'Code API chấm công', 'IN_PROGRESS', '2026-01-20', 2, 3),
(N'Fix bug GPS', 'TODO', '2026-01-25', 2, 5),
(N'Export báo cáo', 'TODO', '2026-01-30', 2, 3),
(N'Tối ưu performance', 'TODO', '2026-02-05', 2, 2),
(N'Review code tuần 1', 'DONE', '2026-02-01', 3, 1),
(N'Viết tài liệu HDSD', 'DONE', '2026-02-02', 3, 4),
(N'Backup database', 'IN_PROGRESS', '2026-02-03', 3, 1),
(N'Fix lỗi font chữ', 'TODO', '2026-02-04', 1, 4),
(N'Thêm chức năng quên MK', 'TODO', '2026-02-05', 1, 2),
(N'Nâng cấp version Spring', 'TODO', '2026-02-06', 1, 3),
(N'Design logo mới', 'TODO', '2026-02-07', 2, 5),
(N'Mua server mới', 'DONE', '2026-01-01', 3, 1),
(N'Cài đặt môi trường Dev', 'DONE', '2026-01-02', 1, 2),
(N'Training nhân viên mới', 'IN_PROGRESS', '2026-02-10', 3, 1),
(N'Kiểm kê tài sản', 'TODO', '2026-02-11', 3, 4),
(N'Tổ chức team building', 'TODO', '2026-02-20', 3, 1),
(N'Thanh toán tiền điện', 'DONE', '2026-01-15', 3, 1),
(N'Gặp khách hàng A', 'DONE', '2026-01-16', 2, 1),
(N'Demo sản phẩm lần 1', 'TODO', '2026-02-28', 1, 1),
(N'Nộp báo cáo tuần', 'TODO', '2026-01-25', 1, 2);
GO

SELECT t.title, t.status, t.deadline, p.name 
FROM task t
JOIN users u ON t.user_id = u.id
JOIN project p ON t.project_id = p.id
WHERE u.username = 'dev_hung';

SELECT t.title, t.status, u.username
FROM task t
JOIN project p ON t.project_id = p.id
LEFT JOIN users u ON t.user_id = u.id
WHERE p.name = N'Dự án E-commerce';

SELECT status, COUNT(*)
FROM task
GROUP BY status;

INSERT INTO project_member (project_id, user_id) VALUES (1, 2);
INSERT INTO project_member (project_id, user_id) VALUES (1, 3);