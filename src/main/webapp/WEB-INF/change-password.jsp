<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entites.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Đổi mật khẩu</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(to right, #e0eafc, #cfdef3);
            margin: 0;
            padding: 0;
        }

        .container {
            width: 400px;
            margin: 60px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
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

        input[type="password"], input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin-top: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 15px;
        }

        input[type="submit"] {
            margin-top: 25px;
            background-color: #4285f4;
            color: white;
            border: none;
            cursor: pointer;
            font-weight: bold;
        }

        input[type="submit"]:hover {
            background-color: #3367d6;
        }

        .toggle-password {
            margin-top: 10px;
            font-size: 14px;
        }

        .error, .success {
            margin-top: 15px;
            padding: 10px;
            text-align: center;
            border-radius: 5px;
            font-size: 14px;
        }

        .error {
            background-color: #ffe6e6;
            color: #cc0000;
        }

        .success {
            background-color: #e6ffee;
            color: #008000;
        }

        .back-link {
            text-align: center;
            margin-top: 20px;
        }

        .back-link a {
            text-decoration: none;
            color: #555;
        }

        .back-link a:hover {
            color: #000;
        }
    </style>
    <script>
        function togglePassword() {
            const inputs = document.querySelectorAll('input[type="password"]');
            inputs.forEach(input => {
                input.type = input.type === "password" ? "text" : "password";
            });
        }
    </script>
</head>
<body>

<div class="container">
    <h2>Đổi mật khẩu</h2>

    <form action="user" method="post">
        <input type="hidden" name="action" value="changePassword" />

        <label for="currentPassword">Mật khẩu hiện tại:</label>
        <input type="password" name="currentPassword" id="currentPassword" required />

        <label for="newPassword">Mật khẩu mới:</label>
        <input type="password" name="newPassword" id="newPassword" required />

        <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
        <input type="password" name="confirmPassword" id="confirmPassword" required />

        <div class="toggle-password">
            <input type="checkbox" onclick="togglePassword()" /> Hiển thị mật khẩu
        </div>

        <input type="submit" value="Đổi mật khẩu" />
    </form>

    <% String error = (String) request.getAttribute("error");
        if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <% String success = (String) request.getAttribute("success");
        if (success != null) { %>
    <div class="success"><%= success %></div>
    <% } %>

    <div class="back-link">
        <a href="WEB-INF/profile.jsp">← Quay lại hồ sơ</a>
    </div>
</div>

</body>
</html>
