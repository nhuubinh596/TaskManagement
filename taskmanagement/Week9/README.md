# 🚀 JobLevel - Task Management API

Dự án Hệ thống Quản lý Công việc (Task Management) thuộc nền tảng tuyển dụng JobLevel. Backend được xây dựng bằng Spring Boot 3, bảo mật bằng Spring Security (JWT) và cơ sở dữ liệu SQL Server.

---

## 🛠️ 1. Hướng dẫn cài đặt (Setup)

1. **Yêu cầu hệ thống:**
    * Java JDK 17+
    * SQL Server
    * Maven 3.x+

2. **Cài đặt Database:**
    * Chạy script SQL để khởi tạo database `Joblevel_Datn` và các bảng.
    * Mở file `src/main/resources/application-dev.properties` và cấu hình lại `username` / `password` của SQL Server cho khớp với máy của bạn.

3. **Cập nhật thư viện:**
    * Mở Terminal tại thư mục gốc và chạy lệnh: `mvn clean install`

---

## 🏃‍♂️ 2. Hướng dẫn khởi chạy (Run)

### 🔹 Chạy môi trường Dev (Local IDE):
* Mở file `TaskmanagementApplication.java` trên IDE (IntelliJ/Eclipse) và nhấn **Run**.
* Ứng dụng sẽ chạy mặc định ở cổng **8080**.

### 🔹 Chạy môi trường Prod (Bằng file JAR):
* Build dự án ra file JAR bằng lệnh: `mvn clean package`
* Khởi chạy bằng Terminal (chạy ở cổng 8081):
  ```bash
  java -jar target/taskmanagement-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod