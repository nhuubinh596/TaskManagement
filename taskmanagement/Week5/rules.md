# QUY TẮC NGHIỆP VỤ (BUSINESS RULES) - TUẦN 5

## 1. Quy tắc về Trạng thái (Task Status Flow)
* Mặc định khi tạo mới: Task luôn có trạng thái là **TODO**.
* Quy trình chuẩn: TODO -> IN_PROGRESS -> DONE.
* **Quy tắc chặn:** Nếu Task đã ở trạng thái **DONE** (Hoàn thành), hệ thống KHÔNG cho phép chỉnh sửa bất kỳ thông tin nào nữa (Frozen).

## 2. Quy tắc về Dự án (Project)
* Một Task bắt buộc phải thuộc về một Project tồn tại.
* Không được phép tạo Task "mồ côi" (không có project_id).

## 3. Quy tắc về Gán việc (Assignment)
* Chỉ được gán Task cho User đang tồn tại trong hệ thống.
* (Nâng cao - Tuần sau): User đó phải là thành viên của Project thì mới được nhận việc.

## 4. Quy tắc về Thời gian
* Deadline của Task phải là một ngày trong tương lai (lớn hơn ngày hiện tại).