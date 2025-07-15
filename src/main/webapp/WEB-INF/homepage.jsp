<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Footwear Shop</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,600&display=swap" rel="stylesheet">

    <!-- Bootstrap only -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"> <!-- Optional custom CSS -->

    <style>
        body {
            font-family: 'Montserrat', sans-serif;
        }
        .product-card .old-price {
            text-decoration: line-through;
            color: #333;
            font-size: 0.9rem;
        }
        .product-card .new-price {
            color: #e74c3c;
            font-weight: 600;
            font-size: 1.1rem;
        }
        .product-card .btn {
            margin-right: 5px;
        }
    </style>
</head>

<body>
<div class="container-fluid p-0">
    <%@ include file="header.jsp" %>

    <!-- 🔹 Banner -->
    <div class="jumbotron jumbotron-fluid text-center bg-light" style="background: url('${pageContext.request.contextPath}/images/cover-img-1.jpg') center/cover no-repeat;">
        <div class="container text-white">
            <h1 class="display-4 text-white">Men’s Shoes Collection</h1>
            <p class="lead">Up to 50% Off – Trending styles and new arrivals.</p>
            <a href="product-list.jsp" class="btn btn-primary btn-lg">Shop Collection</a>
        </div>
    </div>


    <!-- 🔹 Featured Categories -->
    <div class="row text-center mb-5">
        <div class="col-md-6">
            <a href="category.jsp?gender=men">
                <img src="${pageContext.request.contextPath}/images/men.jpg" alt="Men's Collection" class="img-fluid">
                <h3>Shop Men's Collection</h3>
            </a>
        </div>
        <div class="col-md-6">
            <a href="category.jsp?gender=women">
                <img src="${pageContext.request.contextPath}/images/women.jpg" alt="Women's Collection" class="img-fluid">
                <h3>Shop Women's Collection</h3>
            </a>
        </div>
    </div>

    <!-- 🔹 Product List -->
    <div class="container mb-5">
        <h2 class="text-center mb-4">Best Sellers</h2>
        <div class="row">
            <c:forEach var="p" items="${products}">
                <div class="col-md-3 mb-4 text-center">
                    <div class="card h-100 product-card">
                        <img src="${p.image}" class="card-img-top" alt="${p.productName}">
                        <div class="card-body">
                            <h5 class="card-title">${p.productName}</h5>

                            <p class="card-text mb-1 old-price">
                                <fmt:formatNumber value="${p.purchaseCost}" type="number" groupingUsed="true"/>₫
                            </p>
                            <p class="card-text new-price">
                                <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>₫
                            </p>

                            <div class="mt-3">
                                <!-- Nút Add to Cart -->
                                <form method="POST" action="cart" style="display:inline;">
                                    <input type="hidden" name="action" value="add">
                                    <input type="hidden" name="productId" value="${p.productId}">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn btn-outline-success btn-sm">Add to Cart</button>
                                </form>

                                <!-- Nút Buy Now -->
                                <form method="POST" action="cart" style="display:inline;">
                                    <input type="hidden" name="action" value="buyNow">
                                    <input type="hidden" name="productId" value="${p.productId}">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn btn-primary btn-sm">Buy Now</button>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

</div>

<!-- Bootstrap CDN fallback nếu bạn cần -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
