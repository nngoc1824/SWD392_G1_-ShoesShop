<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Tạo Link thanh toán</title>
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="main-box">
  <div class="checkout">
    <div class="product">
      <form action="/create-payment-link" method="post">
        <p><strong>Tên sản phẩm:</strong> Mì tôm Hảo Hảo ly</p>

        <!-- Tổng tiền -->
        <p><strong>Giá tiền:</strong>
          <input type="number" name="totalPrice" value="1000" readonly> VNĐ
        </p>

        <!-- Số lượng -->
        <p><strong>Số lượng:</strong>
          <input type="number" name="quantity" value="1" readonly>
        </p>

        <!-- Địa chỉ giao hàng -->
        <p><strong>Địa chỉ giao hàng:</strong>
          <input type="text" name="shipAddress" value="123 Đường ABC, Hà Nội" required>
        </p>

        <!-- Trạng thái thanh toán -->
        <input type="hidden" name="paymentStatus" value="PENDING">

        <button type="submit" id="create-payment-link-btn">Tạo Link thanh toán</button>
      </form>
    </div>
  </div>
</div>
</body>
</html>