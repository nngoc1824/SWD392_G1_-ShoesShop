<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký tài khoản</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 500px;
            margin: 60px auto;
            background: white;
            padding: 30px 40px;
            border-radius: 8px;
            box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="password"],
        input[type="email"] {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .form-actions {
            margin-top: 20px;
            text-align: center;
        }

        input[type="submit"] {
            background-color: #4285f4;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        input[type="submit"]:hover {
            background-color: #3367d6;
        }

        .error {
            color: red;
            margin-top: 15px;
            text-align: center;
        }

        .links {
            margin-top: 20px;
            text-align: center;
        }

        .links a {
            color: #4285f4;
            text-decoration: none;
        }

        .links a:hover {
            text-decoration: underline;
        }
    </style>

    <script>
        function validateForm() {
            var pw = document.getElementById("password").value;
            var confirmPw = document.getElementById("confirmPassword").value;
            if (pw !== confirmPw) {
                alert("Mật khẩu xác nhận không khớp.");
                return false;
            }
            return true;
        }

        function togglePassword(id) {
            const field = document.getElementById(id);
            if (field.type === "password") {
                field.type = "text";
            } else {
                field.type = "password";
            }
        }
    </script>
</head>
<body>

<div class="container">
    <h2>Đăng ký tài khoản</h2>

    <form action="user" method="post" onsubmit="return validateForm()">
        <input type="hidden" name="action" value="register" />

        <label for="userName">Tên đăng nhập</label>
        <input type="text" name="userName" id="userName" required />

        <label for="email">Email</label>
        <input type="email" name="email" id="email" required />

        <label for="password">Mật khẩu</label>
        <input type="password" name="password" id="password" required />
        <input type="checkbox" onclick="togglePassword('password')"> Hiển thị mật khẩu

        <label for="confirmPassword">Xác nhận mật khẩu</label>
        <input type="password" name="confirmPassword" id="confirmPassword" required />
        <input type="checkbox" onclick="togglePassword('confirmPassword')"> Hiển thị mật khẩu

        <label for="fullName">Họ và tên</label>
        <input type="text" name="fullName" id="fullName" required />

        <label for="phone">Số điện thoại</label>
        <input type="text" name="phone" id="phone" required />

        <div class="form-actions">
            <input type="submit" value="Đăng ký" />
        </div>
    </form>

    <%-- Hiển thị lỗi nếu có --%>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <div class="error"><%= error %></div>
    <% } %>

    <div class="links">
        <p><a href="login.jsp">Đã có tài khoản? Đăng nhập</a></p>
        <p><a href="index.jsp">Về trang chủ</a></p>
    </div>
</div>

</body>
</html>
