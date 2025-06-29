<%@ page contentType="text/html; charset=UTF-8" %>
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
  <title>Cập nhật hồ sơ</title>
  <style>
    * { box-sizing: border-box; }
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: linear-gradient(to right, #e0eafc, #cfdef3);
      margin: 0; padding: 0;
    }

    .container {
      max-width: 550px;
      margin: 60px auto;
      background: white;
      padding: 40px 30px;
      border-radius: 12px;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
    }

    h2 {
      text-align: center;
      margin-bottom: 25px;
      color: #333;
    }

    label {
      font-weight: 600;
      display: block;
      margin-top: 15px;
      color: #444;
    }

    input[type="text"],
    input[type="email"],
    input[type="password"],
    input[type="file"] {
      width: 100%;
      padding: 10px 12px;
      margin-top: 8px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 15px;
    }

    input[type="submit"] {
      width: 100%;
      margin-top: 25px;
      padding: 12px;
      background-color: #4285f4;
      border: none;
      border-radius: 6px;
      color: white;
      font-weight: bold;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    input[type="submit"]:hover {
      background-color: #3367d6;
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
  </style>
</head>
<body>

<div class="container">
  <h2>Cập nhật hồ sơ</h2>

  <form action="user" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="updateProfile" />

    <label for="fullName">Họ và tên</label>
    <input type="text" name="fullName" id="fullName" value="<%= user.getFullName() %>" required />

    <label for="email">Email</label>
    <input type="email" name="email" id="email" value="<%= user.getEmail() %>" required />

    <label for="phone">Số điện thoại</label>
    <input type="text" name="phone" id="phone" value="<%= user.getPhone() %>" required />

    <label for="image">Ảnh đại diện</label>
    <input type="file" accept="image/png, image/jpeg, image/jpg" name="image" id="image" />

    <hr style="margin: 30px 0;">



    <input type="submit" value="Cập nhật" />
  </form>

  <% String error = (String) request.getAttribute("error");
    if (error != null) { %>
  <div class="error"><%= error %></div>
  <% } %>

  <% String success = (String) request.getAttribute("success");
    if (success != null) { %>
  <div class="success"><%= success %></div>
  <% } %>
</div>

<div style="text-align: center; margin-top: 20px;">
  <a href="dashboard.jsp" style="
      display: inline-block;
      padding: 10px 20px;
      background-color: #6c757d;
      color: white;
      text-decoration: none;
      border-radius: 6px;
      font-weight: bold;
      transition: background-color 0.3s ease;">
    ← Về Dashboard
  </a>
</div>

</body>
</html>
