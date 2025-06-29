<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="user_action/login.css">
    <script>
        function togglePassword() {
            const passwordField = document.getElementById("password");
            if (passwordField.type === "password") {
                passwordField.type = "text";
            } else {
                passwordField.type = "password";
            }
        }
    </script>
</head>
<body>

<div class="overlay"></div>

<div class="login-container">
    <h2>Đăng nhập</h2>

    <!-- Gửi tới controller /user?action=login -->
    <form action="user" method="post">
        <input type="hidden" name="action" value="login" />

        <div class="form-group">
            <label for="userName">Tên đăng nhập</label>
            <input type="text" name="userName" id="userName" required />
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <input type="password" name="password" id="password" required />
            <div>
                <input type="checkbox" onclick="togglePassword()" /> Hiển thị mật khẩu
            </div>
        </div>

        <div class="form-actions">
            <input type="submit" value="Đăng nhập" />
        </div>
    </form>

    <!-- Hiển thị lỗi -->
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <div class="error" style="color: red; margin-top: 10px;">
        <%= error %>
    </div>
    <%
        }
    %>

    <div class="links">
        <div>
            <a href="forgot_password.jsp">Quên mật khẩu?</a> |
            <a href="register.jsp">Đăng ký người dùng</a>
        </div>
        <div style="margin-top: 10px;">
            <a href="index.jsp">Về trang chủ</a>
        </div>
    </div>
</div>

</body>
</html>
