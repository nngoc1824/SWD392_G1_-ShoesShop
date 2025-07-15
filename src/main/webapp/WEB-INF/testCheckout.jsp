<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán PayOS</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            padding: 40px;
        }
        form {
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            max-width: 600px;
            margin: auto;
            box-shadow: 0 0 10px #ccc;
        }
        input, select, textarea {
            width: 100%;
            padding: 8px;
            margin: 8px 0;
        }
        label {
            font-weight: bold;
        }
        .btn {
            background: #28a745;
            color: white;
            border: none;
            padding: 10px 15px;
            cursor: pointer;
        }
        h2 {
            text-align: center;
            color: #007bff;
        }
    </style>
</head>
<body>
<form method="post" accept-charset="UTF-8" action="${pageContext.request.contextPath}/checkout/create">
    <h2>Thông tin đơn hàng</h2>

    <label for="firstName">Họ</label>
    <input type="text" id="firstName" name="firstName" value="Nguyen" required>

    <label for="lastName">Tên</label>
    <input type="text" id="lastName" name="lastName" value="Van A" required>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" value="nguyen@example.com" required>

    <label for="phone">Số điện thoại</label>
    <input type="text" id="phone" name="phone" value="0912345678" required>

    <label for="address">Địa chỉ cụ thể</label>
    <input type="text" id="address" name="address" value="123 Đường ABC" required>

    <label for="province">Tỉnh/Thành phố</label>
    <input type="text" id="province" name="province" value="Hà Nội" required>

    <label for="district">Quận/Huyện</label>
    <input type="text" id="district" name="district" value="Nam Từ Liêm" required>

    <label for="ward">Phường/Xã</label>
    <input type="text" id="ward" name="ward" value="Mễ Trì" required>

    <!-- Nếu muốn cho phép khách ghi chú -->
    <!--
    <label for="note">Ghi chú</label>
    <textarea id="note" name="note"></textarea>
    -->

    <br>
    <button type="submit" class="btn">🛍️ Tiến hành thanh toán</button>
</form>
</body>
</html>
