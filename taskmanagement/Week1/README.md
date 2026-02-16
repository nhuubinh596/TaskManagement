TASK MANAGEMENT SYSTEM

1. Giới thiệu
Hệ thống quản lý công việc, cho phép Quản lý (Manager) giao việc và Nhân viên (User) cập nhật tiến độ.

2. Các thực thể chính (Entities)
User (Người dùng)
- Role: MANAGER (Quản lý), USER (Nhân viên).
- Thông tin: Username, Password, Email.

Project (Dự án)
- Dự án chứa nhiều Task.
- Thông tin: Tên dự án, Mô tả, Ngày bắt đầu, Trạng thái.

Task (Công việc)
- Thuộc về 1 Project.
- Có thể được giao cho 1 User (Assign).
- Trạng thái: TODO -> IN_PROGRESS -> DONE.
- Quy tắc: Deadline phải lớn hơn ngày tạo.

3. Quy trình nghiệp vụ 
- Tạo mới: Manager tạo Project và các Task bên trong.
- Giao việc: Manager gán (Assign) Task cho User cụ thể.
- Thực hiện: User xem danh sách Task của mình và update trạng thái.