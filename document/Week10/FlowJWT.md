# Luồng Xác Thực JWT (JWT Authentication Flow)

1. **Client gửi yêu cầu đăng nhập**: Người dùng gọi API POST /auth/login kèm theo username và password.

2. **Server xác thực & Cấp Token**: Controller gọi xuống Repository để lấy User, dùng PasswordEncoder so sánh mật khẩu. Nếu khớp, JwtUtil sẽ tạo ra một chuỗi JWT Token có chứa username và trả về cho Client thông qua ApiResponse.

3. **Client gắn Token vào Request**: Trong các lần gọi API tiếp theo (vd: tạo Project, xem Task), Client đính kèm Token này vào HTTP Header với định dạng: `Authorization: Bearer <token_string>`.

4. **Filter đánh chặn (JwtAuthenticationFilter)**: Request trước khi chạm tới Controller sẽ đi qua JwtAuthenticationFilter (đã cấu hình trong SecurityConfig).

5. **Trích xuất thông tin**: Filter tách chữ "Bearer " để lấy Token chuẩn, sau đó dùng `jwtUtil.extractUsername(token)` để giải mã và lấy ra username.

6. **Kiểm tra Database**: Filter gọi `UserService.loadUserByUsername()` để load đầy đủ thông tin User (bao gồm cả Role: USER/MANAGER).

7. **Xác thực hợp lệ**: Hàm `jwtUtil.validateToken()` kiểm tra hạn sử dụng và chữ ký của Token.

8. **Cấp quyền Context**: Nếu mọi thứ hợp lệ, hệ thống tạo một đối tượng `UsernamePasswordAuthenticationToken` và đưa vào `SecurityContextHolder`.

9. **Xử lý nghiệp vụ**: Request được cho phép đi tiếp vào Controller. Các annotation như `@PreAuthorize` sẽ kiểm tra Role trong Context xem có được phép thực thi API hay không.

10. **Từ chối truy cập**: Nếu Token hết hạn, sai chữ ký, hoặc không có Token, Filter sẽ chặn lại và ném ra lỗi 401 Unauthorized hoặc 403 Forbidden.
