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
    <style>
        .google-login {
            text-align: center;
            margin-top: 20px;
        }

        .google-login img {
            width: 230px;
            cursor: pointer;
        }
    </style>
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

    <!-- ✅ Đăng nhập bằng Google -->

    <div class="google-login" style="text-align: center; margin-top: 20px;">
        <a href="https://accounts.google.com/o/oauth2/v2/auth?scope=openid%20email%20profile&redirect_uri=http://localhost:8080/SWD392_ShoesShop_war_exploded/login-google&response_type=code&client_id=841040454506-pttbflftv7fu6qsdqpn7ju434hpag7be.apps.googleusercontent.com&access_type=offline&prompt=consent"
           class="btn btn-lg btn-danger"
           style="text-decoration: none; padding: 10px 20px;">
            <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google"
                 style="width: 20px; vertical-align: middle; margin-right: 8px;" />
            Đăng nhập bằng Google
        </a>
    </div>


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
            <a href="register.jsp">Đăng ký người dùng</a>
        </div>
        <div style="margin-top: 10px;">
            <a href="index.jsp">Về trang chủ</a>
        </div>
    </div>
</div>

</body>
</html>
