<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: phand
  Date: 6/28/2025
  Time: 9:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
</head>
<body>
<div class="navbar navbar-expand-lg p-10 pt-md-10">
    <div class="toggle-button">
        <button class="btn btn-link text-dark" type="button" id="sidebarToggle">
            <i class="fa-solid fa-bars"></i>
        </button>

    </div>
    <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
        <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle d-flex align-items-center gap-2 lh-base"
               href="#" id="drop1" data-bs-toggle="dropdown" aria-expanded="false">
                <c:if test="${not empty sessionScope.user.image}">
                    <img src="${sessionScope.user.image}" alt="Avatar" class="rounded-circle" width="50"
                         height="50">
                </c:if>
                <c:if test="${empty sessionScope.user.image}">
                    <i class="fas fa-user-circle fa-lg mr-2"></i>
                </c:if>
            </a>

            <div class="dropdown-menu dropdown-menu-end p-3 mt-3" aria-labelledby="drop1">
                <div class="d-flex align-items-center mb-3 pb-3 border-bottom gap-3">
                    <c:if test="${not empty sessionScope.user.image}">
                        <img src="${sessionScope.user.image}" alt="Avatar" class="rounded-circle mr-2" width="30"
                             height="30">
                    </c:if>
                    <c:if test="${empty sessionScope.user.image}">
                        <i class="fas fa-user-circle fa-lg mr-2"></i>
                    </c:if>

                    <div>
                        <h5 class="mb-0 fs-12">${sessionScope.user.fullName}</h5>
                        <p class="mb-0 text-dark">${sessionScope.user.email}</p>
                    </div>
                </div>
                <div class="message-body">
                    <a class="dropdown-item p-2 h6 rounded-1" href="dashboard.jsp">Profile</a>
                    <a class="dropdown-item p-2 h6 rounded-1" href="user?action=logout">Logout</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
