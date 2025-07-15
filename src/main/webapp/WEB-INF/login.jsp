<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">

    <!-- Optional custom CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/user_action/login.css">

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

    <form action="user" method="post">
        <input type="hidden" name="action" value="login" />

        <div class="form-group">
            <label for="userName">Tên đăng nhập</label>
            <input type="text" name="userName" id="userName" required />
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <input type="password" name="password" id="password" required />
            <br/>
            <input type="checkbox" onclick="togglePassword()"> Hiển thị mật khẩu
        </div>

        <div class="form-actions">
            <input type="submit" value="Đăng nhập" />
        </div>
    </form>

    <!-- Google Login -->
    <div class="google-login">
        <a href="https://accounts.google.com/o/oauth2/v2/auth?scope=openid%20email%20profile&redirect_uri=http://localhost:8080/SWD392_ShoesShop_war_exploded/login-google&response_type=code&client_id=841040454506-pttbflftv7fu6qsdqpn7ju434hpag7be.apps.googleusercontent.com&access_type=offline&prompt=consent">
            <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google">
            Đăng nhập bằng Google
        </a>
    </div>

    <% String error = (String) request.getAttribute("error"); if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <div class="links">
        <p><a href="WEB-INF/register.jsp">Chưa có tài khoản? Đăng ký</a></p>
        <p><a href="home">Về trang chủ</a></p>
    </div>
</div>

</body>
</html>
