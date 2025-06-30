<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="css/css/header.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

<header class="main-header shadow-sm sticky-top bg-white">
    <div class="container d-flex justify-content-between align-items-center py-3">
        <!-- Left: Logo -->
        <a class="logo h4 font-weight-bold text-dark m-0" href="home">Footwear</a>

        <!-- Center: Navigation -->
        <nav>
            <ul class="nav justify-content-center">
                <li class="nav-item"><a class="nav-link" href="home">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="category?type=men">Men</a></li>
                <li class="nav-item"><a class="nav-link" href="category?type=women">Women</a></li>
                <li class="nav-item"><a class="nav-link" href="contact">Contact</a></li>
            </ul>
        </nav>

        <!-- Right: Avatar + Cart -->
        <div class="d-flex align-items-center">
            <a href="profile" class="text-dark mr-3">
                <i class="fas fa-user-circle fa-lg"></i>
            </a>
            <a href="cart" class="text-dark">
                <i class="fas fa-shopping-cart"></i>
                <span class="ml-1">Cart [<c:out value="${cartSize != null ? cartSize : 0}"/>]</span>
            </a>
        </div>
    </div>
</header>
