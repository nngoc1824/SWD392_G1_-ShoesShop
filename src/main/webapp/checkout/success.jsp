<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán thành công</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            padding: 40px;
        }
        .container {
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            max-width: 600px;
            margin: auto;
            box-shadow: 0 0 10px #ccc;
            text-align: center;
        }
        h2 {
            color: #28a745; /* Màu xanh thành công */
        }
        p {
            margin: 20px 0;
        }
        .btn {
            background: #007bff;
            color: white;
            border: none;
            padding: 10px 15px;
            text-decoration: none;
            display: inline-block;
            cursor: pointer;
            border-radius: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>🎉 Cảm ơn bạn!</h2>
    <p>Đơn hàng đã được thanh toán thành công qua PayOS.</p>
    <a href="${pageContext.request.contextPath}/home" class="btn">🏠 Quay về trang chủ</a>
</div>
</body>
</html>
