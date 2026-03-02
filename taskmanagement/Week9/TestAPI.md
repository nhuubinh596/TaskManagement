# 🚀 TÀI LIỆU TEST API

Truy cập: http://localhost:8080/swagger-ui/index.html

Mở khóa (Authorize): Dùng Token lấy được từ API Đăng nhập, bấm vào nút Authorize ở góc trên trang Swagger, dán Token vào để mở khóa toàn bộ API nghiệp vụ.

## 🔑 PHẦN 1: HƯỚNG DẪN MỞ KHÓA BẢO MẬT (AUTHORIZE)
1. Mở API `POST /auth/login`.
2. Nhập tài khoản/mật khẩu hợp lệ và bấm **Execute**.
3. Copy chuỗi `Token` dài ngoằng trong phần `"result"` trả về.
4. Cuộn lên đầu trang Swagger, bấm nút **Authorize** (màu xanh lá cây).
5. Dán chuỗi Token vừa copy vào ô **Value** và bấm **Authorize** -> **Close**.

## 📦 PHẦN 2: TỔNG HỢP JSON BODY CÁC API

### 1. Nhóm Authentication (Xác thực)
**📌 POST `/auth/register` (Tạo tài khoản Sếp)**
```json
{
  "username": "sep_tong",
  "password": "123",
  "email": "sep@gmail.com",
  "roles": ["MANAGER"]
}

📌 POST /auth/register (Tạo tài khoản Nhân viên)

JSON
{
  "username": "nhan_vien_1",
  "password": "123",
  "email": "nv1@gmail.com",
  "roles": ["USER"]
}

📌 POST /auth/login (Đăng nhập lấy Token)

JSON
{
  "username": "sep_tong",
  "password": "123"
}

2. Nhóm Project (Dự án)
📌 POST /api/projects (Tạo Dự án mới)
(Yêu cầu: Đã Authorize bằng Token của MANAGER)

JSON
{
  "name": "Dự án Website JobLevel",
  "description": "Nền tảng tuyển dụng tích hợp AI"
}

3. Nhóm Task (Công việc)
📌 POST /api/tasks/add (Tạo công việc mới)
(Yêu cầu: Truyền đúng projectId đã tồn tại trong Database)

JSON
{
  "title": "Thiết kế API Login/Register",
  "description": "Viết API bảo mật JWT cho hệ thống",
  "status": "TODO",
  "projectId": 1
}

4. Nhóm User (Quản trị viên tạo tài khoản)
📌 POST /api/users (Tạo user trực tiếp bằng Entity)

JSON
{
  "username": "nhanvien01",
  "password": "123",
  "email": "nv01@company.com",
  "roles": [
    {
      "id": 2,
      "name": "USER"
    }
  ]
}
🎬 PHẦN 3: KỊCH BẢN VẤN ĐÁP MENTOR (LUỒNG NGHIỆP VỤ THỰC TẾ)
🔹 Bước 1: Chuẩn bị Data (Chưa cần Authorize)

- Dùng POST /auth/register tạo acc Sếp (MANAGER).

- Dùng POST /auth/register tạo acc Nhân viên (USER).

🔹 Bước 2: Đóng vai SẾP (Giao việc)

- Đăng nhập (POST /auth/login) bằng acc Sếp -> Copy Token -> Lên đầu trang bấm Authorize và dán Token.

- Sếp tạo Dự án mới (POST /api/projects). Ghi nhớ ID dự án (VD: 1).

- Sếp tạo Task cho dự án (POST /api/tasks/add). Nhập projectId: 1. Ghi nhớ ID task (VD: 1).

- Sếp giao việc (PUT /api/tasks/assign): 
    => Điền taskId = 1, userId = 2 (ID của nhân viên) -> Thực thi báo 200 OK.

🔹 Bước 3: Đóng vai NHÂN VIÊN (Làm việc & Demo bị chặn quyền)

- Lên mục Authorize trên cùng -> Logout để xóa Token của Sếp.

- Đăng nhập (POST /auth/login) bằng acc Nhân viên -> Copy Token mới -> Authorize lại.

- Biểu diễn chặn quyền (Ăn điểm nhấn): 
    => Cố tình gọi API tạo Dự án (POST /api/projects) -> Bị hệ thống chặn, văng lỗi 403 Forbidden.

- Nhân viên xem việc của mình: 
    => Gọi API GET /api/tasks/user/{id} với ID bản thân (2) -> Thấy task Sếp giao.

- Nhân viên báo cáo hoàn thành: 
    => Gọi API PATCH /api/tasks/{id}/status -> Nhập id = 1, status = DONE -> Báo 200 OK.

📌 PHẦN 4: DANH SÁCH CÁC API TEST TRỰC TIẾP (KHÔNG CẦN JSON BODY)
Trên Swagger, bạn chỉ cần điền giá trị vào các ô nhập liệu (Fields) có sẵn và bấm Execute đối với các API sau:

- GET /api/projects: Lấy danh sách toàn bộ dự án.

- GET /api/users: Lấy danh sách nhân viên (Cần quyền MANAGER).

- GET /api/tasks: Lấy danh sách task của bản thân người đang đăng nhập.

- GET /api/tasks/project/{id}: Nhập id dự án để xem các task thuộc dự án đó.

- GET /api/tasks/user/{id}: Nhập id user để xem các task của nhân viên đó.

- PUT /api/tasks/assign: Nhập taskId và userId để tiến hành giao việc.

- PATCH /api/tasks/{id}/status: Nhập id task và chuỗi status (TODO, IN_PROGRESS, DONE) để cập nhật tiến độ.

- DELETE /api/tasks/{id}: Nhập id task để xóa (Cần quyền MANAGER).
