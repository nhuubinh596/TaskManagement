# Checklist Demo - Task Management System (Backend API)

## Phần Chuẩn Bị
- [ ] Khởi động server Spring Boot qua IntelliJ hoặc Terminal.
- [ ] Mở giao diện API Document: http://localhost:8080/swagger-ui/index.html#/
- [ ] Đảm bảo Database đã được dọn sạch để test luồng mới từ đầu.

## Phần 1: Registration & Authentication (Xác thực)

### 1.1 Khởi tạo tài khoản MANAGER (Quản lý)
- [ ] Mở nhóm 1. Authentication -> Gọi POST /auth/register
- [ ] Nhập Request Body:
```json
{
  "username": "manager01",
  "email": "manager@iku.vn",
  "password": "123",
  "role": "MANAGER"
}
```
- [ ] Verify: Mã 200 OK, trả về thông tin user. (Lưu lại ID của Manager).

### 1.2 Khởi tạo tài khoản USER (Nhân viên)
- [ ] Gọi tiếp POST /auth/register
- [ ] Nhập Request Body (role mặc định hoặc điền "USER"):
```json
{
  "username": "dev01",
  "email": "dev@iku.vn",
  "password": "123",
  "role": "USER"
}
```
- [ ] Verify: Mã 200 OK. (Lưu lại ID của User).

### 1.3 Login & Lấy JWT Token
- [ ] Gọi POST /auth/login với tài khoản MANAGER.
- [ ] Verify: Mã 200 OK, hệ thống trả về chuỗi Token trong trường result.
- [ ] Copy chuỗi Token, kéo lên đầu trang Swagger bấm vào nút Authorize (Ổ khóa) và dán Token vào để mở khóa các API.

## Phần 2: Luồng nghiệp vụ của MANAGER (Tạo Project & Assign Task)

### 2.1 Tạo dự án mới
- [ ] Mở nhóm 2. Project Management -> Gọi POST /api/projects
- [ ] Nhập Request Body (truyền userId của tài khoản nhân viên vào để add họ làm member luôn):
```json
{
  "name": "Dự án Tốt nghiệp 2026",
  "description": "Làm Backend Spring Boot",
  "startDate": "2026-03-25",
  "userId": 2 
}
```
- [ ] Verify: Mã 200 OK. (Lưu lại projectId).

### 2.2 Tạo công việc (Task)
- [ ] Mở nhóm 3. Task Management -> Gọi POST /api/tasks/add
- [ ] Nhập Request Body:
```json
{
  "title": "Thiết kế Database",
  "description": "Vẽ ERD và tạo script SQL",
  "deadline": "2026-04-30T10:00:00Z",
  "projectId": 1
}
```
- [ ] Verify: Mã 200 OK, task được tạo với trạng thái mặc định là TODO. (Lưu lại taskId).

### 2.3 Giao việc (Assign Task) cho USER
- [ ] Gọi PUT /api/tasks/assign
- [ ] Nhập Parameters: taskId = 1, userId = 2.
- [ ] Verify: Mã 200 OK. (Nhấn mạnh: Nếu User không thuộc Project, hệ thống sẽ ném lỗi ngay).

## Phần 3: Luồng nghiệp vụ của USER (Xử lý công việc)

### 3.1 Login tài khoản USER
- [ ] Gọi lại POST /auth/login với tài khoản dev01 để lấy Token mới.
- [ ] Bấm Logout ổ khóa cũ trên Swagger và Authorize bằng Token của USER.

### 3.2 Kiểm tra danh sách việc được giao
- [ ] Gọi GET /api/tasks (Lấy danh sách task của người đang đăng nhập).
- [ ] Verify: Mã 200 OK, chỉ trả về đúng task "Thiết kế Database" vừa được assign.

### 3.3 Cập nhật tiến độ Task (Update Status)
- [ ] Gọi PATCH /api/tasks/{id}/status
- [ ] Nhập Parameters: id = 1, status = IN_PROGRESS.
- [ ] Verify: Mã 200 OK, trạng thái đã đổi.
- [ ] Gọi tiếp API trên đổi status thành DONE.
- [ ] Verify: Mã 200 OK. (Nhấn mạnh: Nếu Task đã DONE, gọi chuyển lại TODO sẽ bị văng Exception).

## Phần 4: Test Validation & Authorization Handling

### 4.1 Bắt lỗi Validation (Bean Validation)
- [ ] Gọi POST /api/tasks/add nhưng cố tình truyền deadline là một ngày trong quá khứ.
- [ ] Verify: Trả về mã lỗi 400 Bad Request (báo lỗi "Deadline phải là ngày trong tương lai").

### 4.2 Bắt lỗi Phân quyền (Forbidden 403)
- [ ] Đang dùng Token của USER, thử gọi API xóa Task DELETE /api/tasks/{id}.
- [ ] Verify: Bị chặn lại với mã 403 Forbidden (Vì hàm này gắn @PreAuthorize("hasAuthority('MANAGER')")).
