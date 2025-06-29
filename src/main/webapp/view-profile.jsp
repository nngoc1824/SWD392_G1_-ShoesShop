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
  <title>Hồ sơ người dùng</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, sans-serif;
      background: #f0f2f5;
      margin: 0;
      padding: 0;
    }

    .profile-container {
      max-width: 600px;
      margin: 50px auto;
      background: white;
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    h2 {
      margin-bottom: 20px;
      color: #333;
    }

    .profile-image img {
      width: 120px;
      height: 120px;
      object-fit: cover;
      border-radius: 50%;
      margin-bottom: 20px;
    }

    .info {
      text-align: left;
      margin-top: 20px;
    }

    .info p {
      margin: 8px 0;
      font-size: 16px;
    }

    .btn-group {
      margin-top: 30px;
    }

    .btn {
      padding: 10px 20px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 6px;
      text-decoration: none;
      font-weight: bold;
      margin: 5px;
      display: inline-block;
    }

    .btn:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>

<div class="profile-container">
  <h2>Hồ sơ của bạn</h2>

  <div class="profile-image">
    <img src="<%= (user.getImage() != null && !user.getImage().isEmpty()) ? user.getImage() : "https://via.placeholder.com/120" %>" alt="Ảnh đại diện" />
  </div>

  <div class="info">
    <p><strong>Họ tên:</strong> <%= user.getFullName() %></p>
    <p><strong>Email:</strong> <%= user.getEmail() %></p>
    <p><strong>Số điện thoại:</strong> <%= user.getPhone() %></p>
    <p><strong>Tên đăng nhập:</strong> <%= user.getUserName() %></p>
    <p><strong>Trạng thái:</strong> <%= user.getStatus() == 1 ? "Hoạt động" : "Bị khóa" %></p>
    <p><strong>Mật khẩu:</strong> •••••••••</p>
    <a href="change-password.jsp" class="btn" style="background-color: #28a745;">🔐 Đổi mật khẩu</a>


  </div>

  <div class="btn-group">
    <a href="profile.jsp" class="btn">✏️ Cập nhật hồ sơ</a>
    <a href="dashboard.jsp" class="btn" style="background-color: #6c757d;">← Quay về Dashboard</a>
  </div>
</div>

</body>
</html>
