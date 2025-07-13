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
                <img class="rounded-circle" width="50" height="50"
                     src="https://bootstrapdemos.adminmart.com/matdash/dist/assets/images/profile/user-1.jpg">
            </a>

            <div class="dropdown-menu dropdown-menu-end p-3 mt-3" aria-labelledby="drop1">
                <div class="d-flex align-items-center mb-3 pb-3 border-bottom gap-3">
                    <img class="rounded-circle" width="40" height="40"
                         src="https://bootstrapdemos.adminmart.com/matdash/dist/assets/images/profile/user-1.jpg">
                    <div>
                        <h5 class="mb-0 fs-12">John Doe</h5>
                        <p class="mb-0 text-dark">dav@gmai.com</p>
                    </div>
                </div>
                <div class="message-body">
                    <a class="dropdown-item p-2 h6 rounded-1" href="#">Profile</a>
                    <a class="dropdown-item p-2 h6 rounded-1" href="#">Logout</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
