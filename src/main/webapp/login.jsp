<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css">

    <!-- Optional custom CSS -->
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

<div class="container">
    <div class="login-container">
        <h2 class="text-center mb-4">Đăng nhập</h2>

        <!-- Gửi tới controller /user?action=login -->
        <form action="user" method="post">
            <input type="hidden" name="action" value="login" />

            <div class="form-group">
                <label for="userName">Tên đăng nhập</label>
                <input type="text" name="userName" id="userName" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" name="password" id="password" class="form-control" required />
                <div class="form-check mt-2">
                    <input type="checkbox" class="form-check-input" onclick="togglePassword()" id="showPass" />
                    <label class="form-check-label" for="showPass">Hiển thị mật khẩu</label>
                </div>
            </div>

            <div class="form-actions mt-4">
                <input type="submit" value="Đăng nhập" class="btn btn-primary" />
            </div>
        </form>

        <!-- ✅ Đăng nhập bằng Google -->
        <div class="google-login mt-4">
            <a href="https://accounts.google.com/o/oauth2/v2/auth?scope=openid%20email%20profile&redirect_uri=http://localhost:8080/SWD392_ShoesShop_war_exploded/login-google&response_type=code&client_id=841040454506-pttbflftv7fu6qsdqpn7ju434hpag7be.apps.googleusercontent.com&access_type=offline&prompt=consent"
               class="btn btn-danger btn-block">
                <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google" />
                Đăng nhập bằng Google
            </a>
        </div>

        <!-- Hiển thị lỗi -->
        <% String error = (String) request.getAttribute("error");
            if (error != null) { %>
        <div class="error text-center"><%= error %></div>
        <% } %>

        <div class="links mt-4">
            <a href="register.jsp">Đăng ký người dùng</a><br>
            <a href="home">Về trang chủ</a>
        </div>
    </div>
</div>

</body>
</html>
