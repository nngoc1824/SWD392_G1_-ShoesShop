<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/user_action/register.css">
    <title>Đăng ký tài khoản</title>


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
        function validateForm() {
            const firstName = document.forms["registerForm"]["firstName"].value.trim();
            const lastName = document.forms["registerForm"]["lastName"].value.trim();
            const email = document.forms["registerForm"]["email"].value.trim();
            const phoneNumber = document.forms["registerForm"]["phoneNumber"].value.trim();
            const password = document.forms["registerForm"]["password"].value;
            const gender = document.forms["registerForm"]["gender"].value;

            // Check if all required fields are filled
            if (!firstName || !lastName || !email || !phoneNumber || !password || !gender) {
                alert("All fields are required!");
                return false;
            }

            // Validate first name and last name (only letters and spaces)
            const namePattern = /^[A-Za-z\s]+$/;
            if (!namePattern.test(firstName)) {
                alert("First name should contain only letters and spaces!");
                return false;
            }
            if (!namePattern.test(lastName)) {
                alert("Last name should contain only letters and spaces!");
                return false;
            }

            // Validate email format
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                alert("Please enter a valid email address!");
                return false;
            }

            // Validate phone number (10 digits)
            const phonePattern = /^\d{10}$/;
            if (!phonePattern.test(phoneNumber)) {
                alert("Phone number must be exactly 10 digits!");
                return false;
            }

            // Validate password strength
            if (password.length < 8) {
                alert("Password must be at least 8 characters long!");
                return false;
            }

            if (!/(?=.*[a-z])/.test(password)) {
                alert("Password must contain at least one lowercase letter!");
                return false;
            }

            if (!/(?=.*[A-Z])/.test(password)) {
                alert("Password must contain at least one uppercase letter!");
                return false;
            }

            if (!/(?=.*\d)/.test(password)) {
                alert("Password must contain at least one number!");
                return false;
            }

            if (!/(?=.*[@$!%*?&])/.test(password)) {
                alert("Password must contain at least one special character (@$!%*?&)!");
                return false;
            }

            return true;
        }
    </script>
</head>
<body>

<div class="overlay"></div>

<div class="login-container">
    <h2>Đăng ký tài khoản</h2>

    <form action="user" method="post" onsubmit="return validateForm()">
        <input type="hidden" name="action" value="register" />

        <div class="form-group">
            <label for="userName">Tên đăng nhập</label>
            <input type="text" name="userName" id="userName" required />
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" name="email" id="email" required />
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <input type="password" name="password" id="password" required />
            <br/>
            <input type="checkbox" onclick="togglePassword('password')"> Hiển thị mật khẩu
        </div>

        <div class="form-group">
            <label for="confirmPassword">Xác nhận mật khẩu</label>
            <input type="password" name="confirmPassword" id="confirmPassword" required />
            <br/>
            <input type="checkbox" onclick="togglePassword('confirmPassword')"> Hiển thị mật khẩu
        </div>

        <div class="form-group">
            <label for="fullName">Họ và tên</label>
            <input type="text" name="fullName" id="fullName" required />
        </div>

        <div class="form-group">
            <label for="phone">Số điện thoại</label>
            <input type="text" name="phone" id="phone" required />
        </div>

        <div class="form-actions">
            <input type="submit" value="Đăng ký" />
        </div>
    </form>

    <% String error = (String) request.getAttribute("error"); if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <div class="links">
        <p><a href="user?action=login">Đã có tài khoản? Đăng nhập</a></p>
        <p><a href="home">Về trang chủ</a></p>
    </div>
</div>

</body>
</html>
