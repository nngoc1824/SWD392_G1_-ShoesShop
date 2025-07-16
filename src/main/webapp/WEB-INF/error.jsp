<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đã xảy ra lỗi!</title>

    <!-- Bootstrap + FontAwesome (CDN) -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        body {
            background-color: #f8f9fa;
        }

        .error-container {
            max-width: 600px;
            margin: 100px auto;
            padding: 40px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .error-icon {
            font-size: 4rem;
            color: #dc3545;
            margin-bottom: 20px;
        }

        .error-message {
            font-size: 1.5rem;
            font-weight: 500;
            color: #333;
        }

        .back-btn {
            margin-top: 30px;
        }
    </style>
</head>
<body>

<div class="error-container">
    <div class="error-icon">
        <i class="fas fa-exclamation-triangle"></i>
    </div>
    <div class="error-message">
        Rất tiếc! Đã xảy ra lỗi trong quá trình xử lý yêu cầu của bạn.
    </div>

    <p class="text-muted mt-3">Vui lòng thử lại sau hoặc quay về trang chủ.</p>

    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-primary back-btn">
        <i class="fas fa-arrow-left mr-1"></i> Quay về trang chủ
    </a>
</div>

</body>
</html>
