<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entites.User" %>
<%@ page session="true" %>
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
</head>
<body>
<h2>Xin chào, <%= user.getFullName() %>!</h2>
<p>Email: <%= user.getEmail() %></p>
<p>Số điện thoại: <%= user.getPhone() %></p>

<a href="logout">Đăng xuất</a>
<a href="profile.jsp">Cập nhật hồ sơ</a>

</body>
</html>
