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
  <title>Dashboard</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: linear-gradient(to right, #e0eafc, #cfdef3);
      margin: 0;
      padding: 0;
    }

    .container {
      max-width: 600px;
      margin: 60px auto;
      background: white;
      padding: 40px 30px;
      border-radius: 12px;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    h2 {
      color: #333;
      margin-bottom: 30px;
    }

    .avatar {
      width: 120px;
      height: 120px;
      object-fit: cover;
      border-radius: 50%;
      border: 3px solid #ccc;
      margin-bottom: 20px;
    }

    .info {
      font-size: 16px;
      color: #555;
      margin-bottom: 10px;
    }

    .buttons a {
      display: inline-block;
      margin: 10px 10px 0 10px;
      padding: 10px 18px;
      background-color: #4285f4;
      color: white;
      text-decoration: none;
      border-radius: 6px;
      font-weight: bold;
      transition: background-color 0.3s ease;
    }

    .buttons a:hover {
      background-color: #3367d6;
    }

    .buttons .logout {
      background-color: #dc3545;
    }

    .buttons .logout:hover {
      background-color: #b52a37;
    }
  </style>
</head>
<body>

<div class="container">
  <h2>Chào mừng, <%= user.getFullName() %>!</h2>

  <!-- ✅ Hiển thị ảnh đại diện nếu có -->
  <%
    String imagePath = user.getImage();
    if (imagePath != null && !imagePath.trim().isEmpty()) {
  %>
  <img src="<%= imagePath %>" alt="Avatar" class="avatar" />
  <%
  } else {
  %>
  <img src="default-avatar.png" alt="No Avatar" class="avatar" />
  <%
    }
  %>

  <!-- ✅ Hiển thị thông tin người dùng -->
  <div class="info"><strong>Email:</strong> <%= user.getEmail() %></div>
  <div class="info"><strong>Số điện thoại:</strong> <%= user.getPhone() %></div>
  <div class="info"><strong>Tên đăng nhập:</strong> <%= user.getUserName() %></div>

  <!-- ✅ Các nút chức năng -->
  <div style="margin-top: 20px; text-align: center;">
    <a href="view-profile.jsp"
       style="display: inline-block;
              background-color: #4285f4;
              color: white;
              padding: 10px 20px;
              border-radius: 6px;
              text-decoration: none;
              font-weight: bold;
              transition: background-color 0.3s ease;">
      👤 Xem hồ sơ
    </a>
  </div>

  <div class="buttons">
    <a href="profile.jsp">Chỉnh sửa hồ sơ</a>
    <a href="user?action=logout" class="logout">Đăng xuất</a>
  </div>

  <div class="buttons">
    <a href="homepage.jsp">Home Page</a>
  </div>
</div>

</body>
</html>
