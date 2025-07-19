<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="entites.User" %>
<%
    User user = (User) session.getAttribute("user");
%>

<!-- CSS: Bootstrap & Custom -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/css/header.css">

<!-- ✅ Custom dropdown CSS -->
<style>
    .custom-dropdown {
        position: relative;
        display: inline-block;
    }

    .dropdown-toggle-manual {
        cursor: pointer;
        padding: 6px 12px;
        border: 1px solid #ccc;
        border-radius: 5px;
        background-color: #fff;
        display: flex;
        align-items: center;
        transition: background-color 0.2s ease;
    }

    .dropdown-toggle-manual:hover {
        background-color: #f8f9fa;
    }

    .dropdown-menu-manual {
        position: absolute;
        right: 0;
        top: 100%;
        z-index: 1000;
        display: none;
        min-width: 200px;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 5px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
        margin-top: 6px;
        padding: 8px 0;
    }

    .dropdown-menu-manual.show {
        display: block;
    }

    .dropdown-item-manual {
        display: block;
        padding: 8px 16px;
        color: #333;
        text-decoration: none;
        transition: background-color 0.15s;
    }

    .dropdown-item-manual:hover {
        background-color: #f0f0f0;
    }

    .dropdown-divider-manual {
        height: 1px;
        background-color: #e0e0e0;
        margin: 8px 0;
    }

    .dropdown-toggle-manual img {
        border-radius: 50%;
        object-fit: cover;
        margin-right: 8px;
    }
</style>

<!-- 🔷 Header -->
<header class="main-header shadow-sm sticky-top bg-white">
    <div class="container d-flex justify-content-between align-items-center py-3">
        <!-- Logo -->
        <a class="logo h4 font-weight-bold text-dark m-0" href="${pageContext.request.contextPath}/home">Footwear</a>

        <!-- Navigation -->
        <nav>
            <ul class="nav justify-content-center">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/category?type=men">Men</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/category?type=women">Women</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/contact">Contact</a></li>
            </ul>
        </nav>

        <!-- User + Cart -->
        <div class="d-flex align-items-center">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <!-- ✅ Manual Dropdown -->
                    <div class="custom-dropdown mr-3">
                        <div class="dropdown-toggle-manual" onclick="toggleDropdown()">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.image}">
                                    <img src="${sessionScope.user.image}" alt="Avatar" width="30" height="30">
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-user-circle fa-lg mr-2"></i>
                                </c:otherwise>
                            </c:choose>
                            <span>${sessionScope.user.fullName}</span>
                            <i class="fas fa-caret-down ml-2"></i>
                        </div>
                        <div class="dropdown-menu-manual" id="userDropdownMenu">
                            <a class="dropdown-item-manual" href="${pageContext.request.contextPath}/user?action=viewProfile">👤 Xem hồ sơ</a>
                            <a class="dropdown-item-manual" href="${pageContext.request.contextPath}/user?action=changePassword">🔒 Đổi mật khẩu</a>
                            <div class="dropdown-divider-manual"></div>
                            <a class="dropdown-item-manual text-danger" href="${pageContext.request.contextPath}/user?action=logout">🚪 Đăng xuất</a>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/user?action=login" class="btn btn-sm btn-outline-primary mr-2">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/user?action=register" class="btn btn-sm btn-outline-success">Đăng ký</a>
                </c:otherwise>
            </c:choose>

            <!-- Cart -->
            <a href="${pageContext.request.contextPath}/cart" class="text-dark">
                <i class="fas fa-shopping-cart"></i>
                <span class="ml-1">Cart [<c:out value="${cartSize != null ? cartSize : 0}"/>]</span>
            </a>
        </div>
    </div>
</header>

<!-- ✅ Dropdown JS -->
<script>
    function toggleDropdown() {
        const menu = document.getElementById("userDropdownMenu");
        menu.classList.toggle("show");
    }

    // Close dropdown if clicking outside
    window.addEventListener("click", function (e) {
        const toggle = document.querySelector(".dropdown-toggle-manual");
        const menu = document.getElementById("userDropdownMenu");
        if (!toggle.contains(e.target) && !menu.contains(e.target)) {
            menu.classList.remove("show");
        }
    });
</script>
