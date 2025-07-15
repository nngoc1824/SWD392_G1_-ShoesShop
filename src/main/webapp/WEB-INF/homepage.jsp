<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Footwear Shop</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,600&display=swap" rel="stylesheet">

    <!-- Bootstrap only -->
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/style.css"> <!-- Optional custom CSS -->
</head>

<body>
<div class="container-fluid p-0">
    <%@ include file="header.jsp" %>

    <!-- 🔹 Banner -->
    <div class="jumbotron jumbotron-fluid text-center bg-light">
        <div class="container">
            <h1 class="display-4">Men’s Shoes Collection</h1>
            <p class="lead">Up to 50% Off – Trending styles and new arrivals.</p>
            <a href="product-list.jsp" class="btn btn-primary btn-lg">Shop Collection</a>
        </div>
    </div>

    <!-- 🔹 Featured Categories -->
    <div class="row text-center mb-5">
        <div class="col-md-6">
            <a href="category.jsp?gender=men">
                <img src="../images/men.jpg" alt="Men's Collection" class="img-fluid">
                <h3>Shop Men's Collection</h3>
            </a>
        </div>
        <div class="col-md-6">
            <a href="category.jsp?gender=women">
                <img src="../images/women.jpg" alt="Women's Collection" class="img-fluid">
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
                    <div class="card h-100">
                        <img src="${p.image}" class="card-img-top" alt="${p.productName}">
                        <div class="card-body">
                            <h5 class="card-title">${p.productName}</h5>
                            <p class="card-text text-danger">$${p.price}</p>
                            <a href="product-detail?id=${p.productId}" class="btn btn-sm btn-outline-primary">View</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- 🔹 Footer -->
    <footer class="bg-dark text-white text-center py-4">
        <p>&copy; <script>document.write(new Date().getFullYear())</script> Footwear. Powered by JSP + Bootstrap.</p>
    </footer>
</div>

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

</body>
</html>
