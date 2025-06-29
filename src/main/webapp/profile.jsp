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
  <title>Cập nhật thông tin</title>
  <style>
    body {
      font-family: Arial;
      background: #f8f8f8;
    }
    .container {
      width: 500px;
      margin: 50px auto;
      background: white;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 0 8px #ccc;
    }
    input[type=text], input[type=email], input[type=submit] {
      width: 100%;
      padding: 10px;
      margin-top: 10px;
    }
    .error {
      color: red;
      margin-top: 10px;
    }
    .success {
      color: green;
    }
  </style>
</head>
<body>
<div class="container">
  <h2>Cập nhật hồ sơ</h2>
  <form action="user" method="post">
    <input type="hidden" name="action" value="updateProfile" />

    <label>Họ và tên</label>
    <input type="text" name="fullName" value="<%= user.getFullName() %>" required />

    <label>Email</label>
    <input type="email" name="email" value="<%= user.getEmail() %>" required />

    <label>Số điện thoại</label>
    <input type="text" name="phone" value="<%= user.getPhone() %>" required />

    <input type="submit" value="Cập nhật" />
  </form>

  <div class="error">
    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
  </div>
  <div class="success">
    <%= request.getAttribute("success") != null ? request.getAttribute("success") : "" %>
  </div>
</div>
</body>
</html>
