<%--
  Created by IntelliJ IDEA.
  User: td532
  Date: 30/06/2025
  Time: 10:16 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/create" method="get">

  <!-- Ẩn ID -->
  <input type="hidden" name="cartId" value="1" />

  <button type="submit">Tạo Link thanh toán</button>
</form>
</body>
</html>
