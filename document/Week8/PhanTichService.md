# Kịch bản Test cho TaskService

## 1. Hàm createTask
- Case 1: Tạo task thành công khi đầu vào hợp lệ (Happy path).
- Case 2: Tạo task thất bại, ném ra lỗi do Project ID không tồn tại.

## 2. Hàm assignTask
- Case 1: Giao task thành công cho User.
- Case 2: Báo lỗi khi giao task cho User KHÔNG thuộc Project đó (Test Rule).
- Case 3: Báo lỗi khi Task ID hoặc User ID không tồn tại.