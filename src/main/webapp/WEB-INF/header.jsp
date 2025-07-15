<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="entites.User" %>
<%
    User user = (User) session.getAttribute("user");
%>

<!-- Bootstrap JS + FontAwesome -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link rel="stylesheet" href="css/css/header.css">

<!-- 🔷 Header -->
<header class="main-header shadow-sm sticky-top bg-white">
    <div class="container d-flex justify-content-between align-items-center py-3">
        <!-- Logo -->
        <a class="logo h4 font-weight-bold text-dark m-0" href="home">Footwear</a>

        <!-- Navigation -->
        <nav>
            <ul class="nav justify-content-center">
                <li class="nav-item"><a class="nav-link" href="home">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="category?type=men">Men</a></li>
                <li class="nav-item"><a class="nav-link" href="category?type=women">Women</a></li>
                <li class="nav-item"><a class="nav-link" href="contact">Contact</a></li>
            </ul>
        </nav>

        <!-- User + Cart -->
        <div class="d-flex align-items-center">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <!-- Dropdown Menu -->
                    <div class="dropdown mr-3">
                        <a class="btn btn-outline-dark dropdown-toggle d-flex align-items-center" href="#"
                           role="button" id="userDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.image}">
                                    <img src="${sessionScope.user.image}" alt="Avatar" class="rounded-circle mr-2" width="30" height="30">
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-user-circle fa-lg mr-2"></i>
                                </c:otherwise>
                            </c:choose>
                            <span>${sessionScope.user.fullName}</span>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="dashboard.jsp">👤 Xem hồ sơ</a>
                            <a class="dropdown-item" href="change-password.jsp">🔒 Đổi mật khẩu</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item text-danger" href="user?action=logout">🚪 Đăng xuất</a>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <a href="login.jsp" class="btn btn-sm btn-outline-primary mr-2">Đăng nhập</a>
                    <a href="register.jsp" class="btn btn-sm btn-outline-success">Đăng ký</a>
                </c:otherwise>
            </c:choose>

            <!-- Cart -->
            <a href="cart" class="text-dark">
                <i class="fas fa-shopping-cart"></i>
                <span class="ml-1">Cart [<c:out value="${cartSize != null ? cartSize : 0}"/>]</span>
            </a>
        </div>
    </div>
</header>
