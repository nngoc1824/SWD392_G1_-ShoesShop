<%--
  Created by IntelliJ IDEA.
  User: tranb
  Date: 6/28/2025
  Time: 9:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Shoes Shop - Home</title>
    <link rel="stylesheet" href="css/homepage.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<!-- 🔹 Navbar -->
<div class="navbar">
    <div class="logo">Shoes Shop</div>
    <div class="nav-links">
        <a href="#">Home</a>
        <a href="#">Product</a>
        <a href="#">Men</a>
        <a href="#">Women</a>
        <a href="#">Contact</a>
    </div>
    <div class="nav-icons">
        <i class="fas fa-user"></i>
        <a href="cart.jsp"><i class="fas fa-shopping-cart"></i> Cart (0)</a>
    </div>
</div>

<!-- 🔹 Banner -->
<div class="banner">
    <img src="img/banner.jpg" alt="Banner" class="banner-image" />
    <div class="banner-text">
        <h2>Finding Your Perfect Shoes</h2>
        <p>Our North American Field Guides provide tips for identifying birds... and shoes 😄</p>
        <button>Shop Now <i class="fas fa-chevron-down"></i></button>
    </div>
</div>

<!-- 🔹 Main Content -->
<div class="main-container">
    <!-- Sidebar Filters -->
    <div class="sidebar">
        <h3>Filters <a href="#">Clear All</a></h3>

        <div class="filter-group">
            <p>For</p>
            <label><input type="checkbox" checked /> Mens</label>
            <label><input type="checkbox" /> Womens</label>
            <label><input type="checkbox" /> Kids</label>
        </div>

        <div class="filter-group">
            <p>Type</p>
            <label><input type="checkbox" checked /> New Arrivals / Trending</label>
            <label><input type="checkbox" /> Sneaker</label>
            <label><input type="checkbox" /> Boots</label>
            <label><input type="checkbox" /> Formal Shoes</label>
        </div>

        <div class="filter-group">
            <p>Price</p>
            <input type="range" min="0" max="1000" />
        </div>
    </div>

    <!-- Product Grid -->
    <div class="product-grid">
        <div class="product-card">
            <img src="images/shoe1.png" alt="Product">
            <h4>Product Title</h4>
            <p>$20.00</p>
            <button><i class="fas fa-cart-plus"></i></button>
        </div>

        <div class="product-card">
            <img src="images/shoe2.png" alt="Product">
            <h4>Product Title</h4>
            <p>$32.00</p>
            <button><i class="fas fa-cart-plus"></i></button>
        </div>

        <div class="product-card">
            <img src="images/shoe3.png" alt="Product">
            <h4>Product Title</h4>
            <p><span class="old-price">$75.00</span> <span class="sale-price">$50.00</span></p>
            <button><i class="fas fa-cart-plus"></i></button>
        </div>

        <!-- Add more products as needed -->
    </div>
</div>

<!-- 🔹 Footer -->
<footer>
    <div class="footer-links">
        <a href="#">Category X</a>
        <a href="#">Category Y</a>
        <a href="#">Category Z</a>
    </div>
    <div class="footer-social">
        <i class="fab fa-facebook"></i>
        <i class="fab fa-instagram"></i>
    </div>
    <div class="footer-subscribe">
        <input type="email" placeholder="Enter Email" />
        <button>Subscribe</button>
    </div>
</footer>

</body>
</html>
