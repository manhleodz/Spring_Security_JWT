###Spring Boot 3.0 Security with JWT Implementation

![image](https://github.com/manhleodz/Spring_Security_JWT/assets/107250543/7bfe8b86-82ab-432c-89fc-2460f2382694)

##Miêu tả:
- Đây là 1 bản triển khai nho nhỏ khi tôi bắt đầu học và làm 1 dự án cá nhân về banking system bằng java spring boot
- Có sử dụng refresh token và access token để định danh và phân quyền
- Người dùng chỉ có thể đăng nhập và sử dụng trên 1 thiết bị duy nhất để đảm bảo an toàn vì là hệ thống ngân hàng. Khi tạo tài khoản thành công, người dùng đăng nhập lại và sẽ tạo ra refresh và access token để lưu trong db và gửi về phía người dùng với thời gian hết hạn là 20 phút.
- Sau khi token hết hạn, người dùng cần sử dụng API refreshToken có gửi lên refreshToken để xác thực và tạo token mới. Nếu refreshToken và accessToken trùng với dữ liệu trong db thì sẽ tạo refresh và access token mới người dùng tiếp tục sử dụng ko thì sẽ trả về status 403
