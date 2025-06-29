<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Tạo Link thanh toán</title>
</head>
<body>
<h2>Thông tin đơn hàng</h2>
<form action="/create-payment-link" method="post">

  <!-- Ẩn ID -->
  <input type="hidden" name="orderId" value="${order.orderId}" />

  <p><strong>Tổng tiền:</strong>
    <input type="number" name="totalPrice" value="${order.totalPrice}" readonly />
  </p>

  <p><strong>Địa chỉ giao hàng:</strong>
    <input type="text" name="shipAddress" value="${order.shipAddress}" />
  </p>

  <p><strong>Trạng thái thanh toán:</strong>
    <input type="text" name="paymentStatus" value="${order.paymentStatus}" readonly />
  </p>

  <button type="submit">Tạo Link thanh toán</button>
</form>
</body>
</html>